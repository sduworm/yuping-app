package com.yuping.chat.app.bottle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yuping.chat.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class MyBottleListItemAdapter extends RecyclerView.Adapter<MyBottleListItemViewHolder> {

    private final List<MyBottleModel> dataList; // 假设你有一个MyDataModel类来存储每个列表项的数据

    public MyBottleListItemAdapter(List<MyBottleModel> dataList) {
        this.dataList = dataList;
    }

    public void updateDataList(List<MyBottleModel> newDataList) {
        // 更新数据列表
        this.dataList.clear();
        this.dataList.addAll(newDataList);

        // 通知适配器数据已更改，以便更新UI
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyBottleListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bottle_listitem, parent, false);
        return new MyBottleListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBottleListItemViewHolder holder, int position) {
        MyBottleModel data = dataList.get(position);
        Long bid = data.getBid();

        holder.textViewContent.setText(data.getContent());
        holder.textUpdateTime.setText(formatDateTime(data.getDateTime()));

        holder.textPikedCount.setText(String.valueOf(data.getPicked()));
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
        LocalDate today = LocalDate.now();
        LocalDate targetDate = t.toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        DateTimeFormatter dateYearFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if (targetDate.equals(today)) {
            // 同一天
            return t.format(formatter);
        } else if (targetDate.minusDays(1).equals(today)) {
            // 昨天
            return "昨天 " + t.format(formatter);
        } else if (targetDate.minusDays(2).equals(today)) {
            // 前天
            return "前天 " + t.format(formatter);
        } else {
            // 更早
            if (targetDate.getYear() == today.getYear()) {
                return t.format(dateFormatter);
            } else {
                // 去年或更早
                return t.format(dateYearFormatter);
            }
        }
    }
}
