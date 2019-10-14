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

    private HashMap<Short, Cipher> cipherHashMap = new HashMap<>();
    private HashMap<Short, SecretKeyFactory> secretKeyFactoryHashMap = new HashMap<>();
    private HashMap<Short, EncryptionSpec> encryptionImpl = new HashMap<>();
    private HashMap<Short, DecryptionSpec> decryptionImpl = new HashMap<>();
    private HashMap<Short, HashSpec> hashImpl = new HashMap<>();


    public void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipherHashMap.put((short) 1, Cipher.getInstance("AES/CBC/PKCS5Padding"));

        encryptionImpl.put((short) 1, (cipher, input, key, salt) -> { // AES/CBC/PKCS5Padding
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),new IvParameterSpec(salt));
            return new EncryptedValue((short) 1, cipher.getIV(), cipher.doFinal(input));
        });

        decryptionImpl.put((short) 1, (cipher, value, key) -> {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(value.getSalt()));
            return cipher.doFinal(value.getValue());
        });

        secretKeyFactoryHashMap.put((short)1,SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"));
        hashImpl.put((short) 1, (secretKeyFactory,password, salt, iterations, keyLength) -> {
            SecretKeyFactory keyFactory=((SecretKeyFactory) secretKeyFactory);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
            SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
            return secretKey.getEncoded();
        });
    }

    private Cipher getCipherByType(short type){
        Cipher cipher=cipherHashMap.get(type);
        if (cipher==null){
            throw new IllegalArgumentException(String.format("No cipher for type %d found", type));
        }
        return cipher;
    }

    private SecretKeyFactory getSecretKeyFactoryByType(short type){
        SecretKeyFactory keyFactory=secretKeyFactoryHashMap.get(type);
        if (keyFactory==null){
            throw new IllegalArgumentException(String.format("No key factory found for type %d",type));
        }
        return keyFactory;
    }

    public EncryptedValue encrypt(short type,byte[] input,byte[] key,byte[] salt) throws Exception {
        EncryptionSpec encryptionSpec=encryptionImpl.get(type);
        if (encryptionSpec==null){
            throw new IllegalArgumentException(String.format("No encryption implementation found for type %d",type));
        }
        return encryptionSpec.encrypt(getCipherByType(type),input,key,salt);
    }

    public byte[] decrypt(EncryptedValue value,byte[] key) throws Exception {
        DecryptionSpec decryptionSpec=decryptionImpl.get(value.getType());
        if (decryptionSpec==null){
            throw new IllegalArgumentException(String.format("No decryption implementation found for type %d",value.getType()));
        }
        return decryptionSpec.decrypt(getCipherByType(value.getType()),value,key);
    }

    public byte[] hash(short type,String data, byte[] salt, int iterations, int keyLength) throws Exception {
        HashSpec hashSpec=hashImpl.get(type);
        if (hashSpec==null){
            throw new IllegalArgumentException(String.format("No hash implementation found for type %d", type));
        }
        return hashSpec.hash(getSecretKeyFactoryByType(type),data,salt,iterations,keyLength);
    }


    public static interface EncryptionSpec {
        EncryptedValue encrypt(Cipher cipher, byte[] input, byte[] key, byte[] salt) throws Exception;
    }

    public static interface DecryptionSpec {
        byte[] decrypt(Cipher cipher, EncryptedValue value, byte[] key) throws Exception;
    }

    public static interface HashSpec {
        byte[] hash(Object secretKeyFactory,String data, byte[] salt, int iterations, int keyLength) throws Exception;
    }


}
