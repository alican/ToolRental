package eu.alican.toolrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import eu.alican.toolrental.DetailActivity;
import eu.alican.toolrental.R;
import eu.alican.toolrental.models.Product;

/**
 * Project: ToolRental
 * Created by alican on 27.04.2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Product> mDataset;
    private Context context;
    public IMyViewHolderClicks mListener;
    public DisplayImageOptions displayImageOptions;

    View decor;
    View statusBar;
    View navBar;
    View actionBar;

    List<Pair<View, String>> pairs;

    public   class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mProductImage;
        public TextView mPriceTag;

        public ViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            mListener = listener;
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mProductImage = (ImageView) v.findViewById(R.id.pimg);
            mPriceTag = (TextView) v.findViewById(R.id.priceTag);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            mListener.onClick(v, getLayoutPosition());
        }


    }
    public interface IMyViewHolderClicks {
        void onClick(View caller, int position);
    }



    public RecyclerAdapter(Context context, ArrayList<Product> myDataset) {
        mDataset = myDataset;
        this.context = context;

        displayImageOptions = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .build();


        decor = ((Activity) context).getWindow().getDecorView();
        statusBar = decor.findViewById(android.R.id.statusBarBackground);
        navBar = decor.findViewById(android.R.id.navigationBarBackground);
        actionBar = decor.findViewById(R.id.my_toolbar);


        pairs = new ArrayList<>();
        pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        pairs.add(Pair.create(actionBar, "tool_bar"));
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {


        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, new RecyclerAdapter.IMyViewHolderClicks(){

            public void onClick(View caller, int position) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("productId", mDataset.get(position).getId());
                final ImageView image =  (ImageView) caller.findViewById(R.id.pimg);

                pairs.add(Pair.create((View)image, "productImage"));

                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        pairs.toArray(new Pair[pairs.size()])).toBundle();

             //   ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, image, "productImage");

                context.startActivity(intent, options);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUri = "assets://" + mDataset.get(position).getImage();//local or remote image uri address

        ImageLoader.getInstance().displayImage(imageUri, holder.mProductImage, displayImageOptions);
        //holder.mProductImage.setVisibility(View.VISIBLE);



        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getName());
        holder.mPriceTag.setText(mDataset.get(position).getPrice() + " \u20AC");

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
       // holder.mProductImage.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
