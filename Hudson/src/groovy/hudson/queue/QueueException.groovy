package hudson.queue

class QueueException extends Exception {

    QueueException() {
        super()
    }

    QueueException(String message) {
        super(message)
    }

    QueueException(String message, Throwable cause) {
        super(message, cause)
    }

    QueueException(Throwable cause) {
        super(cause)
    }

}