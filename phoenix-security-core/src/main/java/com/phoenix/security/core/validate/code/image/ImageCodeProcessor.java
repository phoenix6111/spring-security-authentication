package com.phoenix.security.core.validate.code.image;

import com.phoenix.security.core.validate.code.validateCodeProcessor.AbstractValidateCodeProcessor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * User: phoenix
 * Date: 2018-03-22 15:23
 * Description:
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    /**
     * 发送图形验证码，将其写到响应中
     * @param request
     * @param validateCode
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode validateCode) throws Exception {
        ImageIO.write(validateCode.getImage(),"jpeg",request.getResponse().getOutputStream());
    }
}
