package auth.service.utils.gp

import javax.crypto.Cipher

class AesUtils {

    private Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
}
