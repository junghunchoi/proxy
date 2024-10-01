package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@Slf4j
public class MultiAdvisorTest {

    @Test
    @DisplayName("여러 프록시")
    void multi1() {

        //proxy 1
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory1 = new ProxyFactory(target);
        DefaultPointcutAdvisor defaultPointcutAdvisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice1());
        proxyFactory1.addAdvisor(defaultPointcutAdvisor1);
        ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

        //proxy2
        ProxyFactory proxyFactory2 = new ProxyFactory(proxy1);
        DefaultPointcutAdvisor defaultPointcutAdvisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new Advice2());
        proxyFactory1.addAdvisor(defaultPointcutAdvisor2);
        ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();
        proxy2.find();
        proxy2.save();

    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
            log.info("Advice1");
            return invocation.proceed();
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(org.aopalliance.intercept.MethodInvocation invocation) throws Throwable {
            log.info("Advice2");
            return invocation.proceed();
        }
    }
}
