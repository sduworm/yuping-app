package com.yuping.chat.app.moment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.yuping.chat.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.wildfire.chat.kit.net.SimpleCallback;
import cn.wildfire.chat.kit.net.base.ResultWrapper;


public class MomentFragment extends Fragment {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    private MomentViewModel momentViewModel;
    private SimpleCallback<List<MomentModel>> getMomentListCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment_moment, container, false);
        bindViews(v);
        bindEvents(v);
//        init();
        MomentService.Instance().recentMoments(this.getMomentListCallback);
        return v;
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.moment_list_item);
        swipeRefreshLayout = view.findViewById(R.id.moment_swipe_refresh_layout);
    }

    private void bindEvents(View view) {
        // 获取ViewModel实例
        momentViewModel = new ViewModelProvider(this).get(MomentViewModel.class);
        // 观察数据变化
        momentViewModel.getDataList().observe(getViewLifecycleOwner(), new Observer<List<MomentModel>>() {
            @Override
            public void onChanged(List<MomentModel> momentModels) {
                // 更新UI
                // 例如，使用RecyclerView显示数据
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行你的刷新逻辑
//                refreshData();
//                init();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        getMomentListCallback = new SimpleCallback<List<MomentModel>>() {
            @Override
            public void onUiSuccess(List<MomentModel> listResponseDTO) {
                Log.i("request Momenttt", "here" );
                MomentListItemAdapter adapter = new MomentListItemAdapter(listResponseDTO);
                recyclerView.setAdapter(adapter);
                Log.i("request Momenttt", "okok" );
            }

            @Override
            public void onUiFailure(int code, String msg) {
                Log.e("request Momenttt Error", "not ok :"+msg);
            }
        };

    }

    private void init() {

        List<MomentModel> dataList = new ArrayList<>();
        int index = new Random().nextInt(100);
        dataList.add(new MomentModel("刘备" + index, "Good Time", "https://picx.zhimg.com/v2-a256ec037bb09a417a038bf9c25b55d9_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("关羽", "星巴克（Starbucks）星倍醇 经典浓郁228ml*12罐 浓咖啡饮料礼盒", "https://pic1.zhimg.com/v2-aa9eccbcaa96420f62747af65c98a636_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("张飞", "因为人有冲天之志，非运不能自通。", "https://picx.zhimg.com/v2-2dab68d599f556d36876b674b4dff2ef_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("刘备" + index, "Good Time", "https://picx.zhimg.com/v2-a256ec037bb09a417a038bf9c25b55d9_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("关羽", "星巴克（Starbucks）星倍醇 经典浓郁228ml*12罐 浓咖啡饮料礼盒", "https://pic1.zhimg.com/v2-aa9eccbcaa96420f62747af65c98a636_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("张飞", "因为人有冲天之志，非运不能自通。", "https://picx.zhimg.com/v2-2dab68d599f556d36876b674b4dff2ef_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("刘备" + index, "Good Time", "https://picx.zhimg.com/v2-a256ec037bb09a417a038bf9c25b55d9_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("关羽", "星巴克（Starbucks）星倍醇 经典浓郁228ml*12罐 浓咖啡饮料礼盒", "https://pic1.zhimg.com/v2-aa9eccbcaa96420f62747af65c98a636_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("张飞", "因为人有冲天之志，非运不能自通。", "https://picx.zhimg.com/v2-2dab68d599f556d36876b674b4dff2ef_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("刘备" + index, "Good Time", "https://picx.zhimg.com/v2-a256ec037bb09a417a038bf9c25b55d9_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("关羽", "星巴克（Starbucks）星倍醇 经典浓郁228ml*12罐 浓咖啡饮料礼盒", "https://pic1.zhimg.com/v2-aa9eccbcaa96420f62747af65c98a636_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("张飞", "因为人有冲天之志，非运不能自通。", "https://picx.zhimg.com/v2-2dab68d599f556d36876b674b4dff2ef_xl.jpg?source=32738c0c"));
        MomentListItemAdapter adapter = new MomentListItemAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }


    public static String formatDateTime(String dateTimeString) {
        // 创建一个DateTimeFormatter用于解析ISO 8601格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        // 使用formatter将字符串解析为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        return formatDateTimeRelativeToNow(dateTime);
    }

    public static String formatDateTimeRelativeToNow(LocalDateTime t) {
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