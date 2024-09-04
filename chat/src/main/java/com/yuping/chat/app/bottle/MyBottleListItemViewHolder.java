package com.yuping.chat.app.bottle;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yuping.chat.R;

public class MyBottleListItemViewHolder extends RecyclerView.ViewHolder {
    
    TextView textViewContent;
    TextView textUpdateTime;
    TextView textPikedCount;


    public MyBottleListItemViewHolder(View itemView) {
        super(itemView);
        textViewContent = itemView.findViewById(R.id.bottle_content);
        textUpdateTime = itemView.findViewById(R.id.bottle_update_time);
        textPikedCount = itemView.findViewById(R.id.bottle_item_picked_count);
    }

}
