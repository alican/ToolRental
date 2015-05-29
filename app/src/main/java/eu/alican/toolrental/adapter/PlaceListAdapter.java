package eu.alican.toolrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import eu.alican.toolrental.R;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Place;

/**
 * Project: ToolRental
 * Created by alican on 10.05.2015.
 */


public class PlaceListAdapter extends ArrayAdapter<Place> {
    private final Context context;
    private final ArrayList<Place> values;
    int layoutResourceId;

    public PlaceListAdapter(Context context, ArrayList<Place> values) {
        super(context, R.layout.places_card_list, values);
        this.context = context;
        this.values = values;
        this.layoutResourceId = R.layout.places_card_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        PlaceHolder holder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new PlaceHolder();
            holder.placeName = (TextView)row.findViewById(R.id.place_name);
            holder.dayPrice = (TextView) row.findViewById(R.id.dayPrice);
            holder.totalPrice = (TextView) row.findViewById(R.id.totalPrice);
            holder.rentCount = (TextView) row.findViewById(R.id.textView4);
            holder.archivCount = (TextView) row.findViewById(R.id.textView7);
            row.setTag(holder);
        }else{
            holder = (PlaceHolder)row.getTag();
        }

        Place place = values.get(position);
        holder.position = position;
        holder.placeName.setText(place.getName());
        new CalculatePriceTask(place, holder, position).execute();
        //holder.dayPrice.setText(String.format("%.2f", place.getPricePerDay()));
        //holder.totalPrice.setText(String.format("%.2f", place.getTotalPrice()));
        return row;
    }


    private static class CalculatePriceTask extends AsyncTask<Void, Void, Float[]>{
        private int mPosition;
        private Place place;
        private float pricePerDay;
        private float totalPrice;
        private int rentCount;
        private int archivCount;

        PlaceHolder holder;


        public CalculatePriceTask(Place place, PlaceHolder holder, int position) {
            this.place = place;
            this.holder = holder;
            this.mPosition = position;
        }

        @Override
        protected Float[] doInBackground(Void... values) {
            pricePerDay = place.getPricePerDay();
            totalPrice = place.getTotalPrice();
            rentCount = place.getRentCount(MyDbHandler.RentalEntry.FILTER_STILL_BORROWED);
            archivCount = place.getRentCount(MyDbHandler.RentalEntry.FILTER_RETURNED);

            Float[] v = new Float[4];
            v[0] = pricePerDay;
            v[1] = totalPrice;
            v[2] = (float) rentCount;
            v[3] = (float) archivCount;
            return v;
        }

        @Override
        protected void onPostExecute(Float[] values) {
            if (holder.position != mPosition){
                return;
            }
            holder.dayPrice.setText(String.format("%.2f", values[0]));
            holder.totalPrice.setText(String.format("%.2f", values[1]));
            holder.rentCount.setText(String.format("%.0f", values[2]));
            holder.archivCount.setText(String.format("%.0f", values[3]));

        }
    }

    static class PlaceHolder
    {
        TextView placeName;
        TextView dayPrice;
        TextView totalPrice;
        TextView rentCount;
        TextView archivCount;
        int position;
    }
}

