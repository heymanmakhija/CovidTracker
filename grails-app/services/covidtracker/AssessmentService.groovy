package covidtracker

import grails.gorm.transactions.Transactional

@Transactional
class AssessmentService {

    Integer calculateRiskScore(AssessmentData assessmentData) {

        List<String> symptomsList = assessmentData.symptoms ? assessmentData.symptoms.split(',') : null

        List<String> actualSymptomsList = ["cough", "cold", "fever"]

        if (!symptomsList.contains(actualSymptomsList) && !assessmentData.travelHistory && !assessmentData.contactWithCovidPatient)
            return 5
        else if (symptomsList.containsAll(actualSymptomsList) && assessmentData.travelHistory && assessmentData.contactWithCovidPatient)
            return 95
        else if ((symptomsList.contains(["cough"]) || symptomsList.contains(["cold"]) || symptomsList.contains(["fever"])) &&
                (assessmentData.travelHistory || assessmentData.contactWithCovidPatient))
            return 50
        else
            75
    }


}
