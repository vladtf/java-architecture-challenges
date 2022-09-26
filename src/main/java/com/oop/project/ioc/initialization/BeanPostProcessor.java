package com.oop.project.ioc.initialization;

import com.oop.project.ioc.ApplicationContext;

public interface BeanPostProcessor {
    <T> T processBeanAfterInitialization(T obj, ApplicationContext ctx);
}
