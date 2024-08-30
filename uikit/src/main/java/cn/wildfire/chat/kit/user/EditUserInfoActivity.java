package cn.wildfire.chat.kit.user;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;

import cn.wildfire.chat.kit.R;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.third.utils.ImageUtils;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.model.UserInfo;

public class EditUserInfoActivity extends WfcBaseActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 100;

    private UserViewModel userViewModel;
    private UserInfo userInfo;

    ImageView portraitImageView;
    TextView emailTextView;

    View editAvatar;
    View emailView;

    OptionItemView displayName;
    OptionItemView gender;
    OptionItemView nameId;
    OptionItemView phone;
    OptionItemView editEmail;

    @Override
    protected int contentLayout() {
        return R.layout.yuping_edit_user_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bindViews() {
        super.bindViews();
        portraitImageView = findViewById(R.id.portraitImageView);
        emailTextView = findViewById(R.id.emailTextView);
        editAvatar = findViewById(R.id.editAvatar);
        displayName = findViewById(R.id.displayName);
        gender = findViewById(R.id.gender);
        nameId = findViewById(R.id.nameId);
        phone = findViewById(R.id.phone);
        editEmail = findViewById(R.id.editEmail);
        emailView = findViewById(R.id.emailView);

        editAvatar.setOnClickListener(_v -> portrait());
        displayName.setOnClickListener(_v -> changeDisplayName());
        editEmail.setOnClickListener(_v -> changeEmail());
        emailView.setOnClickListener(_v -> changeEmail());
    }

    private void changeDisplayName() {
        Intent intent = new Intent(this, ChangeMyNameActivity.class);
        startActivity(intent);
    }

    private void changeEmail() {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        startActivity(intent);
    }

    void portrait() {
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            };
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, 100);
                    return;
                }
            }
        }
        ImagePicker.picker().pick(this, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images == null || images.isEmpty()) {
                Toast.makeText(this, "更新头像失败: 选取文件失败 ", Toast.LENGTH_SHORT).show();
                return;
            }
            File thumbImgFile = ImageUtils.genThumbImgFile(images.get(0).path);
            if (thumbImgFile == null) {
                Toast.makeText(this, "更新头像失败: 生成缩略图失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String imagePath = thumbImgFile.getAbsolutePath();

            MutableLiveData<OperateResult<Boolean>> result = userViewModel.updateUserPortrait(imagePath);
            result.observe(this, booleanOperateResult -> {
                if (booleanOperateResult.isSuccess()) {
                    Toast.makeText(this, "更新头像成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "更新头像失败: " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void afterViews() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        initUserInfo();

        if (userInfo == null) {
            Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        initView();
    }

    private void initView() {
        RequestBuilder<Drawable> avatar = Glide.with(this)
                .load(userInfo.portrait);
        avatar.into(portraitImageView);
        emailTextView.setText(userInfo.email);
        displayName.setDesc(userInfo.displayName);
        gender.setDesc(userInfo.gender == 0 ? "女":"男");
        nameId.setDesc(userInfo.name);
        phone.setDesc(userInfo.mobile);
    }

    void initUserInfo() {
        userInfo = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        userViewModel.userInfoLiveData().observe(this, userInfos -> {
            for (UserInfo info : userInfos) {
                if (userInfo.uid.equals(info.uid)) {
                    userInfo = info;
                    initView();
                    break;
                }
            }
        });
    }
}
