package com.example.mvvmdemo.model.repositiry;

import com.example.mvvmdemo.R;
import com.example.mvvmdemo.model.Fruit;

import org.litepal.crud.DataSupport;

import java.util.List;

//单例模式
public class DbFruit {
    private static DbFruit dbFruit = new DbFruit();
    private DbFruit() {
        new Fruit(R.drawable.apple_pic,"apple").save();
        new Fruit(R.drawable.banana_pic,"apple").save();
        new Fruit(R.drawable.cherry_pic,"apple").save();
        new Fruit(R.drawable.grape_pic,"apple").save();
        new Fruit(R.drawable.mango_pic,"apple").save();
        new Fruit(R.drawable.orange_pic,"apple").save();
        new Fruit(R.drawable.pear_pic,"apple").save();
        new Fruit(R.drawable.pineapple_pic,"apple").save();
        new Fruit(R.drawable.strawberry_pic,"apple").save();
        new Fruit(R.drawable.watermelon_pic,"apple").save();
    }

    public static DbFruit getInstance(){
        return dbFruit;
    }

    public List<Fruit> getallFruit(){
        return DataSupport.findAll(Fruit.class);
    }

    public void additem() {
        Fruit fruit = new Fruit();
        fruit.setF_image(R.drawable.apple_pic);
        fruit.setF_name("add dpple!");
        fruit.save();
    }
}
