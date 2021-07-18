package covidtracker

import CovidTracker.CovidResultStatus
import CovidTracker.UserType
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    def registerUser() {
        String name = params.name
        String phoneNumber = params.phoneNumber
        String pinCode = params.pinCode

        Map response = [:]
        Boolean exceptionOccurred = false
        Boolean isExist = false

        if (User.findByPhoneNumber(phoneNumber)) {
            isExist = true
            response.put("error", "User with that phone number already exist")
        }

        if (!isExist) {
            User user = new User(name: name, phoneNumber: phoneNumber, pinCode: pinCode, type: UserType.PATIENT)
            try {
                user.save(failOnError: true)
            }
            catch (Exception ex) {
                exceptionOccurred = true
                println(" ex.message " + ex.message)
            }
            if (!exceptionOccurred)
                response.put("userId", user.id)
            else
                response.put("error", "User could not be created")
        }
        render response as JSON
    }

    def registerAdmin() {

        String name = params.name
        String phoneNumber = params.phoneNumber
        String pinCode = params.pinCode

        Map response = [:]
        Boolean exceptionOccurred = false
        Boolean isExist = false

        if (User.findByPhoneNumber(phoneNumber)) {
            isExist = true
            response.put("error", "Admin with that phone number already exist")
        }

        if (!isExist) {
            User user = new User(name: name, phoneNumber: phoneNumber, pinCode: pinCode, type: UserType.ADMIN)
            try {
                user.save(failOnError: true)
            }
            catch (Exception ex) {
                exceptionOccurred = true
                println(" ex.message " + ex.message)
            }
            if (!exceptionOccurred)
                response.put("adminId", user.id)
            else
                response.put("error", "Admin could not be created")
        }
        render response as JSON
    }

    @Transactional
    def updateCovidResult() {
        Long userId = params.userId as Long
        Long adminId = params.adminId as Long
        String result = params.result

        User patient = User.findByIdAndType(userId, UserType.PATIENT)
        User admin = User.findByIdAndType(adminId, UserType.ADMIN)
        Map response = [:]
        Boolean exceptionOccurred = false

        if (patient && admin) {
            try {
                CovidResultStatus resultStatus = CovidResultStatus.valueOf(result?.toUpperCase())
                println(" resultStatus " + resultStatus)
                CovidResult covidResult = CovidResult.findByPatient(patient) ?: new CovidResult(patient: patient, admin: admin)
                covidResult.result = resultStatus
                covidResult.save(failOnError: true, flush: true)
            }
            catch (Exception ex) {
                exceptionOccurred = true
                println(" ex.message " + ex.message)
            }
            if (!exceptionOccurred)
                response.put("updated", true)
            else
                response.put("error", "Covid result could not be updated")
        } else
            response.put("error", "Patient or admin does not exist")
        render response as JSON
    }

}
