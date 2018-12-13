package com.liuyq.redisLock.token;

import com.liuyq.common.redis.RedisUtil;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by liuyq on 2018/12/13.
 */
public class TokenGenerate {

    @Resource
    private RedisUtil redisUtil;

    private final Integer tokenEffectiveDuration = 120*60*1000;//token有效时长 两个小时

    private final String User_Name_Key_ = "User_Name_Key_";//用户缓存中key的前缀，后面要加用户名滴
    private final String User_Token_Field = "User_Token_Field";//用户缓存中的Token Field


    private final String Token_User_Name_Field = "Token_User_Name_Field"; //token缓存中的用户名Field
    private final String Token_Invalid_Time_Field = "Token_Invalid_Time_Field";//token缓存中的失效时间Field
    private final String Token_Refreshtoken_Field = "Token_Refreshtoken_Field";//token缓存中的Refreshtoken Field


    /**
     * 通过用户名获取token  请求接口的时候带上token，检验是否超时
     * @param username
     * @return
     */
    public String getToken(String username){
        String userNameKey =User_Name_Key_+username;
        String cachedToken =  redisUtil.hashGet(userNameKey, User_Token_Field); //重新获取tokend的时候，先获取到老的token并删掉
        if(cachedToken != null){
            redisUtil.delKey(cachedToken);
        }

        Long InvalidTime = System.currentTimeMillis()+tokenEffectiveDuration;//token失效时间

        String newToken = generateToken();
        String refreshToken = generateToken();

        redisUtil.hashSet(userNameKey, User_Token_Field, newToken); // 设置新值
        redisUtil.hashSet(newToken, Token_User_Name_Field, User_Token_Field);//token绑定用户名称
        redisUtil.hashSet(newToken, Token_Invalid_Time_Field, InvalidTime.toString());//绑定token的失效时间
        redisUtil.hashSet(newToken, Token_Refreshtoken_Field, refreshToken);// 绑定token刷新token

        return newToken;
    }


    /**
     * 刷新 token时长
     * @return
     */
    public String doRefreshToken(String token, String refreshToken){
        String cachedRefreshToken = redisUtil.hashGet(token, Token_Refreshtoken_Field);
        if(!refreshToken.equals(cachedRefreshToken)){
            System.out.println("令牌错误");
        }
        Long InvalidTime = System.currentTimeMillis()+tokenEffectiveDuration;//token失效时间
        refreshToken = generateToken();
        //更新失效时间
        redisUtil.hashSet(token, Token_Invalid_Time_Field, cachedRefreshToken);
        //更新刷新的令牌
        redisUtil.hashSet(token, Token_Refreshtoken_Field, refreshToken);
        return token;
    }

    private String generateToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
