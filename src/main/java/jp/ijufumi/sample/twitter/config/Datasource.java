package jp.ijufumi.sample.twitter.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "database.connection")
@Data
public class Datasource {

    String username;

    String password;

    String jdbcUrl;

    String driverClassName;

    long connectionTimeout;

    int poolSize;

    int minIdle;

    String testQuery;

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public HikariConfig hikariConfig() {
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
