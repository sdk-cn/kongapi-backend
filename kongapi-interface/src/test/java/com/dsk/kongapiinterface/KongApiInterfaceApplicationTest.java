package com.dsk.kongapiinterface;

import com.dsk.kongapiclientsdk.client.KongApiClient;
import com.dsk.kongapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class KongApiInterfaceApplicationTest {

    @Resource
    private KongApiClient kongApiClient;

    @Test
    void contextLoads(){
        User user = new User();
        user.setUserName("kongkong");
        String result = kongApiClient.getUserNameByPost(user);
        System.out.println(result);

    }
}
