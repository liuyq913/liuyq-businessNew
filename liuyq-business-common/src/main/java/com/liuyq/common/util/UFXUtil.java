package com.liuyq.common.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;


/**
 * ufx加密工具类
 *
 * @author zf
 * @since p2p_cloud_v1.0
 */
public final class UFXUtil
{

    /**
     * ufx私钥加密
     * 
     * @param privateKeyStr
     *            私钥
     * @return str 需要加密的字符串
     * @since p2p_cloud_v1.0
     */
    public static String encrypt(String str, String privateKeyStr)
    {
        PrivateKey privateKey = RSAUtil.generatePrivateKey(privateKeyStr);
        if (privateKey == null)
        {
            throw new RuntimeException("加密私钥为空,请设置");
        }
        Cipher cipher = null;
        try
        {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(str.getBytes("UTF-8"));
            Base64 base64 = new Base64();
            return base64.encodeAsString(base64.encodeAsString(output).getBytes("UTF-8"));
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException("无此加密算法");
        }
        catch(NoSuchPaddingException e)
        {
            e.printStackTrace();
            return null;
        }
        catch(InvalidKeyException e)
        {
            throw new RuntimeException("加密私钥非法,请检查");
        }
        catch(IllegalBlockSizeException e)
        {
            throw new RuntimeException("明文长度非法");
        }
        catch(BadPaddingException e)
        {
            throw new RuntimeException("明文数据已损坏");
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException("编码格式不支持");
        }
    }

    public static void main(String[] args)
    {
        String string = "{\"orderNo\":\"sdsdsdsd\",\"mobile\":\"15100001234\",\"userRole\":\"BORROWERS\",\"returnUrl\":\"http://192.168.131.196:8089/testCallBack/test\",\"notifyUrl\":\"http://192.168.131.196:8089/testCallBack/test\",\"realName\":\"郑正青\",\"idCardNo\":\"130603199310214197\"}";
        String publicKeyStr = "30818902818100908686f2d964125857ef2698c0173cd43e8111c4634faa1c463507850324c2c8c35b1ea9162a54c66171ec018c1abac92bfdcbac8ee346f2baeae7e1c4ce07277f71d8b03d9e1ffceeb15da2085fd4818f35e32d3a6677ff9b682a3db260631c36ba2073f70b508fabcf49532fc90e1b50886b94bf17253fc5242a70dbb574290203010001";
        String privateKeyStr = "30820276020100300d06092a864886f70d0101010500048202603082025c02010002818100908686f2d964125857ef2698c0173cd43e8111c4634faa1c463507850324c2c8c35b1ea9162a54c66171ec018c1abac92bfdcbac8ee346f2baeae7e1c4ce07277f71d8b03d9e1ffceeb15da2085fd4818f35e32d3a6677ff9b682a3db260631c36ba2073f70b508fabcf49532fc90e1b50886b94bf17253fc5242a70dbb574290203010001028180261098886bc354a599fb6df1fb5de3728f47d55321490127d341f24a112ae476962570acde1ea6175de0f064f3eeb2557bd5ab8836561c00a044707061325665a5a8a28a2541189e76180964e1d9ae859f40e6981d9e3f55ca63446696d735fea38190d3478301d3337a1f33d84ff92d4371c8f961140eb093d3ebfa2710a081024100d9f8ebdddd3ee6e0f280d385c7da6e25eeffbad84f836ae1ab2352750dc7882ec8733b9402ab462a0a77d3f37f461a50579c4f8cb586fa77079833246faa4db1024100a9bd52c2643d42fe4ff2ca50c3f235d3732597ebbb683877bebf37b6adcb9c17194aa432d73a4e2478ff067796f203ecf4c108a13df848f88c5bfd7f3037d3f902403d2d6a35f914e0841e4c1e121c47a846a94fc94750f9d793c30ec4e3e99c2912c85bdf4ae97cdcd28be199f16bb56749496dabbe186cebfb607c4d4e5ab0f7e102410088d22612b1b03bff38bc915e57043765116d1d2f9469e71286fc25d1a7f7f7031b8cf21c5527177408e030fb56fe103201fc53937d9b545e9ced4dc22740e929024006121cc1e1f70c8fb16a5907418798640c0ef52ed6db51314c395bedb1c8d41dd81ed6b596eab96b3870ee1f82d0696f48d367ecb7fcfd57eaf42a49a19958eb";
        String sign = generateSign(string, privateKeyStr);
        System.out.println(sign);
        Boolean result = checkSign(string, sign, publicKeyStr);
        System.out.println(result);
    }

    /**
     * 公钥解密
     * 
     * @param publicKeyStr
     *            公钥字符串
     * @param encryptStr
     *            密文
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String publicKeyStr)
    {
        if (StringUtils.isEmpty(encryptStr))
        {
            return encryptStr;
        }
        PublicKey publicKey = RSAUtil.generatePublicKeyFromDer(publicKeyStr);
        if (publicKey == null)
        {
            throw new RuntimeException("解密公钥为空,请设置");
        }
        Cipher cipher = null;
        try
        {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            Base64 base64 = new Base64();
            encryptStr = new String(base64.decode(encryptStr), "UTF-8");
            byte[] output = cipher.doFinal(base64.decode(encryptStr));
            return new String(output, "UTF-8");
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException("无此解密算法");
        }
        catch(NoSuchPaddingException e)
        {
            e.printStackTrace();
            return null;
        }
        catch(InvalidKeyException e)
        {
            throw new RuntimeException("解密公钥非法,请检查", e);
        }
        catch(IllegalBlockSizeException e)
        {
            throw new RuntimeException("密文长度非法", e);
        }
        catch(BadPaddingException e)
        {
            throw new RuntimeException("密文数据已损坏", e);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException("编码格式不支持", e);
        }
    }
    
    /**
     * 生成签名的方法,必须为utf-8格式
     * 
     * @param privateKey
     *            私钥
     * @param str
     *            签名明文字符串
     * @return
     */
    public static String generateSign(String str, String privateKey)
    {
        String signature = null;
        try
        {
            byte[] prikeybytes = RSAUtil.hexString2ByteArr(privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(prikeybytes);
            // 指定的加密算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取私钥匙对象
            PrivateKey privatekey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initSign(privatekey);
            byte[] digest = str.getBytes("UTF-8");
            instance.update(digest);
            byte[] sign = instance.sign();
            signature = RSAUtil.byteArr2HexString(sign);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return signature;
    }

    /**
     * 校验签名参数,必须为utf-8格式
     * 
     * @param str
     *            签名明文
     * @param signInfo
     *            签名参数
     * @param pubKey
     *            公钥
     * @return 返回true和false，true代表验签通过，false代表验签失败
     */
    public static boolean checkSign(String str, String signInfo, String pubKey)
    {
        boolean flag = false;
        try
        {
            Signature instance = Signature.getInstance("SHA1withRSA");
            instance.initVerify(RSAUtil.generatePublicKeyFromDer(pubKey));
            byte[] digest = str.getBytes("UTF-8");
            instance.update(digest);
            flag = instance.verify(RSAUtil.hexString2ByteArr(signInfo));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return flag;
    }

    public static String toUFXAmount(BigDecimal param)
    {
        if (param == null)
            return null;
        BigDecimal amount = param.setScale(2, RoundingMode.DOWN);
        return amount.toPlainString();
    }

    public static String urlEncode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            return s;
        }
    }
    
    public static String encodeReqExt(Object object)
    {
        String str = JSON.toJSONString(object);
        Base64 base64 = new Base64();
        byte[] bytes = str.getBytes();
        return base64.encodeAsString(bytes);
    }
    
    public static <T> T decodeReqExt(String str, Class<T> clazz)
    {
        Base64 base64 = new Base64();
        byte[] bytes = str.getBytes();
        try
        {
            return JSON.parseObject(new String(base64.decode(bytes), "utf-8"), clazz);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
