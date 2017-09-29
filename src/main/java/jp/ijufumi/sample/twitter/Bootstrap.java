package jp.ijufumi.sample.twitter;

import jp.ijufumi.sample.twitter.config.Datasource;
import jp.ijufumi.sample.twitter.interceptor.TwitterConnectionInterceptor;
import jp.ijufumi.sample.twitter.service.CampaignService;
import jp.ijufumi.sample.twitter.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@SpringBootApplication
@Import(Datasource.class)
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

    @Bean
    public HandlerInterceptor twitterConnectionInterceptor(
            TwitterService twitterService,
            CampaignService campaignService,
            Logger logger
    ) {
        return new TwitterConnectionInterceptor(twitterService, campaignService, logger);
    }

    @Bean
    public MappedInterceptor mappedInterceptor(TwitterConnectionInterceptor twitterConnectionInterceptor) {
        return new MappedInterceptor(new String[]{"/**"}, twitterConnectionInterceptor);
    }
}
