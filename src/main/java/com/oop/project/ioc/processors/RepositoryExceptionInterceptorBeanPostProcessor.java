package com.oop.project.ioc.processors;

import com.oop.project.ioc.annotations.Configuration;
import com.oop.project.ioc.annotations.Repository;
import com.oop.project.ioc.initialization.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.core.annotation.AnnotationUtils;

// TODO this class must use a chain of interceptor not just one class
// TODO this class still not catches exception
@Configuration
@SuppressWarnings("unchecked")
public class RepositoryExceptionInterceptorBeanPostProcessor implements BeanPostProcessor {

    // TODO add a value for retries


    public RepositoryExceptionInterceptorBeanPostProcessor() {
    }

    @Override
    public <T> T processBeanAfterInitialization(T obj) {
        Class<?> aClass = obj.getClass();

        if (isRepository(aClass)) {
            return (T) Enhancer.create(aClass, handleRepositoryException(obj));
        }

        return obj;
    }

    private static boolean isRepository(Class<?> aClass) {
        return AnnotationUtils.findAnnotation(aClass, Repository.class) != null;
    }

    private static <T> InvocationHandler handleRepositoryException(T obj) {
        return (o, method, args) -> {
            int timesToRetry = 2;

            for (int i = 0; i < timesToRetry; i++) {
                try {
                    return method.invoke(obj, args);
                } catch (Exception e) {
                    if (i == timesToRetry - 1) {
                        throw new RuntimeException(e);
                    }
                }
            }

            throw new RuntimeException("Failed to catch exception when calling method: " + method.getName());
        };
    }
}
