package redisandkfka.demo.util.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.event.EventBus;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Log
public class RedisEventListener {






    @Bean(destroyMethod = "shutdown")
    ClientResources clientResources() {
        return DefaultClientResources.create();
    }
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean(destroyMethod = "shutdown")
    RedisClient redisClient(ClientResources clientResources) {

        RedisClient redisClient= RedisClient.create(clientResources, RedisURI.create(redisHost, redisPort));


        return redisClient;
    }



    @Bean(destroyMethod = "close")
    StatefulRedisConnection<String, String> connection(RedisClient redisClient) {
        EventBus eventBus = redisClient.getResources().eventBus();

        eventBus.get().subscribe(e -> log.info("redisSparkLoglettuceEventBus:"+e+eventBus.get().toString()));



        return redisClient.connect();
    }


}
