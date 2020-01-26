
# MVVM  MutableLiveData RecyclerView Litepal  
github : [https://github.com/Gong-Shijie/demo_MVVM](https://github.com/Gong-Shijie/demo_MVVM)  
  
MVVM是一种高效组织代码的架构。  
![MVVM](https://upload-images.jianshu.io/upload_images/19741117-193d43c0eeb4f001.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
和MVP架构一样，架构的目的在于：  
* 关注点分离   
* 降低耦合  
* 增加代码的质量  
其中MVVM有一个巨大的优点就是单元测试起来极为方便，在组织大型而且复杂的工程的时候MVVM可以发挥巨大的作用。  
##### LiveData  
一个巨大的特点是观察者模式，通过LiveData的这一特点，在ViewModel中的LiveData数据就可以通过观察者模式一旦数据发生了变化就可以对UI做出更新处理。
可以灵活利用LiveData在repository中进而数据更新通知modleView来进行逻辑处理，对数据进行处理产出UI需要的数据，这个数据也设定为LiveData那么View层就可以对UI需要的数据进行响应更新。      
![活用LiveData分层降低耦合](https://upload-images.jianshu.io/upload_images/19741117-b6c1cd410d9f639a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  

这里主要研究通过LiveData来实现的MVVM架构方式。  
***

##  原理  
这里我们需要有层次的思想来理解MVVM  
就好比计算机网络，我们将整个计算机网络服务划分为自顶而下的应用层、传输层、网络层、数据链路层、物理层  
**各自在自己的模块接受下层的服务向上层提供自己应该提供的服务。**  
理解MVVM架构的方式，我们需要理解每一个模块接受什么，提供什么服务，这样才可以灵活运用和架构MVVM  
#### View层：UI响应式更新     
View只负责处理UI事务，通过观察ViewModel中的数据变化来更新UI  
#### Model层：数据组织、存储来源    
Model层提供数据，获取数据库、文件、或者远端服务器的数据。提供给ViewModel来进行逻辑处理。也就是应用数据的源头。通常增加Repository层通过Repository层来连接数据库和服务器返回数据给Model，Model，再将数据提供给上层。   
#### ViewModle层：数据处理产出UI关心的数据    
***  
## 结构  
![代码结构](https://upload-images.jianshu.io/upload_images/19741117-e8fffedddd83f71e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
***  
#### 一个Mvvmdemo实战  
##### 效果  
![demo](https://upload-images.jianshu.io/upload_images/19741117-ff22c2778cc8d790.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)  
###### 用户通过悬浮按钮来进行交互（View层）；添加item到数据库然后从数据库取数据（Model层）；对取出的数据进行处理（ViewModel层）；更新到界面（View层）；  
***  
##### 代码 
MainActivityViewModel.java
```
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
```  
***  
MainActivity.java  
```
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
``` 
***  
DbFruit.java(数据仓库提供数据服务)  
```

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
```  
***
 XML（悬浮按钮使用）
```  
<com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/add_btn"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_alignParentBottom="true"
        android:layout_alignParentRight="true"
        android:layout_margin="16dp"
        android:src="@drawable/ic_add"
        />
```  

