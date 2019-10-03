package my.test.testapp.PagingLibrary;

import android.support.v7.util.DiffUtil;

import my.test.testapp.Product;

public class ProductDiffCallback extends DiffUtil.ItemCallback<Product> {
    @Override
    public boolean areItemsTheSame(Product product, Product t1) {
        return product.id == t1.id;
    }

    @Override
    public boolean areContentsTheSame(Product product, Product t1) {
        return product == t1;
    }
}
