package covidtracker

import CovidTracker.CovidResultStatus
import CovidTracker.UserType
import grails.converters.JSON
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    def registerUser() {
        String name = params.name
        String phoneNumber = params.phoneNumber
        String pinCode = params.pinCode

        Map<String, Long> response = [:]

        User user = new User(name: name, phoneNumber: phoneNumber, pinCode: pinCode, type: UserType.PATIENT)
        user.save(failOnError: true)
        response.put("userId", user.id)
        render response as JSON
    }

    def registerAdmin() {

        String name = params.name
        String phoneNumber = params.phoneNumber
        String pinCode = params.pinCode

        Map<String, Long> response = [:]

        User user = new User(name: name, phoneNumber: phoneNumber, pinCode: pinCode, type: UserType.ADMIN)
        user.save(failOnError: true)
        response.put("adminId", user.id)
        render response as JSON

    }

    def updateCovidResult() {
        Long userId = params.userId as Long
        Long adminId = params.adminId as Long
        String result = params.result

        User patient = User.findByIdAndType(userId, UserType.PATIENT)
        User admin = User.findByIdAndType(adminId, UserType.ADMIN)

        if (patient && admin) {
            CovidResult covidResult = new CovidResult(patient: patient, admin: admin, result: CovidResultStatus.valueOf(result.toUpperCase()))
            covidResult.save()
            Map response = ["updated": true]
            render response as JSON
        }


    }

}
