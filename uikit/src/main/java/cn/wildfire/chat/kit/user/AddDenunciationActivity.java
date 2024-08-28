package cn.wildfire.chat.kit.user;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.yuping.custom.service.DenouncementService;

import cn.wildfire.chat.kit.R;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.widget.SimpleTextWatcher;

public class AddDenunciationActivity extends WfcBaseActivity {

    String targetUid;
    String displayName;

    EditText editText;

    private MenuItem menuItem;

    @Override
    protected int contentLayout() {
        return R.layout.yuping_denunciation_activity;
    }

    protected void bindViews() {
        super.bindViews();
        targetUid = getIntent().getStringExtra("targetUid");
        displayName = getIntent().getStringExtra("displayName");
        editText = findViewById(R.id.editText);
        editText.setHint("详细描述[" + displayName + "]的违规行为");
        editText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onEditTextChange();
            }
        });
    }

    @Override
    protected int menu() {
        return R.menu.user_feedback_suggestion;
    }

    @Override
    protected void afterMenus(Menu menu) {
        menuItem = menu.findItem(R.id.feedback);
        menuItem.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.feedback) {
            submitDenunciation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void onEditTextChange() {
        menuItem.setEnabled(!editText.getText().toString().trim().isEmpty());
    }

    private void submitDenunciation() {
        String content = editText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "举报原因不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        DenouncementService.addDenouncement(targetUid, content, new SimpleCallback<Void>() {
            @Override
            public void onUiSuccess(Void aVoid) {
                Toast.makeText(AddDenunciationActivity.this, "已收到您对[" + displayName + "]的举报\n感谢您对社区健康环境的维护", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onUiFailure(int code, String msg) {
                Toast.makeText(AddDenunciationActivity.this, "举报出现问题，请稍后重试" + code + " " + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
