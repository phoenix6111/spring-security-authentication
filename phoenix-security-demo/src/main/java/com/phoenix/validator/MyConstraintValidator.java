package com.phoenix.validator;

import com.phoenix.service.HelloService;

import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MyConstraintValidator implements ConstraintValidator<MyConstraint,Object>{

    @Autowired
    private HelloService mHelloService;

    @Override
    public void initialize(MyConstraint myConstraint) {
        System.out.println("MyConstraint validator initilized...");
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        mHelloService.sayHello("validate");
        System.out.println(o);
        return false;
    }
}
