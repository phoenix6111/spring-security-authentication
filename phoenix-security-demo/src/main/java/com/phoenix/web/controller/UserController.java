package com.phoenix.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.phoenix.dto.User;
import com.phoenix.dto.UserQueryCondition;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/me")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails user) {
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
