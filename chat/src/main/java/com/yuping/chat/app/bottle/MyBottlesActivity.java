package com.yuping.chat.app.bottle;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yuping.chat.R;

import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.net.SimpleCallback;


public class MyBottlesActivity extends WfcBaseActivity {
    private final static int PAGE_SIZE = 20;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    private SimpleCallback<List<MyBottleModel>> bottleListRequestCallback;
    private int currentPage = 0;


    // Adapter 用来做数据和界面显示的映射
    // 好处是支持数据变化和界面变化的绑定
    private MyBottleListItemAdapter adapter;


    @Override
    protected int contentLayout() {
        return R.layout.bottle_list_activity;
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        init();
    }

    @Override
    protected void bindViews() {
        super.bindViews();
        recyclerView = findViewById(R.id.bottle_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyBottleListItemAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.bottle_swipe_refresh_layout);
    }

    @Override
    protected void bindEvents() {
        super.bindEvents();
        // 用来映射数据列表和界面
        MyBottleViewModel bottleViewModel = new ViewModelProvider(this).get(MyBottleViewModel.class);
        bottleViewModel.getDataList().observe(this, bottleModels -> {
            // 当ViewModel内部对象数据变化时，更新界面
            adapter.updateDataList(bottleModels);
        });

        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::init);

        // HTTP请求列表数据后监听
        bottleListRequestCallback = new SimpleCallback<List<MyBottleModel>>() {
            @Override
            public void onUiSuccess(List<MyBottleModel> listResponseDTO) {
                if (currentPage == 0) {
                    bottleViewModel.initDataList(listResponseDTO);
                } else {
                    bottleViewModel.updateDataList(listResponseDTO);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUiFailure(int code, String msg) {
                Log.e("request BottleList Error", "not ok :" + msg);
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        // 上拉加载
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                assert layoutManager != null;
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItem >= (totalItemCount - 1)) {
                    // 触发加载更多数据的逻辑
                    swipeRefreshLayout.setRefreshing(true);
                    Handler handler = new Handler();
                    // 这里是延迟1秒后执行的代码
                    handler.postDelayed(() -> loadMoreData(), 800);
                }
            }
        });
    }

    private void init() {
        recyclerView.smoothScrollToPosition(0); // 平滑回到顶部
        currentPage = 0;
        getBottleList(currentPage);
    }

    private void loadMoreData() {
        currentPage++;
        getBottleList(currentPage);
    }

    private void getBottleList(int page) {
        BottleService.Instance().getMyBottles(page, PAGE_SIZE, bottleListRequestCallback);
    }
}