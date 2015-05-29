package eu.alican.toolrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import eu.alican.toolrental.DetailActivity;
import eu.alican.toolrental.R;
import eu.alican.toolrental.models.Product;
import eu.alican.toolrental.models.Rental;

/**
 * Project: ToolRental
 * Created by alican on 10.05.2015.
 */


public class RentalAdapter extends ArrayAdapter<Rental> {
    private final Context context;
    private final ArrayList<Rental> values;

    public RentalAdapter(Context context, ArrayList<Rental> values) {
        super(context, R.layout.rental_card_list_item, values);
        this.context = context;
        this.values = values;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.rental_card_list_item, parent, false);

        ImageView image = (ImageView) rowView.findViewById(R.id.product_image);
        TextView productName = (TextView) rowView.findViewById(R.id.product_name);
        TextView price = (TextView) rowView.findViewById(R.id.dayPrice);
        TextView totalPrice = (TextView) rowView.findViewById(R.id.totalPrice);
        TextView dayCount = (TextView) rowView.findViewById(R.id.days);

        final Rental rental = values.get(position);
        final Product product = rental.getProduct();



        Button bttnOpenProduct = (Button) rowView.findViewById(R.id.bttnOpenProduct);
        Button bttnCancel = (Button) rowView.findViewById(R.id.bttnCancel);

        bttnOpenProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("productId", product.getId());
                context.startActivity(intent);

            }
        });
        if (rental.isInRent()){
            bttnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rental.bringBack();
                    ((Activity)context).finish();
                }
            });
        }else{
            bttnCancel.setVisibility(View.GONE);
        }


        String imageUri = "assets://" + product.getImage();//local or remote image uri address

        ImageLoader.getInstance().displayImage(imageUri, image);

        productName.setText(product.getName());
        price.setText(product.getPrice()+ " EUR");
        totalPrice.setText((rental.getDayCount() * product.getPrice()) + " EUR");
        dayCount.setText(rental.getDayCount()+"");
        // change the icon for Windows and iPhone

        return rowView;
    }
}

