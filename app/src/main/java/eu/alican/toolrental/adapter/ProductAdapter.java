package eu.alican.toolrental.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

import eu.alican.toolrental.R;

/**
 * Created by alican on 24.04.2015.
 */

public class ProductAdapter extends CursorAdapter {


    public ProductAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }



    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
        // Populate fields with extracted properties
        tvBody.setText(body);
        tvPriority.setText(String.valueOf(priority));
    }
}