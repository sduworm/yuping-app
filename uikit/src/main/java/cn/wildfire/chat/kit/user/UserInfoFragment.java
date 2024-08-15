/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package cn.wildfire.chat.kit.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;

import cn.wildfire.chat.kit.R;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.contact.newfriend.InviteFriendActivity;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.mm.MMPreviewActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;

public class UserInfoFragment extends Fragment {
    ImageView portraitImageView;
    ImageView portraitBigImageView;
    ImageView customToolbarEditIcon;

    TextView titleTextView;
    TextView companyTextView;
    TextView accountTextView;

    View chatButton;
    View bottomOptions;
    View notSelfOptions;

    Button inviteButton;
    Button uninviteButton;
    Button followButton;
    Button unfollowButton;
    OptionItemView aliasOptionItemView;
    OptionItemView editItemView;

    private UserInfo userInfo;
    private String groupId;
    private UserViewModel userViewModel;
    private ContactViewModel contactViewModel;

    public static UserInfoFragment newInstance(UserInfo userInfo, String groupId) {
        UserInfoFragment fragment = new UserInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("userInfo", userInfo);
        if (!TextUtils.isEmpty(groupId)) {
            args.putString("groupId", groupId);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        userInfo = args.getParcelable("userInfo");
        groupId = args.getString("groupId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_info_fragment, container, false);
        bindViews(view);
        bindEvents(view);
        init();
        return view;
    }

    private void bindViews(View view) {
        portraitImageView = view.findViewById(R.id.portraitImageView);
        portraitBigImageView = view.findViewById(R.id.portraitBigImageView);
        customToolbarEditIcon = view.findViewById(R.id.customToolbarEditIcon);
        titleTextView = view.findViewById(R.id.titleTextView);
        accountTextView = view.findViewById(R.id.accountTextView);
        companyTextView = view.findViewById(R.id.companyTextView);
        chatButton = view.findViewById(R.id.chatButton);
        inviteButton = view.findViewById(R.id.inviteButton);
        uninviteButton = view.findViewById(R.id.uninviteButton);
        followButton = view.findViewById(R.id.followButton);
        unfollowButton = view.findViewById(R.id.unfollowButton);
        aliasOptionItemView = view.findViewById(R.id.aliasOptionItemView);
        editItemView = view.findViewById(R.id.editItemView);
        notSelfOptions = view.findViewById(R.id.notSelfOptions);
        bottomOptions = view.findViewById(R.id.bottomOptions);


    }

    private void bindEvents(View view) {
        portraitImageView.setOnClickListener(_v -> portrait());
        chatButton.setOnClickListener(_v -> chat());
        inviteButton.setOnClickListener(_v -> invite());
        uninviteButton.setOnClickListener(_v -> deleteFriend(view));
        aliasOptionItemView.setOnClickListener(_v -> alias());
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);

        String selfUid = userViewModel.getUserId();

        if (selfUid.equals(userInfo.uid)) {
            // self
            notSelfOptions.setVisibility(View.GONE);
            bottomOptions.setVisibility(View.GONE);
            editItemView.setVisibility(View.VISIBLE);
            customToolbarEditIcon.setVisibility(View.VISIBLE);
        } else if (contactViewModel.isFriend(userInfo.uid)) {
            // friend
            notSelfOptions.setVisibility(View.VISIBLE);
            bottomOptions.setVisibility(View.VISIBLE);
            editItemView.setVisibility(View.GONE);
            customToolbarEditIcon.setVisibility(View.GONE);

            inviteButton.setVisibility(View.GONE);
            uninviteButton.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.VISIBLE);

        } else {
            // stranger
            notSelfOptions.setVisibility(View.VISIBLE);
            bottomOptions.setVisibility(View.VISIBLE);
            editItemView.setVisibility(View.GONE);
            customToolbarEditIcon.setVisibility(View.GONE);

            uninviteButton.setVisibility(View.GONE);
            inviteButton.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.GONE);
        }

        unfollowButton.setVisibility(contactViewModel.isFav(userInfo.uid) ? View.VISIBLE : View.GONE);
        followButton.setVisibility(contactViewModel.isFav(userInfo.uid) ? View.GONE : View.VISIBLE);

        setUserInfo(userInfo);
        userViewModel.userInfoLiveData().observe(getViewLifecycleOwner(), userInfos -> {
            for (UserInfo info : userInfos) {
                if (userInfo.uid.equals(info.uid)) {
                    userInfo = info;
                    setUserInfo(info);
                    break;
                }
            }
        });
        userViewModel.getUserInfo(userInfo.uid, true);

    }

    private void setUserInfo(UserInfo userInfo) {
        userInfo = ChatManager.Instance().getUserInfo(userInfo.uid, groupId, false);
        RequestBuilder<Drawable> avatar = Glide.with(this)
                .load(userInfo.portrait);
        avatar.into(portraitImageView);
        avatar.into(portraitBigImageView);

        if (!TextUtils.isEmpty(userInfo.friendAlias)) {
            titleTextView.setText(userInfo.friendAlias + "(" + userInfo.displayName + ")");
        } else {
            titleTextView.setText(userInfo.displayName);
        }

        accountTextView.setText("ID: " + userInfo.name);

        if (!TextUtils.isEmpty(userInfo.company)) {
            companyTextView.setText(userInfo.company);
        } else {
            companyTextView.setText("没有个性签名");
        }


    }

    void chat() {
        Intent intent = new Intent(getActivity(), ConversationActivity.class);
        Conversation conversation = new Conversation(Conversation.ConversationType.Single, userInfo.uid, 0);
        intent.putExtra("conversation", conversation);
        startActivity(intent);
        getActivity().finish();
    }


    void alias() {
        String selfUid = userViewModel.getUserId();
        if (selfUid.equals(userInfo.uid)) {
            Intent intent = new Intent(getActivity(), ChangeMyNameActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SetAliasActivity.class);
            intent.putExtra("userId", userInfo.uid);
            startActivity(intent);
        }
    }

    void portrait() {
        if (!TextUtils.isEmpty(userInfo.portrait)) {
            MMPreviewActivity.previewImage(getContext(), userInfo.portrait);
        } else {
            Toast.makeText(getActivity(), "用户未设置头像", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteFriend(View view){
        Snackbar snackbar = Snackbar.make(view, "确定删除好友吗？", Snackbar.LENGTH_LONG);

        snackbar.setAction("取消", v ->  snackbar.dismiss());

        snackbar.setAction("确定", v -> contactViewModel.deleteFriend(userInfo.uid).observe(
                requireActivity(), booleanOperateResult -> {
                    if (booleanOperateResult.isSuccess()) {
                        Intent intent = new Intent(UIUtils.getPackageName() + ".main");
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "delete friend error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                    }
                }
        ));

        snackbar.show();
    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
//
//
//        else if (item.getItemId() == R.id.addFriend) {
//            Intent intent = new Intent(this, InviteFriendActivity.class);
//            intent.putExtra("userInfo", userInfo);
//            startActivity(intent);
//            return true;
//        }
//        else if (item.getItemId() == R.id.addBlacklist) {
//            contactViewModel.setBlacklist(userInfo.uid, true).observe(
//                    this, booleanOperateResult -> {
//                        if (booleanOperateResult.isSuccess()) {
//                            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
//                            invalidateOptionsMenu();
//                        } else {
//                            Toast.makeText(this, "add blacklist error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//            return true;
//        }
//        else if (item.getItemId() == R.id.removeBlacklist) {
//            contactViewModel.setBlacklist(userInfo.uid, false).observe(
//                    this, booleanOperateResult -> {
//                        if (booleanOperateResult.isSuccess()) {
//                            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
//                            invalidateOptionsMenu();
//                        } else {
//                            Toast.makeText(this, "remove blacklist error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//            return true;
//        }
//        else if (item.getItemId() == R.id.setAlias) {
//            Intent intent = new Intent(this, SetAliasActivity.class);
//            intent.putExtra("userId", userInfo.uid);
//            startActivity(intent);
//            return true;
//        }
//        else if (item.getItemId() == R.id.setFav) {
//            contactViewModel.setFav(userInfo.uid, true).observe(
//                    this, booleanOperateResult -> {
//                        if (booleanOperateResult.isSuccess()) {
//                            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
//                            invalidateOptionsMenu();
//                        } else {
//                            Toast.makeText(this, "set fav error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//            return true;
//
//        }
//        else if (item.getItemId() == R.id.removeFav) {
//            contactViewModel.setFav(userInfo.uid, false).observe(
//                    this, booleanOperateResult -> {
//                        if (booleanOperateResult.isSuccess()) {
//                            Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
//                            invalidateOptionsMenu();
//                        } else {
//                            Toast.makeText(this, "remove fav error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            );
//            return true;
//        }
//        else if (item.getItemId() == R.id.setName) {
//            Intent intent = new Intent(this, SetNameActivity.class);
//            intent.putExtra("userInfo", userInfo);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images == null || images.isEmpty()) {
                Toast.makeText(getActivity(), "更新头像失败: 选取文件失败 ", Toast.LENGTH_SHORT).show();
                return;
            }
            File thumbImgFile = ImageUtils.genThumbImgFile(images.get(0).path);
            if (thumbImgFile == null) {
                Toast.makeText(getActivity(), "更新头像失败: 生成缩略图失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String imagePath = thumbImgFile.getAbsolutePath();

            MutableLiveData<OperateResult<Boolean>> result = userViewModel.updateUserPortrait(imagePath);
            result.observe(this, booleanOperateResult -> {
                if (booleanOperateResult.isSuccess()) {
                    Toast.makeText(getActivity(), "更新头像成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "更新头像失败: " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void invite() {
        Intent intent = new Intent(getActivity(), InviteFriendActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
        getActivity().finish();
    }
}
