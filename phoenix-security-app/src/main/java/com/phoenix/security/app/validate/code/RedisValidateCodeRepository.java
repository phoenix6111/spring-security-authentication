package com.phoenix.security.app.validate.code;

import com.phoenix.security.core.validate.code.ValidateCode;
import com.phoenix.security.core.validate.code.ValidateCodeException;
import com.phoenix.security.core.validate.code.ValidateCodeRepository;
import com.phoenix.security.core.validate.code.ValidateCodeType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * User: sheng
 * Date: 2018-04-07 18:26
 * Description: 基于Redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    /**
     * 保存验证码进Redis中
     * @param request
     * @param validateCode
     * @param validateCodeType
     */
    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        redisTemplate.opsForValue().set(buildKey(request,validateCodeType),validateCode,30,TimeUnit.MINUTES);
    }

    /**
     * 获取验证码
     * @param request
     * @param validateCodeType
     * @return
     */
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object value = redisTemplate.opsForValue().get(buildKey(request,validateCodeType));
        if(value == null) {
            return null;
        }

        return (ValidateCode) value;
    }

    /**
     * 从Redis中删除验证码
     * @param request
     * @param validateCodeType
     */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request,validateCodeType));
    }

    /**
     * 根据request中的deviceId生成redis中的验证码对应的key
     * @param request
     * @param validateCodeType
     * @return
     */
    private String buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中设置deviceId参数");
        }

        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }
}
