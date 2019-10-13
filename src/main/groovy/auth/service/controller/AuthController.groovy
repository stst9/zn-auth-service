package auth.service.controller

import auth.service.beans.RsaKeyStore
import auth.service.domain.AuthUser
import auth.service.model.LoginRequest
import auth.service.model.LoginResponse
import auth.service.model.RegisterRequest
import auth.service.model.RegisterResponse
import auth.service.repository.AuthUserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import at.favre.lib.crypto.bcrypt.BCrypt

import javax.inject.Inject
import javax.persistence.NoResultException

@Controller("/auth")
class AuthController {

    @Inject
    private AuthUserRepository authUserRepository
    @Inject
    private RsaKeyStore rsaKeyStore
    @Get("/")
    HttpStatus index() {
        return HttpStatus.OK
    }

    @Post("/login")
    HttpResponse<LoginResponse> login(LoginRequest loginRequest){
        AuthUser user
        try {
            user=authUserRepository.findByEmail(loginRequest.email)
        }catch(NoResultException e){

        }
        if (user&&BCrypt.verifyer().verify(loginRequest.password.chars,user.password.value).verified){
            LoginResponse respUser=new LoginResponse(user.id,user.email,user.authKeys[0].valueBase64,loginRequest.ttl)
            respUser.sign(rsaKeyStore.privateKey)
            return HttpResponse.ok(respUser)
        }else {
            return HttpResponse.notFound()
        }
    }

    @Post("/register")
    HttpResponse<RegisterResponse> register(RegisterRequest request){
        AuthUser user=new AuthUser()
        user
    }

}