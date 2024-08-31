package com.yuping.chat.app.setting;

import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yuping.chat.R;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.contact.newfriend.SearchUserActivity;
import cn.wildfire.chat.kit.widget.SimpleTextWatcher;

public class FeedbackActivity extends WfcBaseActivity {
    EditText suggestionText;
    TextView backdoor;

    private MenuItem menuItem;

    protected void bindViews() {
        super.bindViews();
        backdoor =findViewById(R.id.backdoor);
        backdoor.setOnClickListener(e -> showSearchIntent());
        suggestionText = findViewById(R.id.suggestionText);
        suggestionText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                onTextChange();
            }
        });
    }

    @Override
    protected int contentLayout() {
        return R.layout.setting_feedback_suggestion_activity;
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
            doFeedback();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void showSearchIntent() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }

    void onTextChange() {
        menuItem.setEnabled(!suggestionText.getText().toString().trim().isEmpty());
    }

    private void doFeedback() {
        String content = suggestionText.getText().toString().trim();
        SettingService.Instance().feedbackSuggestion(content, null);
        Toast.makeText(FeedbackActivity.this, "提交成功，感谢您的反馈", Toast.LENGTH_SHORT).show();
        finish();
    }
}
