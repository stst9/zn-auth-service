package auth.service.model

class RegisterResponse {
    /**
     * Can be:
     * ALREADY_EXISTS
     * OK
     */
    String code

    RegisterResponse(String code) {
        this.code = code
    }
}
