package com.dsk.kongapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.dsk.kongapiclientsdk.model.User;
import com.dsk.kongapiclientsdk.utils.SignUtils;

import java.util.HashMap;
import java.util.Map;

public class KongApiClient {

    private static final String GATEWAY_HOST="http://localhost:9000";

    private String accessKey;

    private String secretKey;

    public KongApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String,String> getHeaderMap(String body){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(3));
        hashMap.put("body",body);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()/1000));
        hashMap.put("sign", SignUtils.genSign(body,secretKey));
        return hashMap;
    }

    public String getNameByGet(String name){
        Map<String,Object> params=new HashMap<>();
        params.put("name",name);
        String result = HttpUtil.get(GATEWAY_HOST+"/api/name/get",params);
        return result;
    }


    public String getNameByPost(String name){
        Map<String,Object> params=new HashMap<>();
        params.put("name",name);
        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/post",params);
        return result;
    }

    public String getUserNameByPost(User user){
        String json= JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        String result=httpResponse.body();
        return result;
    }
}
