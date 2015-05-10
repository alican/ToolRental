package eu.alican.toolrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eu.alican.toolrental.R;
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
            row.setTag(holder);
        }else{
            holder = (PlaceHolder)row.getTag();
        }

        Place place = values.get(position);
        holder.placeName.setText(place.getName());

        return row;
    }

    static class PlaceHolder
    {
        TextView placeName;
    }
}

