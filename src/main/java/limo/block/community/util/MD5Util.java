package limo.block.community.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * MD5加密工具类
 */
public class MD5Util {
    public final static String SALT="limo";

    public static String md5(String str , String salt){
        return new Md5Hash(str,salt).toString();
    }

}
