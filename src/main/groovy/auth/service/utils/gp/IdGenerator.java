package auth.service.utils.gp;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Random;
import java.util.UUID;

public class IdGenerator {

    public static String generateId(){
        return Base64.encodeBase64String(DigestUtils.sha3_224(
                UUID.randomUUID().toString()+
                System.currentTimeMillis()+
                new Random().nextDouble()));
    }
}
