package com.supermap.datashare.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加密解密主要方法
 *
 * 加密，直接使用 encryptBySavedKey 方法；
 * 解密，直接使用 decryptBySavedKey 方法；
 *
 * 系统采用 RSA/NONE/NoPadding （无随机数）的方式，同样的数据加密之后，密文一致。
 *
 * @author LeiShiGuang
 * Date  2017/9/1 17:19
 * @version v1.9
 */

public class Rsa {
    /**
     * 定义加密方式，无需修改
     */
    private final static String KEY_RSA = "RSA";
    /**
     * 定义签名算法，无需修改
     */
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    /**
     * 定义公钥算法，无需修改
     */
    private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    /**
     * 定义私钥算法，无需修改
     */
    private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";

    //公钥
    private static final String  PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALzaPKYF64NDjUmE+k9wZvbkpYUJQeXc/glEURFg8Ilh\n" +
            "aAWaKN5/xkdPkHdCGWoqcIA6d4LrByaQByvPM8clmUsCAwEAAQ==";
    //私钥
    private static final String  PRIVATE_KEY = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAvNo8pgXrg0ONSYT6T3Bm9uSlhQlB\n" +
            "5dz+CURREWDwiWFoBZoo3n/GR0+Qd0IZaipwgDp3gusHJpAHK88zxyWZSwIDAQABAkEAsVfjUikH\n" +
            "A8og2KRmhsjP+BO5F5gc6OXBLSSZWE+HMgnGcmRpuPFVwsqHgDg7rogTeii66qhgyn0LCFPev/Wt\n" +
            "OQIhAPQj/yIu2M+GbrleCGkI1UQ9pTga788cfcygI/yD6qjNAiEAxga02mm9cJ+kINe2FtSMhH+p\n" +
            "vw+3L2zYBAoh2IjdqncCICpBXLQ+5Xmtq9Zbhxse00K3ZLQ8PUtchA15niDjeCb1AiBNKlvbKI07\n" +
            "j9njOmuoZdcD4sOAwlz9zAo5OMGLoXnPbQIgfyZ/nSPoMGDadX/mcNQ4C2dzUDApG7fPXeYgQA8h\n" +
            "+kM=";

    //字符串进行加密填充的名称
    private static final String PADDING = "RSA/NONE/NoPadding";

    //字符串持有安全提供者的名称
    private static final String PROVIDER = "BC";

    //初始化
    private static Cipher CIPHER =  null;
    private static PrivateKey PRIVATEKEY = null;
    private static PublicKey PUBLICKEY = null;


    /**
     * 解密，使用已保存的私钥
     * @param data Base64字符串，从数据库中直接读取的加密之后的数据
     * @return 解密之后的字符串
     */
    public static String decryptBySavedKey(String data){
        //判断data的长度，如果长度小于一个值，则是未加密数据，直接返回。
        if (data == null) return null;
        if (data.length() < 70)return data;
        System.out.println(data.length());
        initCliper();
        byte[] result = decryptByPrivateKey(data,PRIVATE_KEY);
        try {
			return new String(result,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return "";
    }

    /**
     * 加密，使用已保存的公钥
     * @param data 待加密数据，字符串
     * @return 加密之后的字符串
     * @throws UnsupportedEncodingException 
     */
    public static String encryptBySavedKey(String data) throws UnsupportedEncodingException{
        if(data==null||"".equals(data))
            return null;
        if (data.length() > 76){
            return data;
        }
        initCliper();
        return encryptByPublicKeyReturnString(data.getBytes("UTF-8"),PUBLIC_KEY);
    }

    /**
     * 初始化加密解密的 cipher
     */
    private static void initCliper(){
        // 初始化加密解密CIPHER;
        if(CIPHER == null){
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            try {
                CIPHER = Cipher.getInstance(PADDING, PROVIDER);
            } catch (NoSuchAlgorithmException e) {
                System.err.println("初始化加密解密CIPHER失败：NoSuchAlgorithmException");
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                System.err.println("初始化加密解密CIPHER失败：NoSuchProviderException");
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                System.err.println("初始化加密解密CIPHER失败：NoSuchPaddingException");
                e.printStackTrace();
            }
        }
        // 初始化私钥
        if(PRIVATEKEY == null){
            byte[] bytes1;
            try {
                bytes1 = decryptBase64(PRIVATE_KEY);
                // 取得私钥
                PKCS8EncodedKeySpec keySpec1 = new PKCS8EncodedKeySpec(bytes1);
                KeyFactory factory1 = KeyFactory.getInstance(KEY_RSA);
                PRIVATEKEY = factory1.generatePrivate(keySpec1);
            } catch (Exception e) {
                System.err.println("初始化私钥失败");
                e.printStackTrace();
            }
        }
        // 初始化公钥
        if(PUBLICKEY == null){
            byte[] bytes2;
            try {
                // 对公钥解密
                bytes2 = decryptBase64(PUBLIC_KEY);
                // 取得公钥
                X509EncodedKeySpec keySpec2 = new X509EncodedKeySpec(bytes2);
                KeyFactory factory2 = KeyFactory.getInstance(KEY_RSA);
                PUBLICKEY = factory2.generatePublic(keySpec2);
            } catch (Exception e) {
                System.err.println("初始化公钥失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化密钥（重新生成新的密钥对）
     * @deprecated
     */
    private static Map<String, Object> init() {
        Map<String, Object> map = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            generator.initialize(512);
            KeyPair keyPair = generator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 将密钥封装为map
            map =new HashMap();
            map.put(KEY_RSA_PUBLICKEY, publicKey);
            map.put(KEY_RSA_PRIVATEKEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 用私钥对信息生成数字签名
     * @param data 加密数据
     * @param privateKey 私钥
     * @deprecated
     */
    private static String sign(byte[] data, String privateKey) {
        String str = "";
        try {
            // 解密由base64编码的私钥
            byte[] bytes = decryptBase64(privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取私钥对象
            PrivateKey key = factory.generatePrivate(pkcs);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str = encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 校验数字签名
     * @param data 加密数据
     * @param publicKey 公钥
     * @param sign 数字签名
     * @return 校验成功返回true，失败返回false
     * @deprecated
     */
    private static boolean verify(byte[] data, String publicKey, String sign) {
        boolean flag = false;
        try {
            // 解密由base64编码的公钥
            byte[] bytes = decryptBase64(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取公钥对象
            PublicKey key = factory.generatePublic(keySpec);
            // 用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 私钥解密
     * @param data 加密数据
     * @param key 私钥
     * @deprecated
     */
    private static byte[] decryptByPrivateKey(byte[] data, String key) {
        byte[] result = null;
        //CIPHER是非线程安全的，得加锁，否则可能会出错
        String lock = "";//随便搞了个锁
        synchronized (lock) {
        	try {
        		// 对私钥解密
        		//byte[] bytes = decryptBase64(key);
        		// 取得私钥
        		//PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        		//KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
        		//PrivateKey privateKey = factory.generatePrivate(keySpec);
        		// 对数据解密
        		// Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        		//Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
        		CIPHER.init(Cipher.DECRYPT_MODE, PRIVATEKEY);
        		result = CIPHER.doFinal(data);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
		}
        return result;
    }

    /**
     * 私钥解密，从Base64中进行解密
     * @param data 加密数据，Base64字符串
     * @param key 私钥
     * @deprecated
     */
    private static byte[] decryptByPrivateKey(String data, String key) {
        byte[] waitDec = null;
        try {
            waitDec = decryptBase64(data);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return decryptByPrivateKey(waitDec,key);
    }



    /**
     * 公钥解密
     * @param data 加密数据
     * @param key 公钥
     * @deprecated
     */
    private static byte[] decryptByPublicKey(byte[] data, String key) {
        byte[] result = null;
      //CIPHER是非线程安全的，得加锁，否则可能会出错
        String lock = "";//随便搞了个锁
        synchronized (lock) {
	        try {
	            // 对公钥解密
	            //byte[] bytes = decryptBase64(key);
	            // 取得公钥
	            //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
	            //KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
	            //PublicKey publicKey = factory.generatePublic(keySpec);
	            // 对数据解密
	            // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	            //Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
	            CIPHER.init(Cipher.DECRYPT_MODE, PUBLICKEY);
	            result = CIPHER.doFinal(data);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
        }
        return result;
    }

    /**
     * 公钥加密
     * @param data 待加密数据
     * @param key 公钥
     * @deprecated
     */
    private static byte[] encryptByPublicKey(byte[] data, String key) {
        byte[] result = null;
        try {
            //byte[] bytes = decryptBase64(key);
            // 取得公钥
            //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            //KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            //PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据加密
            //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            //Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            CIPHER.init(Cipher.ENCRYPT_MODE, PUBLICKEY);
            result = CIPHER.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 公钥加密，返回Base64字符串
     * @param data 待加密数据，字节类型
     * @param key 公钥
     * @deprecated
     */
    private static String encryptByPublicKeyReturnString(byte[] data, String key) {
        String str = "";
        byte[] encWord = encryptByPublicKey(data, key);
        try {
            str = encryptBase64(encWord);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }



    /**
     * 私钥加密
     * @param data 待加密数据
     * @param key 私钥
     * @deprecated
     */
    private static byte[] encryptByPrivateKey(byte[] data, String key) {
        byte[] result = null;
        try {
            //byte[] bytes = decryptBase64(key);
            // 取得私钥
            //PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            //KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            //PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据加密
            //Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            //Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            CIPHER.init(Cipher.ENCRYPT_MODE, PRIVATEKEY);
            result = CIPHER.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取公钥
     * @param map 当前类中生成的map
     * @deprecated
     */
    private static String getPublicKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 获取私钥
     * @param map 当前类中保存的map
     * @deprecated
     */
    private static String getPrivateKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * BASE64 解密，字符串->二进制
     * @param key 需要解密的字符串
     * @return 字节数组
     * @deprecated
     * */
    private static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64 加密，二进制->字符串
     * @param key 需要加密的字节数组
     * @return 字符串
     * @deprecated
     */
    private static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * 测试方法
     * @param args 为空
     * @throws UnsupportedEncodingException 
     * @deprecated
     */
    public static void main(String[] args) throws UnsupportedEncodingException {

        initCliper();
        String privateKey = "";
        String publicKey = "";
        // 直接生成公钥私钥，不从文件读取
        Map<String, Object> map = init();
        publicKey = getPublicKey(map);
        privateKey = getPrivateKey(map);

        //文件中读取私钥密钥
        if (true){
            publicKey = PUBLIC_KEY;
            privateKey = PRIVATE_KEY;
        }
        //对照文件中读取的和保存的的区别
        System.out.println("公钥（从文件读取）： \n\r" + publicKey);
        System.out.println("公钥（存储前）： \n\r" + getPublicKey(map));
        System.out.println("私钥（从文件读取）： \n\r" + privateKey);
        System.out.println("私钥（存储前）： \n\r" + getPrivateKey(map));
        System.out.println("公钥加密--------私钥解密");
        //String word = "你好，世界！";
        String word = "360724111111111110";
        byte[] encWord = encryptByPublicKey(word.getBytes("UTF-8"), publicKey);
        String decWord = new String(decryptByPrivateKey(encWord, privateKey));
        //将加密后的密文（二进制）转化为字符串，用于密文存储
        String encedWord = "";
        try {
            encedWord = encryptBase64(encWord);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("密文：\n\r" + encedWord);
        System.out.println("密文长度：\n\r" + encedWord.length());
        //将加密后的密文（字符串）转化为二进制，用于解密
        byte[] waitDec = null;
        try {
            waitDec = decryptBase64(encedWord);
        }catch (Exception e) {
            e.printStackTrace();
        }
        String decedWord = new String(decryptByPrivateKey(waitDec, privateKey));

        System.out.println("加密前： " + word + "\n\r" + "解密后: " + decWord);
        System.out.println("存储的密文解密后：\n\r" + decedWord);
        System.out.println("\n私钥加密--------公钥解密");
        String english = "Hello, World!";
        byte[] encEnglish = encryptByPrivateKey(english.getBytes("UTF-8"), privateKey);
        String decEnglish = new String(decryptByPublicKey(encEnglish, publicKey));
        System.out.println("加密前： " + english + "\n\r" + "解密后: " + decEnglish);
        System.out.println("私钥签名——公钥验证签名");
        // 产生签名
        String sign = sign(encEnglish, privateKey);
        System.out.println("签名：\n\r" + sign);
        // 验证签名
        boolean status = verify(encEnglish, publicKey, sign);
        System.out.println("状态：\n\r" + status);
    }
}
