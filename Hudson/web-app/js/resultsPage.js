jQuery(function() {
    var $ = jQuery;
    
    //If we click on a "favorite" button we mark it as favorited and change the 
    //Button!
    $("body").on("click", ".response", function(e){
    	e.preventDefault();
    	var id = this.id;
    	var post = this;

    	$.getJSON("/Hudson/profile/markAsResponded", {postId:id}, function(data){    		
    		//Now let's switch the button styles!  		
    		if(data.responseSent) {
    			$(post).replaceWith("<button class=\"btn btn-success btn-small response\" id=\"" + data.id + "\"><span class=\"glyphicon glyphicon-ok\"></span> Yes</button>");
    		}
    		else{
    			$(post).replaceWith("<button class='btn btn-danger btn-small response' id='" + data.id + "'><span class='glyphicon glyphicon-remove'></span> No</button>");
    		}
    	});
    });
    
    
    
    
    //If we click on the "responded" button, we change the button and
    //toggle the hasResponded variable!
    $("body").on("click", ".favorite", function(e){
    	e.preventDefault();
    	var id = this.id;
    	var post = this;

    	$.getJSON("/Hudson/profile/markAsFavorite", {postId:id}, function(data){    		
    		//Now let's switch the button styles!  		
    		if(data.favorite) {
    			$(post).replaceWith("<button class='btn btn-custom btn-small favorite' id='" + data.id + "'><span class='glyphicon glyphicon-star'></span></button>");
    		}
    		else{
    			$(post).replaceWith("<button class='btn btn-default btn-small favorite' id='" + data.id + "'><span class='glyphicon glyphicon-star-empty'></span></button>");
    		}
    	});
    });
    
});