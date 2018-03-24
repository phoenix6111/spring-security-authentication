package com.phoenix.security.core.validate.code.validateCodeProcessor;

import com.phoenix.security.core.validate.code.ValidateCodeException;
import com.phoenix.security.core.validate.code.ValidateCodeType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * User: phoenix
 * Date: 2018-03-22 18:51
 * Description: 根据ValidateCode的类型查找ValidateCodProcessor
 */
@Component
public class ValidateCodeProcessorHolder {

    @Autowired
    private Map<String,ValidateCodeProcessor> validateCodeProcessors;

    /**
     * 根据ValidateCodeType 查找ValidateCodeProcessor
     * @param type
     * @return
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return this.findValidateCodeProcessor(type.toString().toLowerCase());
    }

    /**
     * 根据String类型的ValidateCodeType查找ValidateCodeProcessor
     * @param type
     * @return
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        //validateCodeProcessors中的key是spring自动注入的，值为ValidateCodeProcessor子类中@component注解的值，而不是类名
        String key = type.toLowerCase()+ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(key);

        if(processor == null) {
            throw new ValidateCodeException("验证码处理器" + key + "不存在");
        }

        return processor;
    }


}
