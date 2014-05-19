package hudson

import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import hudson.queue.Message
import hudson.queue.Queue
import java.util.concurrent.Semaphore
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.springframework.orm.hibernate3.SessionFactoryUtils
import static grails.async.Promises.task

// TODO set TTL on queries (one month)
class QueryQueueWorkersJob {
    static triggers = {
      simple startDelay: 0l, repeatInterval: 30_000l // execute job once in 30 seconds
    }

    private static final int NUM_THREADS = 10 // TODO tune this
    private static final Semaphore availableThreadsSem = new Semaphore(NUM_THREADS)
    private static final Queue<Query> queue = Query.queue

    def execute() {
        while (true) {
            try {
                availableThreadsSem.acquire()
            } catch (InterruptedException ie) {
                ie.printStackTrace()
                Thread.currentThread().interrupt() // reset interrupt flag
                break
            }
            Message<Query> msg = queue.dequeue()
            if (msg == null) {
                availableThreadsSem.release()
                break
            }
            def promise = task { // TODO how does local var msg work?
                Query.withNewSession {
                    try {
                        Query query = msg.body
                        if (query.isCancelled) return
                        query.searchAndSaveNewPosts()
                        Message<Query> newMsg = new Message<Query>(query)
                        newMsg.delay = msg.delay
                        // TODO how do I make below atomic? what if query already got again before deleted?
                        queue.enqueue(newMsg)
                        queue.delete(msg)
                        return posts
                    } catch (Exception e) {
                        e.printStackTrace()
                        return []
                    } finally {
                        availableThreadsSem.release()
                    }
                }
            }
        }
    }
}