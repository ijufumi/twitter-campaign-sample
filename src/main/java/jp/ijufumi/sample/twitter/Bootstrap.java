package jp.ijufumi.sample.twitter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import javax.sql.DataSource;

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

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public HikariConfig hikariConfig(
            @Value("${database.connection.username}") String username,
            @Value("${database.connection.password}") String password,
            @Value("${database.connection.jdbc-url}") String jdbcUrl,
            @Value("${database.connection.driver-class-name}") String driverClassName,
            @Value("${database.connection.connection-timeout}") long connectionTimeout,
            @Value("${database.connection.pool-size}") int poolSize,
            @Value("${database.connection.min-idle}") int minIdle,
            @Value("${database.connection.test-query}") String testQuery)
    {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(driverClassName);
        config.setConnectionTimeout(connectionTimeout);
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(minIdle);
        config.setConnectionTestQuery(testQuery);

        return config;
    }
}
