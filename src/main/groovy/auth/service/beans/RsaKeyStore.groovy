package auth.service.beans

import io.micronaut.context.annotation.Parameter
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Value
import org.apache.commons.io.FileUtils

import javax.inject.Singleton
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

@Singleton
class RsaKeyStore {
    //@Value('${micronaut.application.key:private_key.der}')
    String privateKeyPath="./private_key.der"

    PrivateKey privateKey=loadPrivateKey()

    PrivateKey loadPrivateKey(){
        println(privateKeyPath)
        println(new File("./").absolutePath)
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(FileUtils.readFileToByteArray(new File(privateKeyPath)));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

}
