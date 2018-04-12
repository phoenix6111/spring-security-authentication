package com.phoenix.security.app.social;

import com.phoenix.security.app.AppSecurityException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * User: sheng
 * Date: 2018-04-09 17:31
 * Description: App登陆Utils
 */
@Component
public class AppSignUpUtils {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * 保存ConnectionData
     * @param connectionData
     */
    public void saveConnectionData(ConnectionData connectionData, WebRequest request) {
        redisTemplate.opsForValue().set(getKey(request),connectionData, 10 ,TimeUnit.MINUTES);
    }

    /**
     * 用户注册
     * @param userId
     * @param request
     */
    public void doPostSignUp(String userId, WebRequest request) {

        //从Redis中获取保存在Redis中的ConnectionData
        String key = getKey(request);
        if(!redisTemplate.hasKey(key)) {
            throw new AppSecurityException("无法找到缓存的用户社交账户信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);

        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
                                                        .createConnection(connectionData);
        //保存用户信息
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);

        redisTemplate.delete(key);
    }

    /**
     * 获取key
     * @param request
     * @return
     */
    private String getKey(WebRequest request) {
        String devideId = request.getHeader("deviceId");
        if(StringUtils.isBlank(devideId)) {
            throw new AppSecurityException("设备ID参数不能为空，请设置设备ID");
        }

        return "phoenix:security:social.connect."+devideId;
    }


}
