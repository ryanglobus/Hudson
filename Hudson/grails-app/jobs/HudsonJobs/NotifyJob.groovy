package HudsonJobs
import hudson.User


class NotifyJob {
    static triggers = {} //Sey when user starts

    def execute(context) {
        // execute job
		User user = context.mergedJobDataMap.get('user');
		user.notifyUser();
    }
}
