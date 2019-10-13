package auth.service.model

import java.security.PrivateKey

class LoginResponse extends BaseMessage {
    String email
    String id
    String key
    long ttl

    LoginResponse(String email, String id, String key, long ttl = -1) {
        this.email = email
        this.id = id
        this.ttl = ttl
        this.key = key
    }

    void sign(PrivateKey key) {
        def data = [] as byte[]
        data << email.bytes
        data << id.bytes
        data << this.key.bytes
        data << ttl.toString().bytes
        data << timeStamp.toString().bytes
        sign(key, data)
    }
}
