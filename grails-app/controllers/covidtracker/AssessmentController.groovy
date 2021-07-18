package covidtracker

import CovidTracker.CovidResultStatus
import CovidTracker.UserType
import grails.converters.JSON
import org.springframework.web.bind.annotation.RestController

@RestController
class AssessmentController {

    AssessmentService assessmentService

    def selfAssessment() {

        Long userId = params.userId as Long
        String symptoms = params.symptoms
        Boolean travelHistory = params.travelHistory as Boolean
        Boolean contactWithCovidPatient = params.contactWithCovidPatient as Boolean

        User user = User.findByIdAndType(userId, UserType.PATIENT)
        if (user) {
            AssessmentData assessmentData = new AssessmentData(user: user, symptoms: symptoms, travelHistory: travelHistory, contactWithCovidPatient: contactWithCovidPatient)
            Integer riskPercentage = assessmentService.calculateRiskScore(assessmentData)
            assessmentData.riskPercentage = riskPercentage
            assessmentData.save(failOnError: true)
            Map response = ["riskPercentage": riskPercentage + "%"]
            render response as JSON
        }
    }

    def getZoneInfo() {

        String pinCode = params.pinCode
        List<User> patients = User.findAllByTypeAndPinCode(UserType.PATIENT, pinCode)
        long numCases = CovidResult.findAllByPatientInListAndResult(patients, CovidResultStatus.POSITIVE).size()
        String zoneType = "GREEN"
        if (numCases < 5)
            zoneType = "ORANGE"
        else if (numCases >= 5)
            zoneType = "RED"
        Map response = [:]
        response.put("numCases", numCases)
        response.put("zoneType", zoneType)
        render response as JSON
    }

}
