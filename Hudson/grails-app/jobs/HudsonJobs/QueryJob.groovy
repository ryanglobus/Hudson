package HudsonJobs



class QueryJob {
	
    static triggers = {} //The value for the trigger is set dynamically once created by the user.

    def execute(context) {
		Query query = context.mergedJobDataMap.get('query');
        
		//Get new posts!
		<List> posts = query.searchCraigslist();
		
		//Filter out which ones are actually new and saves them to the DB.
		query.savePosts(posts);
    }
}
