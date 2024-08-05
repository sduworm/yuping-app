package com.yuping.chat.app.moment;

import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yuping.chat.R;

public class MomentListItemViewHolder extends RecyclerView.ViewHolder {

    ImageView imageViewAvatar;
    TextView textViewNickname;
    TextView textViewContent;
    HorizontalScrollView horizontalScrollView;

    public MomentListItemViewHolder(View itemView) {
        super(itemView);
        imageViewAvatar = itemView.findViewById(R.id.moment_avatar);
        textViewNickname = itemView.findViewById(R.id.moment_nickname);
        textViewContent = itemView.findViewById(R.id.moment_content);
    }

}
