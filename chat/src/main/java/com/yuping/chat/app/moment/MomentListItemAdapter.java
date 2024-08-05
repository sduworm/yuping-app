package com.yuping.chat.app.moment;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuping.chat.R;

import java.util.List;

public class MomentListItemAdapter extends RecyclerView.Adapter<MomentListItemViewHolder> {

    private List<MomentModel> dataList; // 假设你有一个MyDataModel类来存储每个列表项的数据

    public MomentListItemAdapter(List<MomentModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MomentListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_fragment_moment_listitem, parent, false);
        return new MomentListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MomentListItemViewHolder holder, int position) {
        MomentModel data = dataList.get(position);
        holder.imageViewAvatar.setImageURI(Uri.parse(data.getAvatar()));
        holder.textViewNickname.setText(data.getDisplayName());
        holder.textViewContent.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
