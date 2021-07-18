package covidtracker

import CovidTracker.CovidResultStatus

class CovidResult {

    User patient
    User admin
    CovidResultStatus result
    Date dateCreated
    Date lastUpdated
    String uuid = UUID.randomUUID()

    static constraints = {
        patient nullable: false
        admin nullable: false
        result nullable: false
    }
}
