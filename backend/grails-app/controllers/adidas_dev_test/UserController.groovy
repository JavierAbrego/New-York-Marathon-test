package adidas_dev_test
import grails.converters.JSON

class UserController {
    def userService

    def save() {
        def email = params.email,
        firstName = params.firstName,
        lastName = params.lastName,
        dateOfBirth = params.dateOfBirth,
        country = params.country

        def httpCode = 200
        if(userService.validateFields(email, firstName, lastName, dateOfBirth, country)) {
            if(!userService.collectInformation(email, firstName, lastName, dateOfBirth, country)){
                httpCode = 500
            }
        }else{
             httpCode = 400
        }
        render status:httpCode

    }

    def index(){
        def users = User.findAll()
        render users as JSON
    }

    def fields(){
        render grailsApplication.config.adidas.user.fields as JSON;

    }

}
