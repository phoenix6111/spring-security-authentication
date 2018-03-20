package com.phoenix.security.core.validate.code;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidateCodeFilter extends OncePerRequestFilter {
    private AuthenticationFailureHandler mAuthenticationFailureHandler;

    //session
    private SessionStrategy mSessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //只有来自用户登陆的url请求，且请求方式为post才执行验证
        if(StringUtils.equals("/authentication/form",request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(),"post")) {
            try {
                validateCode(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                mAuthenticationFailureHandler.onAuthenticationFailure(request,response,e);

                return;
            }
        }

        filterChain.doFilter(request,response);


    }

    private void validateCode(ServletWebRequest request) throws ServletRequestBindingException {

        //从session从获取ImageCode;
        ImageCode imageCodeInSession = (ImageCode) mSessionStrategy.getAttribute(request,ValidateCodeController.SESSION_KEY_IMAGE_CODE);
        //从request中获取用户输入的验证码
        String codeInRequest = ServletRequestUtils.getRequiredStringParameter(request.getRequest(),"imageCode");

        //判断验证码值是否为空
        if(StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码值不能为空");
        }

        //判断session中是否存在验证码
        if(imageCodeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        //判断验证码是否过期
        if(imageCodeInSession.isExpired()) {
            //清除session中的过期验证码
            mSessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY_IMAGE_CODE);
            throw new ValidateCodeException("验证码已过期");
        }

        //判断用户输入的验证码与session中的验证码是否匹配
        if(!StringUtils.equalsIgnoreCase(imageCodeInSession.getCode(),codeInRequest)) {
            throw new ValidateCodeException("验证码输入错误");
        }

        //清除session中的过期验证码
        mSessionStrategy.removeAttribute(request,ValidateCodeController.SESSION_KEY_IMAGE_CODE);
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return mAuthenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        mAuthenticationFailureHandler = authenticationFailureHandler;
    }
}
