package com.phoenix.code;

import com.phoenix.security.core.validate.code.image.ImageCode;
import com.phoenix.security.core.validate.code.ValidateCodeGenerator;

import org.springframework.web.context.request.ServletWebRequest;

//@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("调用更高级的图形验证码接口。。。");

        return null;
    }
}
