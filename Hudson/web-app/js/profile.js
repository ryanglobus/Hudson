// outdated tab code
jQuery(function() {
    var $ = jQuery

    //Only show textbox if the user clicks that they would like to auto-respond!
    $('.query-form').each(function() {
        var form = $(this)
        form.find('input[name=instantReply]').change(function() {
            var textarea = form.find('textarea[name=responseMessage]')
            if (this.checked) {
                textarea.show()
            } else {
                textarea.hide()
            }
        })
    })
    
})

jQuery(function() {
    var $ = jQuery
    $('.query-form').each(function() {
        var form = $(this)
        var neighborhood = form.find('select[name=neighborhoods]')
        var nonSelectedText = 'All Neighborhoods'
        var options = {
            nonSelectedText: nonSelectedText,
            maxHeight: 400,
            numberDisplayed: 1
        }
        neighborhood.multiselect(options)

        var city = $(this).find('select[name=city]')
        city.change(function() {
            var cityVal = $(this).find('option:selected').val()
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
                        var queryId = form.attr('data-query-id')
                        if (initialNeighborhoods && initialNeighborhoods[queryId]) {
                            neighborhood.multiselect('select', initialNeighborhoods[queryId])
                            initialNeighborhoods[queryId] = false
                        }
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
        city.change()
    })

})
