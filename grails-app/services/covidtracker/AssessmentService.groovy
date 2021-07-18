package covidtracker

import CovidTracker.Symptoms
import grails.gorm.transactions.Transactional

@Transactional
class AssessmentService {

    Integer calculateRiskScore(AssessmentData assessmentData) {

        List<String> symptomsList = assessmentData.symptoms ? assessmentData.symptoms.trim().split(',') : null

        List<String> actualSymptomsList = [Symptoms.cough.toString(), Symptoms.cold.toString(), Symptoms.fever.toString()]

        Integer matches = matches(actualSymptomsList, symptomsList)

        if ((symptomsList == null || matches == 0) && !assessmentData.travelHistory && !assessmentData.contactWithCovidPatient) {
            return 5
        } else if (matches == 1 && (assessmentData.travelHistory || assessmentData.contactWithCovidPatient)) {
            return 50
        } else if (matches == 2 && (assessmentData.travelHistory || assessmentData.contactWithCovidPatient)) {
            return 75
        } else if (matches == 3 && assessmentData.travelHistory && assessmentData.contactWithCovidPatient) {
            return 95
        }

    }

    Integer matches(List<String> source, List<String> target) {
        Integer count = 0
        target.each {
            it in source
            count++
        }
        println " match count " + count
        return count
    }


}
