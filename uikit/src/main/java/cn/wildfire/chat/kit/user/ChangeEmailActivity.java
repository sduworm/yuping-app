package cn.wildfire.chat.kit.user;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Collections;

import cn.wildfire.chat.kit.R;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.widget.SimpleTextWatcher;
import cn.wildfirechat.model.ModifyMyInfoEntry;
import cn.wildfirechat.model.ModifyMyInfoType;
import cn.wildfirechat.model.UserInfo;

public class ChangeEmailActivity extends WfcBaseActivity {

    private MenuItem confirmMenuItem;
    EditText emailEditText;

    private UserViewModel userViewModel;
    private UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void bindViews() {
        super.bindViews();
        emailEditText = findViewById(R.id.emailEditText);
        emailEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                inputNewEmail();
            }
        });
    }

    @Override
    protected void afterViews() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userInfo = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        if (userInfo == null) {
            Toast.makeText(this, "用户不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
        initView();
    }

    @Override
    protected int contentLayout() {
        return R.layout.user_change_email_activity;
    }

    @Override
    protected int menu() {
        return R.menu.user_change_my_info;
    }

    @Override
    protected void afterMenus(Menu menu) {
        confirmMenuItem = menu.findItem(R.id.save);
        confirmMenuItem.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            changeEmail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        if (userInfo != null) {
            emailEditText.setText(userInfo.email);
        }
        emailEditText.setSelection(emailEditText.getText().toString().trim().length());
    }

    void inputNewEmail() {
        if (confirmMenuItem != null) {
            confirmMenuItem.setEnabled(!emailEditText.getText().toString().trim().isEmpty());
        }
    }


    private void changeEmail() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
            .content("修改中...")
            .progress(true, 100)
            .build();
        dialog.show();
        String email = emailEditText.getText().toString().trim();
        ModifyMyInfoEntry entry = new ModifyMyInfoEntry(ModifyMyInfoType.Modify_Email, email);
        userViewModel.modifyMyInfo(Collections.singletonList(entry)).observe(this, booleanOperateResult -> {
            if (booleanOperateResult.isSuccess()) {
                Toast.makeText(ChangeEmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangeEmailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
            finish();
        });
    }
}
