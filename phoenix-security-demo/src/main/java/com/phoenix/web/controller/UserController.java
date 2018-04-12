package com.phoenix.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phoenix.dto.User;
import com.phoenix.dto.UserQueryCondition;
import com.phoenix.security.app.social.AppSignUpUtils;
import com.phoenix.security.core.properties.SecurityProperties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private AppSignUpUtils appSignUpUtils;

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 用户注册
     * @param user
     */
    @PostMapping("/register")
    public void register(User user, HttpServletRequest request) {

        String userId = user.getUsername();
        //浏览器环境下完成用户注册
//        providerSignInUtils.doPostSignUp(userId, new ServletWebRequest(request));
        //App环境下完成用户注册
        appSignUpUtils.doPostSignUp(userId,new ServletWebRequest(request));
    }

    @GetMapping("/me")
    public Object getCurrentUser(Authentication user,HttpServletRequest request) throws UnsupportedEncodingException {
        String headerAuthentication = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(headerAuthentication,"bearer ");

        Claims claims = Jwts.parser()
                            .setSigningKey(securityProperties.getOauth2().getJwtSignKey().getBytes("UTF-8"))
                            .parseClaimsJws(token)
                            .getBody();
        logger.info(String.valueOf(claims));
        String company = (String) claims.get("company");
        logger.info("company == "+company);

        return user;
//        return SecurityContextHolder.getContext().getAuthentication();
    }

    @DeleteMapping("/{id:\\d+}")
    public void delete(@PathVariable String id) {
        System.out.println("id === "+id);
    }

    @PutMapping("/{id:\\d+}")
    public User update(@Valid @RequestBody User user, BindingResult errors) {

        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error ->{
                FieldError fieldError = (FieldError) error;
                String errStr = fieldError.getField()+" "+fieldError.getDefaultMessage();
                System.out.println(errStr);
            });
        }

        System.out.println("id = " +user.getId());
        System.out.println("username = " +user.getUsername());
        System.out.println("password = " +user.getPassword());
        System.out.println("birthday = " +user.getBirthday());

        user.setId("1");

        return user;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {

        if(errors.hasErrors()) {
            errors.getAllErrors().stream().forEach(error ->System.out.println(error.getDefaultMessage()));
        }

        System.out.println("id = " +user.getId());
        System.out.println("username = " +user.getUsername());
        System.out.println("password = " +user.getPassword());
        System.out.println("birthday = " +user.getBirthday());

        user.setId("1");

        return user;
    }

    @GetMapping
    @JsonView(User.UserSimpleView.class)
    @ApiOperation(value = "用户查询")
    public List<User> query(UserQueryCondition condition, @PageableDefault(page=10,size=20,sort="username,asc") Pageable pageable) {
        System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

        System.out.println("page === "+pageable.getPageNumber());
        System.out.println("size === "+pageable.getPageSize());
        System.out.println("sort === "+pageable.getSort());

        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        users.add(new User());

        return users;
    }

    @GetMapping("/{id:\\d+}")
    @JsonView(User.UserDetailView.class)
    public User getInfo(@ApiParam(value = "用户ID") @PathVariable String id) {

//        throw new UserNotExistException(id);
//        throw new RuntimeException("User not exist");
        System.out.println("调用getInfo服务");
        User user = new User();
        user.setUsername("phoenix");

        return user;
    }

}
