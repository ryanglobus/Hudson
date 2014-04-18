package hudson

class User {

    String email
    String phone
    String passwordHash
    String salt
    String firstName
    String lastName
    Integer notifyFrequency

    static hashMany = [queries: Query]

    static constraints = {
        '*'(blank: false)
        email email: true
        phone nullable: true
    }

    void notifyUser() {
        // TODO Kelly
    }


}
