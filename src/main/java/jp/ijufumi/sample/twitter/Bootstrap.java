package jp.ijufumi.sample.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootApplication
public class Bootstrap {
    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    /**
     * LoggerをDIできるようにするための設定
     *
     * @param point
     * @return
     */
    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public Logger logger(InjectionPoint point) {
        return LoggerFactory.getLogger(point.getMember().getDeclaringClass());
    }
}
