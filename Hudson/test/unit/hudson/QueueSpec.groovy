package hudson

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import hudson.queue.*
import groovy.hudson.queue.Message
import groovy.hudson.queue.SerializedStringConverter
import groovy.hudson.queue.QueueException

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class QueueSpec extends Specification {

	def setup() {
	}

	def cleanup() {
	}

	void "test empty queue"() {
		given:
		groovy.hudson.queue.Queue<String> queue = new groovy.hudson.queue.Queue<String>('queuespec',
			new SerializedStringConverter<String>(String.class))

		when:
		queue.clear()
		Message<String> nullMessage = queue.dequeue()

		then:
		notThrown(Exception)
		nullMessage == null
		queue.size() == 0
	}

	void "test queue"() {
		given:
		groovy.hudson.queue.Queue<String> queue = new groovy.hudson.queue.Queue<String>('queuespec',
			new SerializedStringConverter<String>(String.class))
		queue.clear()
		String tm = 'hi'
		Message<String> msg = new Message<String>(tm)

		when:
		String id = queue.enqueue(msg).getId()
		Integer firstSize = queue.size()
		Message<String> dm = queue.dequeue()
		queue.delete(dm)
		Integer secondSize = queue.size()

		then:
		id != null
		firstSize == 1
		tm.equals(dm.getBody())
		secondSize == 0
	}

	void "test delay"() {
		given:
		groovy.hudson.queue.Queue<Integer> queue = new groovy.hudson.queue.Queue<Integer>('queuespec',
			new SerializedStringConverter<Integer>(Integer.class))
		queue.clear()
		Integer i1 = 556
		Integer i2 = -22
		int delay = 10
		Message<Integer> m1 = new Message<Integer>(i1)
		m1.setDelay(delay)
		Message<Integer> m2 = new Message<Integer>(i2)
		queue.enqueue(m1)
		queue.enqueue(m2)

		when:
		Integer s1 = queue.size()
		Message<Integer> d1 = queue.dequeue()
		Message<Integer> d2 = queue.dequeue()
		Thread.sleep(delay * 1000)
		Message<Integer> d3 = queue.dequeue()
		Integer s2 = queue.size()
		[d1, d2, d3].each() {if (it != null) queue.delete(it)}
		Integer s3 = queue.size()

		then:
		s1 == 2
		d1.getBody() == i2
		d2 == null
		d3 != null
		d3.getBody() == i1
		s2 == 2
		s3 == 0
	}

	void "test queue exception"() {
		given:
		groovy.hudson.queue.Queue<String> queue = new groovy.hudson.queue.Queue<String>('queuespec',
			new SerializedStringConverter<String>(String.class))
		queue.clear()
		Message<String> m = new Message<String>('hi')
		queue.enqueue(m)
		queue.delete(m)

		when:
		queue.delete(m)

		then:
		thrown(QueueException)
	}
}



