package com.yuping.custom.service;

import com.yuping.custom.config.AppServer;

import java.util.HashMap;
import java.util.Map;

import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;

public class DenouncementService {

    public static void addDenouncement(String targetUid, String content, SimpleCallback<Void> callback) {
        String url = AppServer.ADDRESS + "/denunciation/add";
        Map<String, Object> params = new HashMap<>();
        params.put("targetUid", targetUid);
        params.put("content", content);

        OKHttpHelper.post(url, params, callback);
    }
}
