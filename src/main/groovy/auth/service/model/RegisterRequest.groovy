package auth.service.model

class RegisterRequest {
    String email
    String password
    String key

    RegisterRequest(String email, String password, String key) {
        this.email = email
        this.password = password
        this.key = key
    }

    RegisterRequest(){

    }
}
