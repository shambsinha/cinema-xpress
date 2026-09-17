package com.cinemaxpress.config;

import java.time.Duration;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration(proxyBeanMethods = false)
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        ObjectMapper cacheMapper = new ObjectMapper();
        cacheMapper.activateDefaultTyping(
            BasicPolymorphicTypeValidator.builder().allowIfBaseType(Object.class).build(), 
            ObjectMapper.DefaultTyping.NON_FINAL
        );

        cacheMapper.addMixIn(PageImpl.class, PageImplMixin.class);
        cacheMapper.addMixIn(Page.class, PageImplMixin.class);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(cacheMapper);

        RedisCacheConfiguration baseConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(10))
                .disableCachingNullValues()
                .prefixCacheNameWith("cinema_xpress:")
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(baseConfig)
                .withCacheConfiguration("moviesCache", baseConfig)
                .withCacheConfiguration("theatresCache", baseConfig)
                .build();
    }

    @JsonDeserialize(as = CustomPageImpl.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private abstract static class PageImplMixin<T> { }

    private static class CustomPageImpl<T> extends PageImpl<T> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public CustomPageImpl(@JsonProperty("content") List<T> content,
                              @JsonProperty("number") int number,
                              @JsonProperty("size") int size,
                              @JsonProperty("totalElements") Long totalElements) {
            super(content, PageRequest.of(number, size == 0 ? 1 : size), totalElements == null ? 0 : totalElements);
        }
    }
}
