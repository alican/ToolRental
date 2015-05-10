package eu.alican.toolrental.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import eu.alican.toolrental.R;
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

        TextView productName = (TextView) rowView.findViewById(R.id.product_name);
        TextView price = (TextView) rowView.findViewById(R.id.price);


        productName.setText(values.get(position).getProduct().getName());
        // change the icon for Windows and iPhone

        return rowView;
    }
}

