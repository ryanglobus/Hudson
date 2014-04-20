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
        email email: true, blank: false
        phone nullable: true, blank: false
        passwordHash blank: false
        salt blank: false
        firstName blank: false
        lastName blank: false
        notifyFrequency blank: false
    }

    void notifyUser() {
        // TODO Kelly
    }


}
