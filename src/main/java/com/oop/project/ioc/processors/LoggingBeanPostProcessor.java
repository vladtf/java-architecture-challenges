package com.oop.project.ioc.processors;

import com.oop.project.ioc.annotations.Configuration;
import com.oop.project.ioc.annotations.Logged;
import com.oop.project.ioc.initialization.BeanPostProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Configuration
@Log4j2
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    public LoggingBeanPostProcessor() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T processBeanAfterInitialization(T bean) {
        if (bean.getClass().isAnnotationPresent(Logged.class)) {
            return (T) Enhancer.create(bean.getClass(), (MethodInterceptor) (o, method, objects, methodProxy) -> {
                log.info("Post processor intercepted call of: {}", method.getName());
                return method.invoke(bean, objects);
            });
        }

        return bean;
    }
}
