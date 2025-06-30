package gachi.chills.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    private val redisProperties: RedisProperties,
) {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(redisProperties.host, redisProperties.port)
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        return StringRedisTemplate().apply {
            connectionFactory = redisConnectionFactory()
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
        }
    }
}
