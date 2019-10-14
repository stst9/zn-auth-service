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
import auth.service.repository.AuthUserRepositoryImpl
import auth.service.utils.gp.EncryptedValue
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import at.favre.lib.crypto.bcrypt.BCrypt
import io.micronaut.spring.tx.annotation.Transactional
import org.apache.commons.codec.binary.Base64

import javax.inject.Inject
import javax.persistence.NoResultException
import java.security.SecureRandom
import java.util.logging.Level
import java.util.logging.Logger

@Controller("/auth")
class AuthController {

    @Inject
    private AuthUserRepositoryImpl authUserRepository
    @Inject
    private RsaKeyStore rsaKeyStore
    @Inject
    private CryptUtilsBean cryptUtilsBean
    /*@PersistenceContext
    @CurrentSession
    private EntityManager entityManager*/

    static Logger logger = Logger.getLogger(AuthController.class.getName())


    @Get("/")
    HttpStatus index() {
        return HttpStatus.OK
    }

    @Post("/login")
    HttpResponse<LoginResponse> login(LoginRequest loginRequest) {
        try {
            AuthUser user
            try {
                user = authUserRepository.findByEmail(loginRequest.email)
            } catch (NoResultException e) {

            }
            if (user && BCrypt.verifyer().verify(loginRequest.password.chars, user.password.value).verified) {
                EncryptedValue value = new EncryptedValue(user.authKeys[0].value)
                byte[] encKey = getEncryptionKey(loginRequest.password, value.salt)
                String key = Base64.encodeBase64String(cryptUtilsBean.decrypt(value, encKey))
                LoginResponse respUser = new LoginResponse(
                        user.id,
                        user.email,
                        key,
                        loginRequest.ttl)
                respUser.sign(rsaKeyStore.privateKey)
                return HttpResponse.ok(respUser)
            } else {
                return HttpResponse.notFound()
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "${e.class}: ${e.message}")
        }
        return HttpResponse.serverError()
    }

    @Post("/register")
    HttpResponse<RegisterResponse> register(RegisterRequest request) {
        try {
            if (authUserRepository.existsByEmail(request.email)) {
                return HttpResponse.ok(new RegisterResponse("ALREADY_EXISTS"))
            }
            AuthUser authUser = new AuthUser()
            authUser.email = request.email
            AuthPassword password = new AuthPassword()
            password.setValue(BCrypt.withDefaults().hashToString(13, request.password.toCharArray()))
            authUser.password = password

            byte[] salt = new byte[16]
            SecureRandom secureRandom = new SecureRandom()
            secureRandom.nextBytes(salt)
            byte[] encKey = getEncryptionKey(request.password, salt)
            EncryptedValue encMasterKey = cryptUtilsBean.encrypt(
                    1 as short,
                    Base64.decodeBase64(request.key),
                    encKey,
                    salt
            )
            AuthKey key = new AuthKey()
            key.value = encMasterKey.toByteArray()
            authUser.addAuthKey(key)
            authUserRepository.persist(authUser)
            return HttpResponse.ok(new RegisterResponse("OK"))
        } catch (Exception e) {
            e.printStackTrace()
            logger.log(Level.WARNING, "${e.class}: ${e.message}")
        }
        return HttpResponse.serverError()
    }

    byte[] getEncryptionKey(String password, byte[] salt) {
        cryptUtilsBean.hash(
                1 as short,
                password,
                salt,
                cryptUtilsBean.getIterationByType(1 as short),
                cryptUtilsBean.getKeyLenByType(1 as short)
        )
    }

}