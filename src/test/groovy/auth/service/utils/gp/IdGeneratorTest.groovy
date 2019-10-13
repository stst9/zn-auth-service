package auth.service.utils.gp

import spock.lang.Specification

class IdGeneratorTest extends Specification {
    def "should be of length 40"() {
        expect:
            IdGenerator.generateId().length()==40
    }

    def "should never be null"(){
        expect:
            IdGenerator.generateId()!=null
    }
}
