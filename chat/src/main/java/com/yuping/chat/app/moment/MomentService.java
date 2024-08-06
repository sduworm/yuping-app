package com.yuping.chat.app.moment;

import com.yuping.chat.app.AppService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.net.base.ResultWrapper;
import cn.wildfire.chat.kit.net.base.StatusResult;

public class MomentService {

    private static final MomentService Instance = new MomentService();

    public static MomentService Instance() {
        return Instance;
    }

    public void recentMoments(SimpleCallback<List<MomentModel>> callback) {
        String url = AppService.APP_SERVER_ADDRESS + "/moment/recentMoments";
        Map<String, String> params = new HashMap<>();
        params.put("page", String.valueOf(0));
        params.put("size", String.valueOf(10));
        OKHttpHelper.get(url, params, callback);
    }

    public void createMoment(String content, String images, SimpleCallback<StatusResult> callback) {
        String url = AppService.APP_SERVER_ADDRESS + "/moment/addMoment";
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("images", images);

        OKHttpHelper.post(url, params, callback);
    }
}
