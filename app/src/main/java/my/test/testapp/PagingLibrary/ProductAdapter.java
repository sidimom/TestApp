package my.test.testapp.PagingLibrary;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.test.testapp.Product;
import my.test.testapp.R;
import my.test.testapp.Room.ProductRoom;

public class ProductAdapter extends PagedListAdapter<ProductRoom, ProductViewHolder> {
    public ProductAdapter(@NonNull DiffUtil.ItemCallback<ProductRoom> diffCallback) {
        super(diffCallback);
        Log.d("MY_TAG", "ProductAdapter");
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("MY_TAG", "ProductViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product, viewGroup, false);
        ProductViewHolder holder = new ProductViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Log.d("MY_TAG", "onBindViewHolder");
        productViewHolder.bind(getItem(i));
    }

}
