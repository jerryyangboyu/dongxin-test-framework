package pro.boyu.test;

import org.bouncycastle.util.test.Test;
import pro.boyu.dongxin.framework.annotations.Bean;
import pro.boyu.dongxin.framework.annotations.Service;

@Service
public class TestService {
    @Bean
    String getBeanMessage() {
        return "this is the bean message";
    }

    String getClient() {
        return "client";
    }

    public TestService() {

    }
}
