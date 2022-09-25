package com.oop.project.ioc.initialization;

import com.oop.project.ioc.ContainerContext;

public interface BeanPostProcessor {
    <T> T processBeanAfterInitialization(T obj, ContainerContext ctx);
}
