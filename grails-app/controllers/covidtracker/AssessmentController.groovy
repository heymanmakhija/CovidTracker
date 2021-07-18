package covidtracker

import CovidTracker.CovidResultStatus
import CovidTracker.UserType
import CovidTracker.ZoneType
import grails.converters.JSON
import org.springframework.web.bind.annotation.RestController

@RestController
class AssessmentController {

    AssessmentService assessmentService

    def selfAssessment() {

        Long userId = params.userId as Long
        String symptoms = params.symptoms
        String travelHistory = params.travelHistory
        String contactWithCovidPatient = params.contactWithCovidPatient
        Map response = [:]
        Boolean isException = false
        Boolean errorOccured = false
        Integer riskPercentage = null

        User user = User.findByIdAndType(userId, UserType.PATIENT)
        if (user) {
            AssessmentData assessmentData = new AssessmentData(user: user, symptoms: symptoms, travelHistory: travelHistory, contactWithCovidPatient: contactWithCovidPatient)
            riskPercentage = assessmentService.calculateRiskScore(assessmentData)
            if (!riskPercentage) {
                errorOccured = true
                response = ["error": "risk percentage could not be calculated, case not found"]
            }
            if (!errorOccured) {
                try {
                    assessmentData.riskPercentage = riskPercentage
                    assessmentData.save(failOnError: true)
                }
                catch (Exception ex) {
                    isException = true
                    println(" ex.message " + ex.message)
                }
                if (!isException)
                    response = ["riskPercentage": riskPercentage + "%"]
                else
                    response = ["error": "assessment data could not be stored"]
            }
        } else
            response = ["error": "User does not exists"]
        render response as JSON
    }

    def getZoneInfo() {

        String pinCode = params.pinCode
        List<User> patients = User.findAllByTypeAndPinCode(UserType.PATIENT, pinCode)
        long numCases = CovidResult.findAllByPatientInListAndResult(patients, CovidResultStatus.POSITIVE).size()
        String zoneType = ZoneType.GREEN.toString()
        if (numCases < 5)
            zoneType = ZoneType.ORANGE.toString()
        else if (numCases >= 5)
            zoneType = ZoneType.RED.toString()
        Map response = [:]
        if (patients) {
            response.put("numCases", numCases)
            response.put("zoneType", zoneType)
        } else
            response.put("error", "Could not find any patient for the specified pin code")
        render response as JSON
    }

}
