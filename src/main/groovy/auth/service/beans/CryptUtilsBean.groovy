package auth.service.beans

import auth.service.utils.gp.CryptUtils

@Singleton
class CryptUtilsBean extends CryptUtils{
    HashMap<Short,Integer> iterationTypes=new HashMap<>()
    HashMap<Short,Integer> keyLenTypes=new HashMap<>()

    CryptUtils(){
        super()
        iterationTypes.put(1 as short,262144)

        keyLenTypes.put(1 as short,256)
    }

    int getIterationByType(short type){
        return iterationTypes.get(type)
    }

    int getKeyLenByType(short type){
        return keyLenTypes.get(type)
    }
}
