/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package cn.wildfire.chat.kit.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.Config;
import cn.wildfire.chat.kit.R;
import cn.wildfire.chat.kit.WfcIntent;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.contact.OrganizationServiceViewModel;
import cn.wildfire.chat.kit.contact.newfriend.InviteFriendActivity;
import cn.wildfire.chat.kit.conversation.ConversationActivity;
import cn.wildfire.chat.kit.group.GroupMemberMessageHistoryActivity;
import cn.wildfire.chat.kit.mm.MMPreviewActivity;
import cn.wildfire.chat.kit.organization.OrganizationMemberListActivity;
import cn.wildfire.chat.kit.organization.model.EmployeeEx;
import cn.wildfire.chat.kit.organization.model.Organization;
import cn.wildfire.chat.kit.organization.model.OrganizationRelationship;
import cn.wildfire.chat.kit.qrcode.QRCodeActivity;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.DomainInfo;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.utils.WfcUtils;

public class UserInfoFragment extends Fragment {
    ImageView portraitImageView;
    ImageView portraitBigImageView;

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
    private OrganizationServiceViewModel organizationServiceViewModel;
    private ContactViewModel contactViewModel;

    private List<Organization> organizations;

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
        view.findViewById(R.id.portraitImageView).setOnClickListener(_v -> portrait());
        view.findViewById(R.id.chatButton).setOnClickListener(_v -> chat());
        view.findViewById(R.id.inviteButton).setOnClickListener(_v -> invite());
        view.findViewById(R.id.aliasOptionItemView).setOnClickListener(_v -> alias());
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        organizationServiceViewModel = new ViewModelProvider(this).get(OrganizationServiceViewModel.class);
        String selfUid = userViewModel.getUserId();

        if (selfUid.equals(userInfo.uid)) {
            // self
            notSelfOptions.setVisibility(View.GONE);
            bottomOptions.setVisibility(View.GONE);
            editItemView.setVisibility(View.VISIBLE);
        } else if (contactViewModel.isFriend(userInfo.uid)) {
            // friend
            notSelfOptions.setVisibility(View.VISIBLE);
            bottomOptions.setVisibility(View.VISIBLE);
            editItemView.setVisibility(View.GONE);
            inviteButton.setVisibility(View.GONE);
            uninviteButton.setVisibility(View.VISIBLE);
            aliasOptionItemView.setVisibility(View.VISIBLE);

        } else {
            // stranger
            notSelfOptions.setVisibility(View.VISIBLE);
            bottomOptions.setVisibility(View.VISIBLE);
            editItemView.setVisibility(View.GONE);
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

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    void portrait() {
        if (!TextUtils.isEmpty(userInfo.portrait)) {
            MMPreviewActivity.previewImage(getContext(), userInfo.portrait);
        } else {
            Toast.makeText(getActivity(), "用户未设置头像", Toast.LENGTH_SHORT).show();
        }
    }

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
