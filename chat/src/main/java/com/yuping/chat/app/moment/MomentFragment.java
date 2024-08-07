package com.yuping.chat.app.moment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuping.chat.R;

import java.util.ArrayList;
import java.util.List;

import cn.wildfire.chat.kit.net.SimpleCallback;


public class MomentFragment extends Fragment {
    private final int pageSize = 10;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton addButton;

    private SimpleCallback<List<MomentModel>> momentListRequestCallback;
    private Dialog dialog;
    private int currentPage = 0;


    // Adapter 用来做数据和界面显示的映射
    // 好处是支持数据变化和界面变化的绑定
    private MomentListItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_fragment_moment, container, false);
        bindViews(v);
        bindEvents();
        init();
        return v;
    }

    private void bindViews(View view) {
        recyclerView = view.findViewById(R.id.moment_list_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new MomentListItemAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = view.findViewById(R.id.moment_swipe_refresh_layout);
        addButton = view.findViewById(R.id.moment_add_button);

        bindDialog();
    }

    private void bindEvents() {
        // 用来映射数据列表和界面
        MomentViewModel momentViewModel = new ViewModelProvider(this).get(MomentViewModel.class);
        momentViewModel.getDataList().observe(getViewLifecycleOwner(), momentModels -> {
            // 当ViewModel内部对象数据变化时，更新界面
            adapter.updateDataList(momentModels);
        });

        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(this::init);

        // HTTP请求列表数据后监听
        momentListRequestCallback = new SimpleCallback<List<MomentModel>>() {
            @Override
            public void onUiSuccess(List<MomentModel> listResponseDTO) {
                if (currentPage == 0) {
                    momentViewModel.initDataList(listResponseDTO);
                } else {
                    momentViewModel.updateDataList(listResponseDTO);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUiFailure(int code, String msg) {
                Log.e("request Momenttt Error", "not ok :" + msg);
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
                    handler.postDelayed(() -> {
                        loadMoreData();
                    }, 800);
                }
            }
        });

        addButton.setOnClickListener(e -> {
            dialog.show();
        });

        bindDialogEvent();
    }

    private void init() {
        currentPage = 0;
        getMomentList(currentPage, pageSize);

    }

    private void loadMoreData() {
        currentPage++;
        getMomentList(currentPage, pageSize);
    }

    private void getMomentList(int page, int size) {
        MomentService.Instance().recentMoments(page, size, momentListRequestCallback);
    }

    private void createMoment(String content) {
        MomentService.Instance().createMoment(content, null, null);
    }

    private void bindDialog() {
        // 实例化Dialog，使用Theme.AppCompat.Dialog.Alert，这样它会显示为一个标准的对话框样式
        dialog = new Dialog(this.requireContext(), R.style.Theme_AppCompat_Dialog_Alert);
        // 设置Dialog的ContentView为你定义的布局
        dialog.setContentView(R.layout.main_fragment_moment_add_dialog); // 注意替换为你的布局文件ID
    }

    private void bindDialogEvent() {
        EditText momentText = dialog.findViewById(R.id.moment_new_moment_text);
        Button submitButton = dialog.findViewById(R.id.submit_new_moment);

        momentText.addTextChangedListener(new TextWatcher() {
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
            swipeRefreshLayout.setRefreshing(true);
            createMoment(momentText.getText().toString());
            Handler handler = new Handler();
            // 这里是延迟1秒后执行的代码
            handler.postDelayed(this::init, 1500);
            dialog.hide();
        });
    }
}