package auth.service.beans

import auth.service.utils.gp.CryptUtils

import javax.crypto.NoSuchPaddingException
import javax.inject.Singleton
import java.security.NoSuchAlgorithmException

@Singleton
class CryptUtilsBean extends CryptUtils{
    HashMap<Short,Integer> iterationTypes=new HashMap<>()
    HashMap<Short,Integer> keyLenTypes=new HashMap<>()

    CryptUtilsBean(){
        try {
            init()
        }catch(Exception e){
            e.printStackTrace()
        }
    }

    @Override
    void init() throws NoSuchPaddingException, NoSuchAlgorithmException {

        iterationTypes.put(1 as short,262144)

        keyLenTypes.put(1 as short,256)
        super.init()
    }

    int getIterationByType(short type){
        return iterationTypes.get(type)
    }

    int getKeyLenByType(short type){
        return keyLenTypes.get(type)
    }
}
