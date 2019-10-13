package auth.service.model

import auth.service.utils.gp.ByteUtils

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
        sign(key, ByteUtils.concat(email.bytes,id.bytes,this.key.bytes,ttl.toString().bytes,timeStamp.toString().bytes))
    }
}
