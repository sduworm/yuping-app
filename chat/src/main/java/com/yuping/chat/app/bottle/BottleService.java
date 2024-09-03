package com.yuping.chat.app.bottle;

import com.yuping.chat.app.AppService;

import java.util.HashMap;
import java.util.Map;

import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.net.base.StatusResult;

public class BottleService {

    private static final BottleService Instance = new BottleService();

    public static BottleService Instance() {
        return Instance;
    }

    public void newBottle(String content, String images, SimpleCallback<StatusResult> callback) {
        String url = AppService.APP_SERVER_ADDRESS + "/bottle/add";
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);

        OKHttpHelper.post(url, params, callback);
    }

}
