package hello.proxy.config.v4_postprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PackageLogTracePostPorcessor implements BeanPostProcessor {

    private final String basePackage;
    private final Advisor advisor;

    public PackageLogTracePostPorcessor(String basePackage, Advisor advisor) {
        this.basePackage = basePackage;
        this.advisor = advisor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        log.info("param 1: {}, param 2: {}", bean, beanName);
        //프록시 적용 대상 여부체크
        //프록시 적용 대상이 아니면 원본을 그대로 진행

        String packageName = bean.getClass().getPackageName();
        //패키지명이 베이스 패키지 이하면 프록시 적용
        if (!packageName.startsWith(basePackage)) {
            return bean;
        }

        // 프록시 대상이면 프록시를 만들어서 반환
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvisor(advisor);

        Object proxy = proxyFactory.getProxy();
        log.info("{} 프록시 적용", bean.getClass().getName());
        return proxy;
    }
}
