package com.yuping.chat.app.myself;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yuping.chat.R;
import com.yuping.chat.app.setting.AccountActivity;
import com.yuping.chat.app.setting.SettingActivity;

import java.util.List;

import cn.wildfire.chat.kit.conversation.file.FileRecordListActivity;
import cn.wildfire.chat.kit.favorite.FavoriteListActivity;
import cn.wildfire.chat.kit.settings.MessageNotifySettingActivity;
import cn.wildfire.chat.kit.user.EditUserInfoActivity;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.model.UserInfo;

public class MyselfFragment extends Fragment {

    LinearLayout meLinearLayout;
    ImageView portraitImageView;
    TextView displayNameTextView;
    TextView nameIdTextView;

    OptionItemView bottlesOptionItem;
    OptionItemView editItemView;
    OptionItemView settingOptionItem;

    private UserViewModel userViewModel;
    private UserInfo userInfo;

    private final Observer<List<UserInfo>> userInfoLiveDataObserver = new Observer<List<UserInfo>>() {
        @Override
        public void onChanged(@Nullable List<UserInfo> userInfos) {
            if (userInfos == null) {
                return;
            }
            for (UserInfo info : userInfos) {
                if (info.uid.equals(userViewModel.getUserId())) {
                    userInfo = info;
                    updateUserInfo(userInfo);
                    break;
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_myself, container, false);
        bindViews(view);
        bindEvents(view);
        init();
        return view;
    }

    private void bindViews(View view) {
        meLinearLayout = view.findViewById(R.id.myselfLinearLayout);
        portraitImageView = view.findViewById(R.id.myselfAvatarImageView);
        displayNameTextView = view.findViewById(R.id.myselfDisplayName);
        nameIdTextView = view.findViewById(R.id.myselfNameIdTextView);
        bottlesOptionItem = view.findViewById(R.id.myselfBottlesOptionItemView);
        settingOptionItem = view.findViewById(R.id.myselfSettingOptionItemView);
        editItemView = view.findViewById(cn.wildfire.chat.kit.R.id.editItemView);
    }

    private void bindEvents(View view) {
        view.findViewById(R.id.myselfLinearLayout).setOnClickListener(v -> showMyInfo());
        view.findViewById(R.id.myselfAccountOptionItemView).setOnClickListener(v -> account());
        view.findViewById(R.id.myselfSettingOptionItemView).setOnClickListener(v -> setting());
        view.findViewById(R.id.myselfBottlesOptionItemView).setOnClickListener(v -> msgNotifySetting());
        editItemView.setOnClickListener(_v -> edit());
    }

    private void updateUserInfo(UserInfo userInfo) {
        RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.avatar_def);
        Glide.with(this)
            .load(userInfo.portrait)
            .apply(options)
            .into(portraitImageView);
        displayNameTextView.setText(userInfo.displayName);
        nameIdTextView.setText("ID: " + userInfo.name);
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUserInfoAsync(userViewModel.getUserId(), true)
            .observe(getViewLifecycleOwner(), info -> {
                userInfo = info;
                if (userInfo != null) {
                    updateUserInfo(userInfo);
                }
            });
        userViewModel.userInfoLiveData().observeForever(userInfoLiveDataObserver);
    }

    void edit() {
        Intent intent = new Intent(getActivity(), EditUserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userViewModel.userInfoLiveData().removeObserver(userInfoLiveDataObserver);
    }

    void showMyInfo() {
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    void fav() {
        Intent intent = new Intent(getActivity(), FavoriteListActivity.class);
        startActivity(intent);
    }

    void account() {
        Intent intent = new Intent(getActivity(), AccountActivity.class);
        startActivity(intent);
    }

    void files() {
        Intent intent = new Intent(getActivity(), FileRecordListActivity.class);
        startActivity(intent);
    }


    void theme() {
        SharedPreferences sp = getActivity().getSharedPreferences("wfc_kit_config", Context.MODE_PRIVATE);
        boolean darkTheme = sp.getBoolean("darkTheme", true);
        new MaterialDialog.Builder(getContext()).items(R.array.themes).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View v, int position, CharSequence text) {
                if (position == 0 && darkTheme) {
                    sp.edit().putBoolean("darkTheme", false).apply();
                    restart();
                    return;
                }
                if (position == 1 && !darkTheme) {
                    sp.edit().putBoolean("darkTheme", true).apply();
                    restart();
                }
            }
        }).show();
    }

    private void restart() {
        Intent i = getActivity().getApplicationContext().getPackageManager().getLaunchIntentForPackage(getActivity().getApplicationContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    void setting() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    void msgNotifySetting() {
        Intent intent = new Intent(getActivity(), MessageNotifySettingActivity.class);
        startActivity(intent);
    }
}
