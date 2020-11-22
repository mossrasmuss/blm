package com.illo.blm.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.cache.PrefixedKeyGenerator;
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
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
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
            createCache(cm, com.illo.blm.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.illo.blm.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.illo.blm.domain.User.class.getName());
            createCache(cm, com.illo.blm.domain.Authority.class.getName());
            createCache(cm, com.illo.blm.domain.User.class.getName() + ".authorities");
            createCache(cm, com.illo.blm.domain.Attribute.class.getName());
            createCache(cm, com.illo.blm.domain.Attribute.class.getName() + ".attributeValues");
            createCache(cm, com.illo.blm.domain.Category.class.getName());
            createCache(cm, com.illo.blm.domain.Category.class.getName() + ".attributes");
            createCache(cm, com.illo.blm.domain.Category.class.getName() + ".properties");
            createCache(cm, com.illo.blm.domain.Category.class.getName() + ".parents");
            createCache(cm, com.illo.blm.domain.Property.class.getName());
            createCache(cm, com.illo.blm.domain.Property.class.getName() + ".attributeValues");
            createCache(cm, com.illo.blm.domain.Language.class.getName());
            createCache(cm, com.illo.blm.domain.Country.class.getName());
            createCache(cm, com.illo.blm.domain.City.class.getName());
            createCache(cm, com.illo.blm.domain.Address.class.getName());
            createCache(cm, com.illo.blm.domain.AttributeValue.class.getName());
            createCache(cm, com.illo.blm.domain.Media.class.getName());
            createCache(cm, com.illo.blm.domain.Customer.class.getName());
            createCache(cm, com.illo.blm.domain.Customer.class.getName() + ".accounts");
            createCache(cm, com.illo.blm.domain.Business.class.getName());
            createCache(cm, com.illo.blm.domain.UserAccount.class.getName());
            createCache(cm, com.illo.blm.domain.UserAccount.class.getName() + ".salesProperties");
            createCache(cm, com.illo.blm.domain.Privilege.class.getName());
            createCache(cm, com.illo.blm.domain.SalesProperty.class.getName());
            createCache(cm, com.illo.blm.domain.PropertyPricing.class.getName());
            createCache(cm, com.illo.blm.domain.PropertyGroup.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
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
