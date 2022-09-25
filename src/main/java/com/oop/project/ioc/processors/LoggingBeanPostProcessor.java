package com.oop.project.ioc.processors;

import com.oop.project.ioc.annotations.Configuration;
import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.annotations.Logged;
import com.oop.project.ioc.initialization.BeanPostProcessor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    public <T> T processBeanAfterInitialization(T bean, ContainerContext ctx) {
        if (bean.getClass().isAnnotationPresent(Logged.class)) {
            return (T) Enhancer.create(bean.getClass(), new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    log.info("Post processor intercepted call of: {}", method.getName());
                    return method.invoke(bean, objects);
                }
            });
        }

        return bean;
    }
}
