package hudson.neighborhood

class Neighborhood {

    String name
    Integer value

    static belongsTo = [city: City]

    static constraints = {
        name blank: false
        value min: 0
        // TODO make (city, neighborhood.name) and (city, neighborhood.value) unique
    }
}
