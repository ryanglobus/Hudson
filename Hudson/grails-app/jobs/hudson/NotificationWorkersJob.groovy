package hudson

import java.util.concurrent.Semaphore
import groovy.hudson.queue.*
import hudson.Notification
import groovy.hudson.Notification
import static grails.async.Promises.task


class NotificationWorkersJob {
    static triggers = {
      simple startDelay: 0l, repeatInterval: 30_000l // execute job once in 30 seconds
    }

    // TODO WorkersJob have some duplicated code :/
    private static final int NUM_THREADS = 10 // TODO tune this
    private static final Semaphore availableThreadsSem = new Semaphore(NUM_THREADS)
    private static final Queue<Notification> queue = Notification.queue

    def execute() {
        // execute job
        while (true) {
            try {
                availableThreadsSem.acquire()
            } catch (InterruptedException ie) {
                ie.printStackTrace()
                Thread.currentThread.interrupt() // reset interrupt flag
                break
            }
            Message<Notification> msg = queue.dequeue()
            if (msg == null) {
                availableThreadsSem.release()
                break
            }
            def promise = task { // TODO how does local var msg work?
                try {
                    println("Processing notification")
                    Notification n = msg.body
                    n.sendNotification()
                    queue.delete(msg)
                } catch (Exception e) {
                    e.printStackTrace()
                } finally {
                    availableThreadsSem.release()
                }
            }
        }
    }
}
