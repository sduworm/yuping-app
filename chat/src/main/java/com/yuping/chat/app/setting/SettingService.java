package com.yuping.chat.app.setting;

import com.yuping.chat.app.AppService;
import com.yuping.chat.app.moment.MomentModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.net.base.StatusResult;

public class SettingService {

    private static final SettingService Instance = new SettingService();

    public static SettingService Instance() {
        return Instance;
    }

    public void feedbackSuggestion(String content, SimpleCallback<StatusResult> callback) {
        String url = AppService.APP_SERVER_ADDRESS + "/feedback/add";
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);

        OKHttpHelper.post(url, params, callback);
    }
}
