package com.yuping.chat.app.moment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yuping.chat.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class MomentListItemAdapter extends RecyclerView.Adapter<MomentListItemViewHolder> {

    private final List<MomentModel> dataList; // 假设你有一个MyDataModel类来存储每个列表项的数据

    public MomentListItemAdapter(List<MomentModel> dataList) {
        this.dataList = dataList;
    }

    public void updateDataList(List<MomentModel> newDataList) {
        // 更新数据列表
        this.dataList.clear();
        this.dataList.addAll(newDataList);

        // 通知适配器数据已更改，以便更新UI
        notifyDataSetChanged();
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
        holder.textViewNickname.setText(data.getDisplayName());
        holder.textViewContent.setText(data.getContent());
        holder.textUpdateTime.setText(formatDateTime(data.getUpdateDateTime()));
        Glide.with(holder.itemView).load(data.getAvatar()).into(holder.imageViewAvatar);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private static String formatDateTime(String dateTimeString) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return "";
        }
        // 创建一个DateTimeFormatter用于解析ISO 8601格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // 使用formatter将字符串解析为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return formatDateTimeRelativeToNow(dateTime);
    }

    private static String formatDateTimeRelativeToNow(LocalDateTime t) {
        LocalDateTime now = LocalDateTime.now();

        long daysDiff = ChronoUnit.DAYS.between(t, now);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        DateTimeFormatter dateYearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (daysDiff == 0) {
            // 同一天
            return t.format(formatter);
        } else if (daysDiff == 1) {
            // 昨天
            return "昨天 " + t.format(formatter);
        } else if (daysDiff == 2) {
            // 前天
            return "前天 " + t.format(formatter);
        } else if (daysDiff > 2) {
            // 更早，但不是去年
            if (t.getYear() == now.getYear()) {
                return t.format(dateFormatter);
            } else {
                // 去年或更早
                return t.format(dateYearFormatter);
            }
        }

        // 注意：理论上，daysDiff 应该是非负的，因为我们是从过去到现在比较。
        // 但为了代码的健壮性，这里仍然保留了对负值的检查（尽管它不会执行）。
        return "未知日期";
    }
}
