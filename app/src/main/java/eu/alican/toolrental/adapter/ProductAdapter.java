package eu.alican.toolrental.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.List;

import eu.alican.toolrental.R;

/**
 * Created by alican on 24.04.2015.
 */

public class ProductAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private int imd;

    static class ViewHolder {
        TextView name;
        TextView price;
        ImageView picture;
        int position;
    }

    public ProductAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
   // @Override
   // public View newView(Context context, Cursor cursor, ViewGroup parent) {
    //    return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    //}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.product_list_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.name = (TextView) view.findViewById(R.id.product_name);
        holder.picture = (ImageView) view.findViewById(R.id.product_image);
        view.setTag(holder);
        return view;
    }
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(cursor.getString(1));

        String imageName = cursor.getString(5);
        ImageView imageView = (ImageView) view.findViewById(R.id.product_image);


        new SetPicturesTask(holder, imageName, context).execute(holder);
    }

    private class SetPicturesTask extends AsyncTask<ViewHolder, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        private ViewHolder v;
        private String imageName;
        private Context context;

        public SetPicturesTask(ViewHolder holder, String imageName, Context context) {
            super();
            v = holder;
            this.imageName = imageName;
            this.context = context;

            imageViewReference = new WeakReference<>(holder.picture);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                      int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

        @Override
        protected Bitmap doInBackground(ViewHolder... params) {
            v = params[0];

            Integer id = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

            return decodeSampledBitmapFromResource(context.getResources(), id, 300, 100);

        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // super.onPostExecute(result);

            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }
    }


}