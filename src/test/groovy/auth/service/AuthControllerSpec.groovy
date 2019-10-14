package auth.service

import auth.service.model.LoginRequest
import auth.service.model.RegisterRequest
import auth.service.model.RegisterResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Shared

import javax.inject.Inject

@MicronautTest
class AuthControllerSpec extends Specification {

    @Shared @Inject
    EmbeddedServer embeddedServer

    @Shared @AutoCleanup @Inject @Client("/")
    RxHttpClient client

    void "test index"() {
        given:
        HttpResponse response = client.toBlocking().exchange("/auth")

        expect:
        response.status == HttpStatus.OK
    }

    void "test register"(){
        when:
        def registerRequest= HttpRequest.POST("/auth/register", new RegisterRequest("test@test.com","test123","dGVzdDEyMw=="))
        HttpResponse response=client.toBlocking().exchange(registerRequest)
        then:
        response.status==HttpStatus.OK
        response.getBody(RegisterResponse.class).get().code=="OK"

        when:
        def loginRequest=HttpRequest.POST("/auth/login", new LoginRequest("test@test.com","test123",0))
        HttpResponse loginResponse=client.toBlocking().exchange(loginRequest)

        then:
        loginResponse.status==HttpStatus.OK

        when:
        HttpResponse response1=client.toBlocking().exchange(registerRequest)

        then:
        response1.status==HttpStatus.OK
        response1.getBody(RegisterResponse.class).get().code=="ALREADY_EXISTS"
    }
}
