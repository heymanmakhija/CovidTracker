package covidtracker

import CovidTracker.UserType

class User {

    String name
    String phoneNumber
    String pinCode
    String uuid = UUID.randomUUID()
    Date dateCreated
    Date lastUpdated
    UserType type

    static constraints = {
        name nullable: false
        phoneNumber nullable: false
        pinCode nullable: false
        type nullable: false
    }
}
