package HudsonJobs
import hudson.Post
import hudson.Query


class CrawlJob {
    static triggers = {} //This is defined when the job instance is created in controller.

    def execute(context) {
		Query query = context.mergedJobDataMap.get('query');
	 
		//Get new posts!   
		if (query.isCancelled == false) {
			List<Post> posts = query.searchCraigslist();
			//Filter out which ones are actually new and saves them to DB.
			query.saveNewPosts(posts);
		}
    }
}
