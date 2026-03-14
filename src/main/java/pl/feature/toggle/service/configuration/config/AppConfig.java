package pl.feature.toggle.service.configuration.config;

import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.jooq.autoconfigure.SpringTransactionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
class AppConfig {

    @Bean
    DefaultConfiguration jooqConfiguration(DataSource ds, PlatformTransactionManager txManager) {
        var settings = new Settings()
                .withRenderQuotedNames(RenderQuotedNames.NEVER)
                .withRenderNameCase(RenderNameCase.LOWER)
                .withRenderSchema(false);

        var cfg = new DefaultConfiguration();
        cfg.setSQLDialect(SQLDialect.POSTGRES);
        cfg.setSettings(settings);

        var txAwareDs = new TransactionAwareDataSourceProxy(ds);
        cfg.setConnectionProvider(new DataSourceConnectionProvider(txAwareDs));

        cfg.setTransactionProvider(new SpringTransactionProvider(txManager));

        return cfg;
    }
}
