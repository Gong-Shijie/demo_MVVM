package com.example.mvvmdemo.view.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.model.Fruit;
import com.example.mvvmdemo.model.repositiry.DbFruit;
import com.example.mvvmdemo.view.FruitAdapter;
import com.example.mvvmdemo.viewmodel.MainActivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
   private  FloatingActionButton  floatingActionButton;
    private RecyclerView recyclerView;
    private FruitAdapter fruitAdapter;
     private MainActivityViewModel mainActivityViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        floatingActionButton = findViewById(R.id.add_btn);

        //创建ViewModel
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        //初始化数据
        mainActivityViewModel.inidata();
        //初始化显示数据到界面
        iniRecyclerView();
        //观察到数据变化就更新adapter需要的数据从而更新界面
        mainActivityViewModel.getMutableLiveData().observe(this, new Observer<List<Fruit>>() {
            @Override
            public void onChanged(List<Fruit> fruits) {
               //更新adapter
                updataUIdata();
            }
        });

        //交互添加item按钮执行添加操作
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加item到数据库，更新mutableLiveData
                mainActivityViewModel.additem();
            }
        });
    }

    public void iniRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
         fruitAdapter= new FruitAdapter(mainActivityViewModel.getMutableLiveData().getValue());
        recyclerView.setAdapter(fruitAdapter);
    }
    public void updataUIdata(){
        fruitAdapter.setFlist(mainActivityViewModel.getMutableLiveData().getValue());
        recyclerView.setAdapter(fruitAdapter);
    }

}
