package com.oop.project.ioc.utils;

import com.oop.project.ioc.annotations.Bean;
import org.springframework.core.annotation.AnnotationUtils;

import javax.swing.plaf.InsetsUIResource;

public class ReflectionUtils {
    private ReflectionUtils() {
        throw new RuntimeException("Not allowed to be instantiated!");
    }

    public static Bean getBeanAnnotation(Class<?> aClass) {
        return AnnotationUtils.findAnnotation(aClass, Bean.class);
    }

    public static boolean isBeanAnnotationPresent(Class<?> aClass) {
        return getBeanAnnotation(aClass) != null;
    }

}
