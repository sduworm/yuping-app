/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package cn.wildfire.chat.kit.contact.viewholder.footer;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cn.wildfire.chat.kit.*;
import cn.wildfire.chat.kit.contact.UserListAdapter;
import cn.wildfire.chat.kit.contact.model.ContactCountFooterValue;

public class ContactCountViewHolder extends FooterViewHolder<ContactCountFooterValue> {
    TextView countTextView;
    private final UserListAdapter adapter;

    public ContactCountViewHolder(Fragment fragment, UserListAdapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        this.adapter = adapter;
        bindViews(itemView);
    }

    private void bindViews(View itemView) {
        countTextView = itemView.findViewById(R.id.countTextView);
    }

    @Override
    public void onBind(ContactCountFooterValue contactCountFooterValue) {
        int count = adapter.getContactCount();
        if (count == 0) {
            countTextView.setText("没有好友");
        } else {
            countTextView.setText(count + "位好友");
        }
    }
}
