jQuery(function() {
    var $ = jQuery

    var queryFormTemplate = $('#query-tabs #search-template')
    // can't use hide() and show() but instead must edit css/display
    // since hide() and show() would override .active class and
    // tab('show') function
    queryFormTemplate.css('display', 'none')
    var nextSearchId = 1

    var newTabButton = $('#query-tabs #new-tab')
    newTabButton.click(function(e) {
        e.preventDefault()
        $('#query-tabs .tab-content .active').removeClass('active')
        var newForm = queryFormTemplate.clone()
        newForm.css('display', '') // base display off of .active class
        var searchIdNum = (nextSearchId++)
        newForm.attr('id', 'search' + searchIdNum)
        $('#query-tabs .tab-content').append(newForm)

        var newTab = $('<li><a href="#search' + searchIdNum +
            '" data-toggle="tab">Search ' + searchIdNum + '</a></li>')
        newTab.insertBefore($('#new-tab').parent())
        newTab.find('a').tab('show')
    })
    newTabButton.click() // trigger click to create first form
    
    //Only show textbox if the user clicks that they would like to auto-respond!
    $('#instantReply').change(function(){
    	if(this.checked){
    		$("#responseBox").show();
    	}
    	else
    		$("#responseBox").hide();
    })
    
})

