package hudson.queue

import groovy.transform.PackageScope

class Message<E> {
    private final E body
    private String id = null
    // all Longs are in seconds
    Long timeout = null
    Long delay = null
    Long expiresIn = null

    Message(E body) {
        this.body = body
    }

    E getBody() {
        return body
    }

    String getId() { 
        return id
    }

    @PackageScope
    void setId(String id) {
        this.id = id
    }

}