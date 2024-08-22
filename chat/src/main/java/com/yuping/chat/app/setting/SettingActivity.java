/*
 * Copyright (c) 2024 BottleDestiny. All rights reserved.
 */

package com.yuping.chat.app.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.webkit.CookieManager;
import android.webkit.WebStorage;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.yuping.chat.R;
import com.yuping.chat.app.main.SplashActivity;

import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.Config;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.net.OKHttpHelper;
import cn.wildfire.chat.kit.settings.blacklist.BlacklistListActivity;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.GeneralCallback;

public class SettingActivity extends WfcBaseActivity {
    private final int REQUEST_IGNORE_BATTERY_CODE = 100;
    private SwitchMaterial switchAddFriendNeedVerify;

    protected void bindEvents() {
        super.bindEvents();
        findViewById(R.id.exitOptionItemView).setOnClickListener(v -> exit());
        findViewById(R.id.batteryOptionItemView).setOnClickListener(v -> batteryOptimize());
        findViewById(cn.wildfire.chat.kit.R.id.blacklistOptionItemView).setOnClickListener(v -> blacklistSettings());

        this.switchAddFriendNeedVerify.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ChatManager.Instance().setAddFriendNeedVerify(isChecked, new GeneralCallback() {
                @Override
                public void onSuccess() {
                    // do nothing
                }

                @Override
                public void onFail(int errorCode) {
                    if (!isFinishing()) {
                        Toast.makeText(SettingActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }

    protected void bindViews() {
        super.bindViews();
        this.switchAddFriendNeedVerify = findViewById(cn.wildfire.chat.kit.R.id.switchAddFriendNeedVerify);
        this.switchAddFriendNeedVerify.setChecked(ChatManager.Instance().isAddFriendNeedVerify());
    }

    @Override
    protected int contentLayout() {
        return R.layout.setting_activity;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_IGNORE_BATTERY_CODE:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "允许野火IM后台运行，更能保证消息的实时性", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    void blacklistSettings() {
        Intent intent = new Intent(this, BlacklistListActivity.class);
        startActivity(intent);
    }

    void exit() {
        //不要清除session，这样再次登录时能够保留历史记录。如果需要清除掉本地历史记录和服务器信息这里使用true
        ChatManagerHolder.gChatManager.disconnect(true, false);
        SharedPreferences sp = getSharedPreferences(Config.SP_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        sp = getSharedPreferences("moment", Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        OKHttpHelper.clearCookies();

        WebStorage.getInstance().deleteAllData();
        CookieManager.getInstance().removeAllCookies(null);
        CookieManager.getInstance().flush();

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("BatteryLife")
    void batteryOptimize() {
        try {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
            } else {
                Toast.makeText(this, "已忽略电池优化，请允许遇瓶APP后台运行，更能保证消息的实时性", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
