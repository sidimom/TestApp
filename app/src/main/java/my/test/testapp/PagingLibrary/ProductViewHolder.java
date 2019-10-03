package my.test.testapp.PagingLibrary;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import my.test.testapp.Product;
import my.test.testapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    private TextView tv_id, tv_name;
    private Switch switch_isTicked;
    public ProductViewHolder(View itemView) {
        super(itemView);
        tv_id = itemView.findViewById(R.id.tv_id);
        tv_name = itemView.findViewById(R.id.tv_name);
        switch_isTicked = itemView.findViewById(R.id.switch_isTicked);
    }

    public void bind(Product product){
        tv_id.setText((int) product.id);
        tv_name.setText(product.name);
        switch_isTicked.setChecked(product.isTicked);
    }
}
