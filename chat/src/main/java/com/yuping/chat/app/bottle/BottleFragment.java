package com.yuping.chat.app.bottle;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.yuping.chat.R;

import cn.wildfire.chat.kit.user.SetAliasActivity;


public class BottleFragment extends Fragment {

    View throwButton;
    View pickButton;

    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment_bottle, container, false);
        bindViews(v);
        bindEvents();
        init();
        return v;
    }

    private void bindViews(View view) {
        throwButton = view.findViewById(R.id.btn_throw_bottle);
        pickButton = view.findViewById(R.id.btn_pick_bottle);
        bindDialog();
    }

    private void bindEvents() {
        throwButton.setOnClickListener(e -> dialog.show());

        bindDialogEvent();
    }

    private void init() {
    }

    private void newBottle(String content) {
        BottleService.Instance().newBottle(content, null, null);
    }

    private void bindDialog() {
        // 实例化Dialog，使用Theme.AppCompat.Dialog.Alert，这样它会显示为一个标准的对话框样式
        dialog = new Dialog(this.requireContext(), R.style.Theme_AppCompat_Dialog_Alert);
        // 设置Dialog的ContentView为你定义的布局
        dialog.setContentView(R.layout.fragment_bottle_add_dialog); // 注意替换为你的布局文件ID
    }

    private void bindDialogEvent() {
        EditText bottleText = dialog.findViewById(R.id.new_bottle_text);
        Button submitButton = dialog.findViewById(R.id.submit_new_bottle);

        bottleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submitButton.setEnabled(!s.toString().isEmpty());
                submitButton.setBackgroundTintList(s.toString().isEmpty() ? ContextCompat.getColorStateList(requireActivity(), R.color.gray) : ContextCompat.getColorStateList(requireActivity(), R.color.colorPrimary));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        submitButton.setOnClickListener(e -> {
            String bottleContent = bottleText.getText().toString();
            if (bottleContent.isEmpty()){
                return;
            }
            newBottle(bottleContent);
            Toast.makeText(getActivity(), "瓶子已飘向远方", Toast.LENGTH_SHORT).show();
            dialog.hide();
            bottleText.setText("");
        });
    }
}