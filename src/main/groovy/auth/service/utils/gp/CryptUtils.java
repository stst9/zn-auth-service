package auth.service.utils.gp;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

public class CryptUtils {
    private SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    private HashMap<Short, Cipher> cipherHashMap = new HashMap<>();
    private HashMap<Short, EncryptionSpec> encryptionImpl = new HashMap<>();
    private HashMap<Short, DecryptionSpec> decryptionImpl = new HashMap<>();


    public CryptUtils() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cipherHashMap.put((short) 1, Cipher.getInstance("AES/CBC/PKCS5Padding"));

        encryptionImpl.put((short) 1, (cipher, input, key, salt) -> { // AES/CBC/PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            return new EncryptedValue((short) 1, cipher.getIV(), cipher.doFinal(input));
        });

        decryptionImpl.put((short) 1, (cipher, value, key) -> {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),new IvParameterSpec(value.getSalt()));
            return cipher.doFinal(value.getValue());
        });
    }

    public byte[] hashPBKDF2(char[] password, byte[] salt, int iterations, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        return secretKey.getEncoded();
    }


    public static interface EncryptionSpec {
        EncryptedValue encrypt(Cipher cipher, byte[] input, byte[] key, byte[] salt) throws Exception;
    }

    public static interface DecryptionSpec {
        byte[] decrypt(Cipher cipher, EncryptedValue value, byte[] key) throws Exception;
    }

    public static interface HashSpec{

    }


}
