package auth.service.repository

import auth.service.domain.AuthUser
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional

import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Singleton
class AuthUserRepository {

    @PersistenceContext
    @CurrentSession
    private EntityManager entityManager

    @Transactional
    @Deprecated
    AuthUser findByEmailAndPassword(String email, String password){
        entityManager.createQuery("select u from AuthUser u where u.email=:email and u.password=:password",AuthUser)
        .setParameter("email",email)
        .setParameter("password",password)
        .singleResult
    }

    @Transactional
    AuthUser findByEmail(String email){
        entityManager.createQuery("select u from AuthUser u where u.email=:email",AuthUser)
                .setParameter("email",email)
                .singleResult
    }
}