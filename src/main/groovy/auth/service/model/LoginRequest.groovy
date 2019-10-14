package auth.service.model

class LoginRequest {
    String email
    String password
    long ttl

    LoginRequest(){

    }

    LoginRequest(String email, String password, long ttl) {
        this.email = email
        this.password = password
        this.ttl = ttl
    }
}
