package auth.service.repository

import auth.service.domain.AuthUser

interface AuthUserRepositorySpec {
    AuthUser findByEmail(String email)
    void persist(AuthUser authUser)
    AuthUser save(AuthUser authUser)
    boolean existsByEmail(String email)
}