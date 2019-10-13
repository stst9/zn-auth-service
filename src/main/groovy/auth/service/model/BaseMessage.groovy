package auth.service.model

import org.apache.commons.codec.binary.Base64

import java.nio.charset.Charset
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature

class BaseMessage {
    String signature
    long timeStamp=System.currentTimeMillis()

    void sign(PrivateKey privateKey, byte [] data){
        Signature privSignature=Signature.getInstance("SHA512withRSA")
        privSignature.initSign(privateKey)
        privSignature.update(data)
        signature= Base64.encodeBase64String(privSignature.sign())
    }

    boolean verify(PublicKey publicKey, byte [] data){
        Signature pubSignature=Signature.getInstance("SHA512withRSA")
        pubSignature.initVerify(publicKey)
        pubSignature.update(data)
        pubSignature.verify(Base64.decodeBase64(signature as String))
    }
}
