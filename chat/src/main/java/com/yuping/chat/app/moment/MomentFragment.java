package com.yuping.chat.app.moment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yuping.chat.R;

import java.util.ArrayList;
import java.util.List;


public class MomentFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment_moment, container, false);
        bindViews(v);
        bindEvents(v);
        init();
        return v;
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.moment_list_item);
    }

    private void bindEvents(View view) {

    }

    private void init() {
        List<MomentModel> dataList = new ArrayList<>();
        dataList.add(new MomentModel("刘备", "Good Time", "https://picx.zhimg.com/v2-a256ec037bb09a417a038bf9c25b55d9_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("关羽", "星巴克（Starbucks）星倍醇 经典浓郁228ml*12罐 浓咖啡饮料礼盒", "https://pic1.zhimg.com/v2-aa9eccbcaa96420f62747af65c98a636_xl.jpg?source=32738c0c"));
        dataList.add(new MomentModel("张飞", "因为人有冲天之志，非运不能自通。", "https://picx.zhimg.com/v2-2dab68d599f556d36876b674b4dff2ef_xl.jpg?source=32738c0c"));
        MomentListItemAdapter adapter = new MomentListItemAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }
}