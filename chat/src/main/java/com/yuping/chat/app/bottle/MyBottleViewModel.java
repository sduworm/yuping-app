package com.yuping.chat.app.bottle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class MyBottleViewModel extends ViewModel {

    // 使用MutableLiveData来包装数据列表
    private final MutableLiveData<List<MyBottleModel>> dataList = new MutableLiveData<>();

    // 构造器私有，防止外部直接创建ViewModel实例
    private MyBottleViewModel() {
        // 可以在这里初始化一些数据
        List<MyBottleModel> initialData = new ArrayList<>();
        // 假设我们添加一些初始数据
        // initialData.add(new MyBottleModel(/* 初始化参数 */));
        // ...

        // 设置初始数据
        dataList.setValue(initialData);
    }

    // 提供一个公有的方法来获取MutableLiveData的不可变版本（LiveData）
    // 这有助于防止外部直接修改LiveData中的数据
    public LiveData<List<MyBottleModel>> getDataList() {
        return dataList;
    }

    // 提供一个方法来更新数据列表
    public void initDataList(List<MyBottleModel> newData) {
        dataList.setValue(newData);
    }

    // 提供一个方法来更新数据列表
    public void updateDataList(List<MyBottleModel> newData) {
        List<MyBottleModel> list = dataList.getValue();
        if (list == null) {
            dataList.setValue(newData);
        } else {
            // 去重合并
            list.addAll(newData);
            List<MyBottleModel> result = list.stream()
                    .distinct().sorted(Comparator.comparingLong(MyBottleModel::getBid).reversed())
                    .collect(Collectors.toList());

            dataList.setValue(result);
        }
    }
}