package my.test.testapp.PagingLibrary;

import android.support.v7.util.DiffUtil;

import my.test.testapp.Room.ProductRoom;

public class ProductDiffCallback extends DiffUtil.ItemCallback<ProductRoom> {
    @Override
    public boolean areItemsTheSame(ProductRoom product, ProductRoom t1) {
        return product.id == t1.id;
    }

    @Override
    public boolean areContentsTheSame(ProductRoom product, ProductRoom t1) {
        return product == t1;
    }
}
