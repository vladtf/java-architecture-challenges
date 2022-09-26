package com.oop.project.ioc.initialization;

public interface ExceptionInterceptor {
    // TODO this method will be used by a postProcessor that will define a behaviour in case of exception using a set of Interceptors
    //  need to find out what this function will get and return (maybe to keep it void and accept thrown exception)
    void intercept();
}
