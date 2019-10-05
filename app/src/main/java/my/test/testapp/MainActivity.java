package my.test.testapp;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import my.test.testapp.Dagger.AppModule;
import my.test.testapp.Dagger.DaggerAppComponent;
import my.test.testapp.Dagger.RoomModule;
import my.test.testapp.PagingLibrary.MainThreadExecutor;
import my.test.testapp.PagingLibrary.ProductAdapter;
import my.test.testapp.PagingLibrary.ProductDataSource;
import my.test.testapp.PagingLibrary.ProductDiffCallback;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_products)
    RecyclerView rv_products;

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.et_name)
    EditText et_name;
    ProductDao productDao;

    final int ID_ACTIVITY_NOTE_DETAILS = 1;
    final int RESULT_OK = 1;
    final int RESULT_DELETE = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_without_binding);

        ButterKnife.bind(this);
        productDao = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .roomModule(new RoomModule(getApplication()))
                .build()
                .getProductDao();

//        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
//                .withDrawable(R.drawable.ic_input_add)
//                .withButtonColor(Color.WHITE)
//                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
//                .withMargins(0, 0, 16, 16)
//                .create();

        //productDao = App.getInstance().getDataBase().productDao();

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

//        productDao = App.getInstance().getDataBase().productDao();
//        DataSource.Factory factory = productDao.getAllPaged();

        ProductDataSource dataSource = new ProductDataSource(productDao);
//        LiveData pagedListLiveData = new LivePagedListBuilder<>(productDao.getAllPaged(), config)
//                .build();
        @SuppressLint("WrongThread") PagedList<ProductRoom> productsList = new PagedList.Builder<>(dataSource, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor(new MainThreadExecutor())
                .build();


        ProductDiffCallback diffCallback = new ProductDiffCallback();
        ProductAdapter adapter = new ProductAdapter(diffCallback);
        adapter.submitList(productsList);

//        pagedListLiveData.observe(this, new Observer<PagedList<ProductRoom>>(){
//
//            @Override
//            public void onChanged(@Nullable PagedList<ProductRoom> products) {
//                adapter.submitList(products);
//            }
//        });


        //adapter.submitList(pagedList);

        rv_products.setAdapter(adapter);
    }

    @OnClick(R.id.floating_add_product)
    void addProduct(){


        String et_id_value = et_id.getText().toString();
        if (et_id_value.isEmpty()){
            Toast.makeText(this, "Value Id is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        int id_value = Integer.parseInt(et_id_value);
        ProductRoom product = productDao.getProductById(id_value);

        String et_name_value = et_name.getText().toString();
        if (et_name_value.isEmpty()){
            Toast.makeText(this, "Value Name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (product == null){
            product = new ProductRoom();
            product.id = id_value;
            product.name = et_name_value;
            productDao.insertProduct(product);
        }else{
            productDao.updateProduct(product);
        }
        Toast.makeText(this, "Product " + et_name_value + " is created!", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_delete_product)
    void deleteProduct(){
        String et_id_value = et_id.getText().toString();
        if (et_id_value.isEmpty()){
            Toast.makeText(this, "Value Id is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        int id_value = Integer.parseInt(et_id_value);
        ProductRoom product = productDao.getProductById(id_value);

        if (product == null){
            Toast.makeText(this, "Product with id " + id_value + " is not founded!", Toast.LENGTH_SHORT).show();
            return;
        }
        productDao.deleteProduct(product);
        Toast.makeText(this, "Product with id " + id_value + " is deleted!", Toast.LENGTH_SHORT).show();
    }

}

