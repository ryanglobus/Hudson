package hudson.queue

import grails.util.Environment
import groovy.transform.PackageScope
import groovyx.net.http.RESTClient
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator

class Queue<E> {

    private static final String PROJECT_ID;
    private static final String TOKEN;
    static {
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                PROJECT_ID = '5366e92fd5bdfc000900004a'
                TOKEN = 'c3WPoOKtPPApd59I9ddvo_5zhBA'
                break
            case Environment.TEST:
                PROJECT_ID = '536881e6e00508003b000022'
                TOKEN = 'pd8Klcip8-xWeiCe_bQRZJH6Bgc'
                break
            case Environment.PRODUCTION:
                PROJECT_ID = '5366ea3fcc7580000900005b'
                TOKEN = 'TbMwwETgnJItWO8dmMiEvwSlwVg'
                break
        }
    }
    private final StringConverter<E> converter
    private final RESTClient client
    private final String name
    private final String endpoint // without the last '/'

    Queue(String name, StringConverter<E> converter) {
        this.converter = converter
        this.name = name
        this.endpoint = "https://mq-aws-us-east-1.iron.io/1/projects/${PROJECT_ID}/queues/${name}"
        client = new RESTClient(this.endpoint + '/')
    }

    // this method returns the argument after resetting its id.
    Message<E> enqueue(Message<E> msg) throws QueueException {
        String bodyString = converter.generateString(msg.getBody())
        def body = [body: bodyString]
        if (msg.timeout != null) body['timeout'] = msg.timeout
        if (msg.delay != null) body['delay'] = msg.delay
        if (msg.expiresIn != null) body['expires_in'] = msg.expiresIn
        def response = null
        try {
            response = client.post(
                path: 'messages',
                contentType: ContentType.JSON,
                headers: [Authorization: "OAuth ${TOKEN}"],
                body: [messages: [body]]
            )
        } catch (Exception e) {
            throw new QueueException(e)
        }
        checkResponseSuccess(response)
        def ids = response.data['ids']
        if (ids == null || ids.size() == 0) 
            throw new QueueException("enqueue did not return any ids")
        String id = ids[0]
        msg.setId(id)
        return msg
    }

    Message<E> dequeue() throws QueueException {
        def response = null
        try {
            response = client.get(
                path: 'messages',
                contentType: ContentType.JSON,
                headers: [Authorization: "OAuth ${TOKEN}"],
                query: [n: 1]
            )
        } catch (Exception e) {
            throw new QueueException(e)
        }
        // TODO type checks
        checkResponseSuccess(response)
        def messages = response.data['messages']
        if (messages == null || messages.size() == 0) return null
        def message = messages[0]
        Message<E> m = new Message<E>(converter.parseString(message['body']))
        m.setId(message['id'])
        m.timeout = message['timeout']
        return m
    }

    void delete(Message<E> msg) throws QueueException {
        def response = null
        try {
            response = client.delete(
                path: "messages/${msg.getId()}",
                contentType: ContentType.JSON,
                headers: [Authorization: "OAuth ${TOKEN}"]
            )
        } catch (Exception e) {
            throw new QueueException(e)
        }
        checkResponseSuccess(response)
    }

    Integer size() throws QueueException {
        def response = null
        try {
            response = client.get(
                uri: endpoint, // hack since clear doesn't work with trailing '/'
                contentType: ContentType.JSON,
                headers: [Authorization: "OAuth ${TOKEN}"]
            )
        } catch (Exception e) {
            throw new QueueException(e)
        }
        checkResponseSuccess(response)
        return response.data['size']
    }

    // used for dev and testing
    void clear() throws QueueException {
        def response = null
        try {
            response = client.post(
                path: 'clear',
                contentType: ContentType.JSON,
                headers: [Authorization: "OAuth ${TOKEN}"]
            )
        } catch (Exception e) {
            throw new QueueException(e)
        }
        checkResponseSuccess(response)
    }

    String getName() {
        return name
    }

    private static boolean checkResponseSuccess(response) throws QueueException {
        if (response == null)
            throw new QueueException("client response is null")
        if (!(response instanceof HttpResponseDecorator))
            throw new QueueException("client response is of unexpected type ${response.getClass()}")
        if (!response.isSuccess())
            throw new QueueException("client resposne has non-success status code of ${response.status}")
    }

}