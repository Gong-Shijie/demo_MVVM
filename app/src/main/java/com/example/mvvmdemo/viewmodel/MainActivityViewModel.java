package com.example.mvvmdemo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mvvmdemo.model.Fruit;
import com.example.mvvmdemo.model.repositiry.DbFruit;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {

    //记住新建对象创建实例，否则容易出现空引用问题
    private DbFruit dbFruit = DbFruit.getInstance();
    private  List<Fruit> fruitList=new ArrayList<>();
    private MutableLiveData<List<Fruit>> mutableLiveData= new MutableLiveData<>();


    public MutableLiveData<List<Fruit>> getMutableLiveData() {
        return mutableLiveData;
    }

    public  void additem(){
        //dbFruit用来操作数据库
       dbFruit.additem();
       fruitList = dbFruit.getallFruit();
       //调用postValue来更新数据，该更新就被观察者观察到做相应的处理
       mutableLiveData.postValue(fruitList);
    }
    public void inidata(){
        //创建数据库
        LitePal.getDatabase();
        fruitList =null;
        fruitList = dbFruit.getallFruit();
        //mutableLiveData的第一次赋值使用setValue
        mutableLiveData.setValue(fruitList);
    }

}
