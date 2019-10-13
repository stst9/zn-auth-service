package auth.service.model

import spock.lang.Specification

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey

class BaseMessageTest extends Specification {
    def "Signature should be validated"() {
        given:
        def test = new BaseMessage()
        KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        def msg = "test msg 123 456 789"
        when:
        test.sign(keys.private, msg.bytes)
        then:
        test.verify(keys.public, msg.bytes)
    }

    def "Signature should not be validated change in msg"() {
        given:
        def test = new BaseMessage()
        KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        def msg = "test msg 123 456 789"
        when:
        test.sign(keys.private, msg.bytes)
        then:
        !test.verify(keys.public, (msg + "1").bytes)
    }
    def "Signature should not be validated keys do not match"() {
        given:
        def test = new BaseMessage()
        KeyPair keys = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        KeyPair keys2 = KeyPairGenerator.getInstance("RSA").generateKeyPair()
        def msg = "test msg 123 456 789"
        when:
        test.sign(keys.private, msg.bytes)
        then:
        !test.verify(keys2.public, msg.bytes)
    }
}
