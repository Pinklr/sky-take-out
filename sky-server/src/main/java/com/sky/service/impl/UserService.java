package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.utils.HttpClientUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements com.sky.service.UserService {

    private final String SRL = "https://api.weixin.qq.com/sns/jscode2session";


    @Autowired
    private UserMapper userMapper;


    @Autowired
    private WeChatProperties weChatProperties;
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        String code = userLoginDTO.getCode();
        //获取openid
        String openid = this.getOpenid(code);
        //在数据空值中查询openid，如果存在，则直接返回，否则，创建一个新的用户
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);

        }
        return user;


    }

    private String getOpenid(String code) {

        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(SRL, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }

}
