package com.yuping.chat.app.moment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class MomentViewModel extends ViewModel {

    // 使用MutableLiveData来包装数据列表
    private MutableLiveData<List<MomentModel>> dataList = new MutableLiveData<>();

    // 构造器私有，防止外部直接创建ViewModel实例
    private MomentViewModel() {
        // 可以在这里初始化一些数据
        List<MomentModel> initialData = new ArrayList<>();
        // 假设我们添加一些初始数据
        // initialData.add(new MomentModel(/* 初始化参数 */));
        // ...

        // 设置初始数据
        dataList.setValue(initialData);
    }

    // 提供一个公有的方法来获取MutableLiveData的不可变版本（LiveData）
    // 这有助于防止外部直接修改LiveData中的数据
    public LiveData<List<MomentModel>> getDataList() {
        return dataList;
    }

    // 提供一个方法来更新数据列表
    public void initDataList(List<MomentModel> newData) {
        dataList.setValue(newData);
    }

    // 提供一个方法来更新数据列表
    public void updateDataList(List<MomentModel> newData) {
        List<MomentModel> list = dataList.getValue();
        if (list == null) {
            dataList.setValue(newData);
        } else {
            // 去重合并
            list.addAll(newData);
            List<MomentModel> result = list.stream()
                    .distinct().sorted(Comparator.comparingLong(MomentModel::getMomentId).reversed())
                    .collect(Collectors.toList());

            dataList.setValue(result);
        }
    }
}