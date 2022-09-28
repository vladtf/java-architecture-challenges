package com.oop.project.ioc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Bean
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
}
