package auth.service.utils.gp;

import java.nio.ByteBuffer;

public class ByteUtils {

    public static byte[] concat(byte[]...in){
        int totalLen=0;
        for (byte[] b:in){
            totalLen+=b.length;
        }
        ByteBuffer byteBuffer=ByteBuffer.allocate(totalLen);
        for (byte[] b:in){
            byteBuffer.put(b);
        }
        return byteBuffer.array();
    }
}
