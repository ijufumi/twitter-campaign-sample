package jp.ijufumi.sample.twitter

import jp.ijufumi.sample.twitter.interceptor.TwitterConnectionInterceptor
import jp.ijufumi.sample.twitter.service.CampaignService
import jp.ijufumi.sample.twitter.service.TwitterService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.handler.MappedInterceptor


@SpringBootApplication
open class Bootstrap {
    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
            SpringApplication.run(Bootstrap::class.java, *args)
        }
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    open fun logger(point : InjectionPoint) : Logger {
        return LoggerFactory.getLogger(point.member.declaringClass)
    }

    @Bean
    open fun twitterConnectionInterceptor(
            twitterService: TwitterService,
            campaignService: CampaignService,
            logger: Logger
    ): HandlerInterceptor {
        return TwitterConnectionInterceptor(twitterService, campaignService, logger)
    }

    @Bean
    open fun mappedInterceptor(twitterConnectionInterceptor: TwitterConnectionInterceptor): MappedInterceptor {
        return MappedInterceptor(arrayOf("/**"), twitterConnectionInterceptor)
    }

}
