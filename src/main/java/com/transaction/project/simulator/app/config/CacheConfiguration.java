package com.transaction.project.simulator.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.transaction.project.simulator.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.transaction.project.simulator.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.transaction.project.simulator.app.domain.User.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Authority.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.transaction.project.simulator.app.domain.MessageTypeIndicator.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.MessageTypeIndicator.class.getName() + ".messageIsoConfigs");
            createCache(cm, com.transaction.project.simulator.app.domain.MessageIsoConfig.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.MessageIsoConfig.class.getName() + ".messageFieldsConfigs");
            createCache(cm, com.transaction.project.simulator.app.domain.MessageFieldsConfig.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.MessageFieldType.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.MessageFieldType.class.getName() + ".messageFieldsConfigs");
            createCache(cm, com.transaction.project.simulator.app.domain.FieldType.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.FieldType.class.getName() + ".messageFieldTypes");
            createCache(cm, com.transaction.project.simulator.app.domain.Currency.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Currency.class.getName() + ".accountBanks");
            createCache(cm, com.transaction.project.simulator.app.domain.Acquirer.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Acquirer.class.getName() + ".advice");
            createCache(cm, com.transaction.project.simulator.app.domain.Acquirer.class.getName() + ".messageIsoConfigs");
            createCache(cm, com.transaction.project.simulator.app.domain.Merchant.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Merchant.class.getName() + ".advice");
            createCache(cm, com.transaction.project.simulator.app.domain.Issuer.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Issuer.class.getName() + ".cards");
            createCache(cm, com.transaction.project.simulator.app.domain.Advice.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.AccountBank.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.CardType.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.CardType.class.getName() + ".cards");
            createCache(cm, com.transaction.project.simulator.app.domain.TransactionType.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.TransactionType.class.getName() + ".transactions");
            createCache(cm, com.transaction.project.simulator.app.domain.Card.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.Transaction.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.DebitCard.class.getName());
            createCache(cm, com.transaction.project.simulator.app.domain.CreditCard.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
