package HudsonJobs



class NotifyJob {
    static triggers = {} //Set dynamically when the user inputs query info.

    def execute(context) {
        // execute job
		User user = context.mergedJobDataMap.get('user');
		user.notifyUser();
    }
}
