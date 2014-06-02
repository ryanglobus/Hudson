package hudson.neighborhood

class City {

    String name
    String value // in URL path on Craigslist

    static belongsTo = [region: Region]
    static hasMany = [neighborhoods: Neighborhood]

    static constraints = {
        name blank: false
        value blank: false
        // TODO make (region, city.name) and (region, city.value) pair unique
    }

    static mapping = {
        neighborhoods sort: 'name'
    }
}
