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

})

jQuery(function() {
    var $ = jQuery

    var neighborhood = $('#neighborhoods')
    var nonSelectedText = 'All Neighborhoods'
    var options = {
        nonSelectedText: nonSelectedText,
        maxHeight: 400,
        numberDisplayed: 1
    }
    neighborhood.multiselect(options)

    $('#city').change(function() {
        var cityVal = $('#city option:selected').val()
        neighborhood.multiselect('dataprovider', [])
        neighborhood.multiselect('disable')
        if (cityVal != null && cityVal != '') {
            // TODO add delay to "loading" to prevent flashing effect?
            options.nonSelectedText = 'Loading...'
            neighborhood.multiselect('setOptions', options)
            neighborhood.multiselect('rebuild')
            $.ajax({
                url: 'getNeighborhoods',
                data: {
                    city: cityVal
                },
                dataType: 'json',
                type: 'GET',
                success: function(data) {
                    var dataProvider = []
                    for (var i in data) {
                        var nh = data[i]
                        dataProvider.push({label: nh.name, value: nh.value})
                    }
                    neighborhood.multiselect('dataprovider', dataProvider)
                    neighborhood.multiselect('enable')
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(errorThrown)
                    neighborhood.multiselect('dataprovider', [])
                    neighborhood.multiselect('disable')
                    alert('There was an error loading the neighborhoods. Reload the page or try again later.')
                },
                complete: function(jqXHR, textStatus) {
                    options.nonSelectedText = nonSelectedText
                    neighborhood.multiselect('setOptions', options)
                    neighborhood.multiselect('rebuild')
                }
            })
        }
    })
})
