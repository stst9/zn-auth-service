package auth.service.repository

import auth.service.domain.AuthUser
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional

import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import javax.persistence.PersistenceContext

@Singleton
class AuthUserRepositoryImpl implements AuthUserRepositorySpec {

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
        try {
            return entityManager.createQuery("select u from AuthUser u where u.email=:email",AuthUser)
                    .setParameter("email",email)
                    .singleResult
        }catch(NoResultException e){
            return null
        }
    }

    @Transactional
    void persist(AuthUser authUser){
        entityManager.persist(authUser)
    }

    @Transactional
    AuthUser save(AuthUser authUser){
        entityManager.merge(authUser)
    }

    @Override
    boolean existsByEmail(String email) {
        return findByEmail(email)!=null
    }
}