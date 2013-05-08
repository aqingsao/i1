package com.thoughtworks.i1.commons.test;

import com.thoughtworks.i1.commons.I1Application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TestApplication {
    Class<? extends I1Application> value();
}
