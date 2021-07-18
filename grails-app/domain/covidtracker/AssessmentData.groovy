package covidtracker

class AssessmentData {

    User user
    Boolean travelHistory = false
    Boolean contactWithCovidPatient = false
    String symptoms
    Integer riskPercentage = 5

    static constraints = {
        user nullable: false
        travelHistory nullable: false
        contactWithCovidPatient nullable: false
        riskPercentage nullable: false
        symptoms nullable: true
    }
}
