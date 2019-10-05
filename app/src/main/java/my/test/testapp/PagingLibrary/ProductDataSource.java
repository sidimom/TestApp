package my.test.testapp.PagingLibrary;

import android.arch.paging.PositionalDataSource;
import android.support.annotation.NonNull;

import java.util.List;

import my.test.testapp.Room.ProductDao;
import my.test.testapp.Room.ProductRoom;

public class ProductDataSource extends PositionalDataSource<ProductRoom> {

    private ProductDao productDao;

    public ProductDataSource(ProductDao _productDao){
        this.productDao = _productDao;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams params, @NonNull LoadInitialCallback<ProductRoom> callback) {
        List<ProductRoom> result = productDao.getAll();
        callback.onResult(result, 0);
    }

    @Override
    public void loadRange(@NonNull LoadRangeParams params, @NonNull LoadRangeCallback<ProductRoom> callback) {
        List<ProductRoom> result = productDao.getAll();
        callback.onResult(result);
    }
}
