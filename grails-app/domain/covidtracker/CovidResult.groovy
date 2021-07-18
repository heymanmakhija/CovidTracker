package covidtracker

import CovidTracker.CovidResultStatus

class CovidResult {

    User patient
    User admin
    CovidResultStatus result

    static constraints = {
        patient nullable: false
        admin nullable: false
        result nullable: false
    }
}
