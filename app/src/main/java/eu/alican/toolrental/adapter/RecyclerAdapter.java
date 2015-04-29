package eu.alican.toolrental.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

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


    public   class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mProductImage;

        public ViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            mListener = listener;
            mTextView = (TextView) v.findViewById(R.id.info_text);
            mProductImage = (ImageView) v.findViewById(R.id.pimg);
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

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, image, "productImage");

                context.startActivity(intent, options.toBundle());
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUri = "assets://" + mDataset.get(position).getImage();//local or remote image uri address

        ImageLoader.getInstance().displayImage(imageUri, holder.mProductImage);

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
