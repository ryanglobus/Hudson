import hudson.*
import hudson.queue.*
import HudsonJobs.*
import hudson.neighborhood.*


class BootStrap {

    def init = { servletContext ->
        
        Region.initializeData()

        environments {
            development {
                /* insert your own email address and phone number to easily
                   create an account and query: %*/
                /*
                User ryan = new User(
                    email: '',
                    firstName: 'Ryan',
                    lastName: 'Globus',
                    notifyFrequency: 30,
                    // password is 'ryan'
                    passwordHash: '666CE900852DCC736A79ED9A3EDFC9FAD84D2A7AF11B5BC771C61D786CE59D6E',
                    phone: '',
                    salt: '[-100, -12, -74, -93, 21, -119, 27, 55, -1, -110, 121, 58, -25, -64, -44, 29, 125, 42, -55, 46, -75, -76, 92, 86, -118, -38, 0, -120, -5, -65, -35, 85]'
                )
                ryan.save(failOnError: true)
                NotifyJob.schedule(60_000, -1, [user: ryan])
                Query q1 = new Query(
                    user: ryan,
                    name: 'bootstrap query q1',
                    minRent: 300,
                    maxRent: 30000,
                    numBedrooms: 1,
                    notify: true,
                    instantReply: false
                )
                q1.save(failOnError: true)
                Message<Query> m = new Message<Query>(q1)
                m.delay = 10
                Query.queue.enqueue(m)
                */
            }
        }
    }
    def destroy = {
    }
}
