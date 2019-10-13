package auth.service.controller

import auth.service.beans.CryptUtilsBean
import auth.service.beans.RsaKeyStore
import auth.service.domain.AuthKey
import auth.service.domain.AuthPassword
import auth.service.domain.AuthUser
import auth.service.model.LoginRequest
import auth.service.model.LoginResponse
import auth.service.model.RegisterRequest
import auth.service.model.RegisterResponse
import auth.service.repository.AuthUserRepository
import auth.service.utils.gp.EncryptedValue
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import at.favre.lib.crypto.bcrypt.BCrypt
import org.apache.commons.codec.binary.Base64

import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext
import java.security.SecureRandom

@Controller("/auth")
class AuthController {

    @Inject
    private AuthUserRepository authUserRepository
    @Inject
    private RsaKeyStore rsaKeyStore
    @Inject
    private CryptUtilsBean cryptUtilsBean
    @PersistenceContext
    @CurrentSession
    private EntityManager entityManager


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
            String key=Base64.encodeBase64String(cryptUtilsBean.decrypt(new EncryptedValue(user.authKeys[0].value)))
            LoginResponse respUser=new LoginResponse(
                    user.id,
                    user.email,
                    key,
                    loginRequest.ttl)
            respUser.sign(rsaKeyStore.privateKey)
            return HttpResponse.ok(respUser)
        }else {
            return HttpResponse.notFound()
        }
    }

    @Post("/register")
    HttpResponse<RegisterResponse> register(RegisterRequest request){
        entityManager.transaction.begin()
        AuthUser authUser=new AuthUser()
        authUser.email=request.email
        AuthPassword password=new AuthPassword()
        password.setValue(BCrypt.withDefaults().hashToString(13,request.password.toCharArray()))
        authUser.password=password

        byte [] salt=new byte[16]
        SecureRandom secureRandom=new SecureRandom()
        secureRandom.nextBytes(salt)
        byte [] encKey=cryptUtilsBean.hash(
                1 as short,
                request.password,
                salt,
                cryptUtilsBean.getIterationByType(1 as short),
                cryptUtilsBean.getKeyLenByType(1 as short)
        )
        EncryptedValue encMasterKey=cryptUtilsBean.encrypt(
                1 as short,
                Base64.decodeBase64(request.key),
                encKey,
                salt
        )
        AuthKey key=new AuthKey()
        key.value=encMasterKey.toByteArray()
        authUser.authKeys.add(key)

        entityManager.persist(authUser)
        return HttpResponse.accepted()
    }

}