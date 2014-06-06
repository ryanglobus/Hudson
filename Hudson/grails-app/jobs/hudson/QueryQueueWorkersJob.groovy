package hudson

import java.util.concurrent.Executors
import java.util.concurrent.ExecutorService
import groovy.hudson.queue.Message
import groovy.hudson.queue.Queue
import java.util.concurrent.Semaphore
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.Transaction
import org.springframework.orm.hibernate3.SessionFactoryUtils
import static grails.async.Promises.task
import grails.util.Environment

// TODO set TTL on queries (one month)
class QueryQueueWorkersJob {
    static triggers = {
      simple startDelay: 0l, repeatInterval: 5_000l // execute job once in 5 seconds
    }

    // TODO WorkersJob have some duplicated code :/
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
            def promise = Query.async.task { // TODO how does local var msg work?
                Query.withNewSession {
                    try {
                        println("Processing a Craigslist query...")
                        Query query = msg.body
                        println("\tat URL: ${query.craigslistUrl(true)}")
                        if (query.isCancelled) return
                        query.searchAndSaveNewPosts()
                        Message<Query> newMsg = new Message<Query>(query)
                        println(msg.delay)
                        if (msg.delay == null || msg.delay < 60) {
                            if (Environment.current.equals(Environment.PRODUCTION)) {
                                newMsg.delay = 600 // every 10 minutes
                            } else {
                                newMsg.delay = 60 // every minute
                            }
                        } else {
                            newMsg.delay = msg.delay
                        }
                        // TODO how do I make below atomic? what if query already got again before deleted?
                        queue.enqueue(newMsg)
                        queue.delete(msg)
                    } catch (Exception e) {
                        if (e instanceof org.hibernate.LazyInitializationException) {
                            println("LazyInitializationException: ${e.getMessage()}")
                        } else {
                            e.printStackTrace()
                        }
                    } finally {
                        availableThreadsSem.release()
                        return null
                    }
                }
            }
        }
    }
}
