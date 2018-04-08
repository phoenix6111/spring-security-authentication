package com.phoenix.security.core.validate.code;

import com.phoenix.security.core.properties.SecurityConstants;
import com.phoenix.security.core.properties.SecurityProperties;
import com.phoenix.security.core.validate.code.validateCodeProcessor.ValidateCodeProcessor;
import com.phoenix.security.core.validate.code.validateCodeProcessor.ValidateCodeProcessorHolder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码Filter
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean{

    private Logger mLogger = LoggerFactory.getLogger(getClass());

    /**
     * 验证码校验失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 系统中的校验码处理器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 封装系统配置的properties
     */
    @Autowired
    private SecurityProperties mSecurityProperties;

    /**
     * 存放所有需要校验验证码的url-ValidateCodeType键值对
     */
    private Map<String,ValidateCodeType> urlMap = new HashMap<>();

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private AntPathMatcher mAntPathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        //添加ImageCode需要验证的url
        //因为 /authentication/form 这个url一定要做检验，所以最后加这一个url
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM,ValidateCodeType.IMAGE);
        addUrlToMap(mSecurityProperties.getCode().getImage().getUrls(),ValidateCodeType.IMAGE);

        //添加SmsCode需要验证的url，同上
        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,ValidateCodeType.SMS);
        addUrlToMap(mSecurityProperties.getCode().getSms().getUrls(),ValidateCodeType.SMS);
    }

    /**
     * 将url-ValidateCodeType的键值对加入Map
     * @param urlStr
     * @param type
     */
    private void addUrlToMap(String urlStr,ValidateCodeType type) {
        if(StringUtils.isNotBlank(urlStr)){
            String[] matchedUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlStr,",");
            for (String url : matchedUrls) {
                urlMap.put(url,type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        mLogger.info("进入校验验证码 ValidateCodeFilter");

        //根据request的url取出需要校验的ValidateCodeType
        ValidateCodeType type = getValidateCodeType(request);
        //如果能获取到ValidateCodeType，证明需要校验该url的验证码，否则不需要校验url
        if(type != null) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);

            try {
                ValidateCodeProcessor processor = validateCodeProcessorHolder.findValidateCodeProcessor(type);
                processor.validate(new ServletWebRequest(request,response));

                mLogger.info("验证码校验通过");
            } catch (ValidateCodeException e) {
                mLogger.info("验证码校验失败");
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }

    /**
     * 根据request的url获取ValidateCodeType
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;

        //对于非get请求才校验验证码
        if(!StringUtils.equalsIgnoreCase(request.getMethod(),"get")) {
            //取出所有需要校验url的地址
            Set<String> urls = urlMap.keySet();
            for(String url : urls) {
                if(mAntPathMatcher.match(url,request.getRequestURI())) {
                    result = urlMap.get(url);
                    break;
                }
            }
        }

        return result;
    }

    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return authenticationFailureHandler;
    }

    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        mSecurityProperties = securityProperties;
    }
}
