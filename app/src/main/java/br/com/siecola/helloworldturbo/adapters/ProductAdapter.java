package br.com.siecola.helloworldturbo.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.siecola.helloworldturbo.R;
import br.com.siecola.helloworldturbo.models.Product;

public class ProductAdapter extends BaseAdapter {

    private final Activity activity;
    List<Product> products;

    public ProductAdapter(Activity activity, List<Product> products) {
        this.activity = activity;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(
                R.layout.product_list_item, null);

        Product product = products.get(position);

        TextView productListItemNumber = (TextView) view.
                findViewById(R.id.productListItemNumber);
        productListItemNumber.setText(Integer.toString(position + 1));

        TextView productListItemId = (TextView) view.
                findViewById(R.id.productListItemId);
        productListItemId.setText(product.getCode());

        TextView productListItemUser = (TextView) view.
                findViewById(R.id.productListItemUser);
        productListItemUser.setText(product.getName());

        return view;
    }
}
