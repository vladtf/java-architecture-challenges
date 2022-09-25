package com.oop.project.ioc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
    boolean prototype() default false;

    boolean lazy() default false;
}
