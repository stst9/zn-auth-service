package auth.service.utils.gp;

import java.nio.ByteBuffer;

public class EncryptedValue {
    private short type;
    private byte[] binType;
    private byte[] salt;
    private byte[] value;
    private CryptUtils cryptUtils;

    public static final int TYPE_LEN=2;
    public static final int SALT_LEN=16;

    public EncryptedValue(byte[] input) {
        binType = new byte[TYPE_LEN];
        System.arraycopy(input, 0, binType, 0, TYPE_LEN);
        salt = new byte[16];
        System.arraycopy(input, 2, salt, 0, SALT_LEN);
        value = new byte[input.length - TYPE_LEN - SALT_LEN];
        System.arraycopy(input, TYPE_LEN + SALT_LEN ,value, 0, value.length);
        type = ByteBuffer.wrap(binType).getShort();
    }

    public EncryptedValue(short type, byte[] salt, byte[] value) {
        this.type = type;
        this.salt = salt;
        this.value = value;
    }

    public byte[] toByteArray(){
        byte[] ret=new byte[TYPE_LEN+SALT_LEN+value.length];
        ByteBuffer buffer=ByteBuffer.allocate(TYPE_LEN);
        buffer.putShort(type);
        System.arraycopy(buffer.array(),0,ret,0,TYPE_LEN);
        System.arraycopy(salt,0,ret,TYPE_LEN,SALT_LEN);
        System.arraycopy(value,0,ret,TYPE_LEN+SALT_LEN,value.length);
        return ret;
    }

    public short getType() {
        return type;
    }

    public byte[] getBinType() {
        return binType;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getValue() {
        return value;
    }
}
