jQuery(function() {
    var $ = jQuery

    var queryFormTemplate = $('#query-tabs #search1').clone()
    var nextSearchId = 2

    $('#query-tabs #new-tab').click(function(e) {
        e.preventDefault()
        $('#query-tabs .tab-content .active').removeClass('active')
        var newForm = queryFormTemplate.clone()
        var searchIdNum = (nextSearchId++)
        newForm.attr('id', 'search' + searchIdNum)
        $('#query-tabs .tab-content').append(newForm)

        var newTab = $('<li><a href="#search' + searchIdNum +
            '" data-toggle="tab">Search ' + searchIdNum + '</a></li>')
        newTab.insertBefore($('#new-tab').parent())
        newTab.find('a').tab('show')
    })
})