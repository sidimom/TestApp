package my.test.testapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.Executors;

import my.test.testapp.PagingLibrary.MainThreadExecutor;
import my.test.testapp.PagingLibrary.ProductAdapter;
import my.test.testapp.PagingLibrary.ProductDiffCallback;
import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;
import my.test.testapp.databinding.ActivityMainBinding;
import my.test.testapp.myBinding.MyHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rv_products;
    Button btn_add, btn_delete;
    EditText et_id, et_name;
    ProductDao productDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_without_binding);

        btn_add = findViewById(R.id.btn_add_product);
        btn_delete = findViewById(R.id.btn_delete_product);
        et_id = findViewById(R.id.et_id);
        et_name = findViewById(R.id.et_name);
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        productDao = App.getInstance().getDataBase().productDao();

//        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        MyHandler myHandler = new MyHandler();
//        binding.setHandler(myHandler);

        /*PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        productDao = App.getInstance().getDataBase().productDao();
        DataSource.Factory factory = productDao.getAllPaged();

        LiveData pagedListLiveData = new LivePagedListBuilder<>(factory, config)
                .build();

        ProductDiffCallback diffCallback = new ProductDiffCallback();
        ProductAdapter adapter = new ProductAdapter(diffCallback);

        pagedListLiveData.observe(this, new Observer<PagedList<Product>>(){

            @Override
            public void onChanged(@Nullable PagedList<Product> products) {
                adapter.submitList(products);
            }
        });


        //adapter.submitList(pagedList);

        rv_products = findViewById(R.id.rv_products);
        rv_products.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View v) {
        String et_id_value = et_id.getText().toString();
        if (et_id_value.isEmpty()){
            Toast.makeText(this, "Value Id is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        int id_value = Integer.parseInt(et_id_value);
        ProductRoom product = productDao.getProductById(id_value);

        switch (v.getId()){
            case R.id.btn_add_product:
                String et_name_value = et_name.getText().toString();
                if (et_name_value.isEmpty()){
                    Toast.makeText(this, "Value Name is empty!", Toast.LENGTH_SHORT).show();
                    break;
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
                break;
            case R.id.btn_delete_product:
                if (product == null){
                    Toast.makeText(this, "Product with id " + id_value + " is not founded!", Toast.LENGTH_SHORT).show();
                    break;
                }
                productDao.deleteProduct(product);
                Toast.makeText(this, "Product with id " + id_value + " is deleted!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
