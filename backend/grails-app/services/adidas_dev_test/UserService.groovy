package adidas_dev_test

import grails.transaction.Transactional


@Transactional
class UserService {

    def collectInformation(email, firstName, lastName, dateOfBirth, country) {
        try{
            def user = new User(email:email, firstName:firstName, lastName:lastName, dateOfBirth:dateOfBirth, country:country);
            user.save(flush:true);
            return true
        }catch(ex){
            log.error(ex)
            return false
        }
    }

    def validateFields(email, firstName, lastName, dateOfBirth, country) {
        //TODO field validations
        return true
    }
}
