package eu.alican.toolrental.utls;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import eu.alican.toolrental.db.MyDbHandler;

/**
 * Created by alican on 22.04.2015.
 */
public class FetchJsonTask extends AsyncTask<String, Void, String[]> {


    private final String LOG_TAG = FetchJsonTask.class.getSimpleName();
    private final Context mContext;



    public FetchJsonTask(Context context) {
        mContext = context;


    }

    private void getProductDataFromJson(String productJsonStr) throws JSONException {
        Log.d(LOG_TAG, "FetchJsonTask triggered");


        final String JSON_NAME = "name";
        final String JSON_PRICE = "price";
        final String JSON_DESC = "description";
        final String JSON_PID = "productId";
        final String JSON_CATEGORY = "category";

        try {
            JSONArray productArray = new JSONArray(productJsonStr);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(productArray.length());

            for (int i = 0; i < productArray.length(); i++) {
                JSONObject obj = productArray.getJSONObject(i);
                String name = obj.getString(JSON_NAME);
                int price = obj.getInt(JSON_PRICE);
                String description = obj.getString(JSON_DESC);
                String productId = obj.getString(JSON_PID);
                int category = obj.getInt(JSON_CATEGORY);

                ContentValues productValues = new ContentValues();
                productValues.put(MyDbHandler.ProductEntry.COLUMN_NAME, name);
                productValues.put(MyDbHandler.ProductEntry.COLUMN_PRICE, price);
                productValues.put(MyDbHandler.ProductEntry.COLUMN_DESC, description);
                productValues.put(MyDbHandler.ProductEntry.COLUMN_CATEGORY, category);
                // TODO rest implementieren


                cVVector.add(productValues);
            }
            // add to database
            if ( cVVector.size() > 0 ) {
                MyDbHandler dbHandler = new MyDbHandler(mContext, null, null, 1);
                dbHandler.bulkInsert(cVVector);

            }



            Log.d(LOG_TAG, "FetchJsonTask Complete. " + cVVector.size() + " Inserted");


        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }


    @Override
    protected String[] doInBackground(String... params) {
        String json;

        try {

            AssetManager manager = mContext.getAssets();
            InputStream is = manager.open("tools.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            getProductDataFromJson(json);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
