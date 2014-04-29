package hudson

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Post)
class PostSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something2"() {
		when:
		User newUser = new User();
		newUser.email = "ckortel@stanford.edu";
		newUser.phone = "4108977488";
		newUser.firstName = "Kelly";
		Post newPost = new Post();
		newPost.link = "asdf link!";
		Boolean isNew = true;
		List<Post> newList = new ArrayList<Post>();
		newList.add(newPost);
		
		then:
		newUser.notifyUser(newList);
    }
}
