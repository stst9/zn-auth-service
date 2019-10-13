package auth.service.utils.gp

import spock.lang.Specification

class EncryptedValueTest extends Specification {

    void "test"(){
        given:
            EncryptedValue encryptedValue=new EncryptedValue(1 as short,new byte[16],"test 123".bytes)
        expect:
            new EncryptedValue(encryptedValue.toByteArray()).type==1 as short
    }
}
