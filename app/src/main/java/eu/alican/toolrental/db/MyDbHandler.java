package eu.alican.toolrental.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import eu.alican.toolrental.MainActivity;
import eu.alican.toolrental.models.Place;
import eu.alican.toolrental.models.Product;
import eu.alican.toolrental.utls.FetchJsonTask;

/**
 * Created by alican on 22.04.2015.
 */
public class MyDbHandler extends SQLiteOpenHelper  {

    private Context mContext;
    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "Toolrental.db";

    public static abstract class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_IMAGE = "image";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME + " TEXT, "  +
                        COLUMN_DESC + " TEXT, " +
                        COLUMN_PRICE + " INTEGER, " +
                        COLUMN_CATEGORY + " INTEGER, " +
                        COLUMN_IMAGE + " TEXT" +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class PlaceEntry implements BaseColumns{
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME + " TEXT, "  +
                        COLUMN_ADDRESS + " TEXT" +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProductEntry.SQL_CREATE_ENTRIES);
        db.execSQL(PlaceEntry.SQL_CREATE_ENTRIES);
        FetchJsonTask weatherTask = new FetchJsonTask(mContext);
        weatherTask.execute();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ProductEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PlaceEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }



    public void insertPlace(String name, String address) {
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(PlaceEntry.COLUMN_NAME, name);
            values.put(PlaceEntry.COLUMN_ADDRESS, address);
            long newRowId;
            newRowId = db.insert(PlaceEntry.TABLE_NAME, null, values);
        }
    }
    public Place getPlace(int placeID){

        String selectQuery = "SELECT  * FROM " + PlaceEntry.TABLE_NAME +
                " WHERE " + PlaceEntry._ID + "=" + placeID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Place place = new Place(
                cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("address"))
        );
        cursor.close();
        db.close();
        return place;
    }
    public ArrayList<Place> getPlaces(){
        String selectQuery = "SELECT  * FROM " + PlaceEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Place> places = new ArrayList<Place>();


        int _id = cursor.getColumnIndex(PlaceEntry._ID);
        int name = cursor.getColumnIndex(PlaceEntry.COLUMN_NAME);
        int address = cursor.getColumnIndex(PlaceEntry.COLUMN_ADDRESS);
        while(cursor.moveToNext()) {

            Place place = new Place(
                    cursor.getInt(_id),
                    cursor.getString(name),
                    cursor.getString(address)
            );
            places.add(place); //add the item
        }
        db.close();

        return places;
    }



    public void insert(String name, String description, int price, int category){
        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME, name);
            values.put(ProductEntry.COLUMN_DESC, description);
            values.put(ProductEntry.COLUMN_PRICE, price);
            values.put(ProductEntry.COLUMN_PRICE, category);
            long newRowId;
            newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        }

    }

    public void bulkInsert(Vector<ContentValues> cVVector){
        SQLiteDatabase db = getWritableDatabase();

        try
        {
            db.beginTransaction();

            for(int i = 0; i < cVVector.size(); i++) {
                db.insert(MyDbHandler.ProductEntry.TABLE_NAME, null, cVVector.elementAt(i));
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {}
        finally
        {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<Product> getProducts(int category){
        String selectQuery = "SELECT  * FROM " + ProductEntry.TABLE_NAME;

        if (category >= 0 && category < 4){
            selectQuery = "SELECT  * FROM " + ProductEntry.TABLE_NAME +
                    " WHERE " + ProductEntry.COLUMN_CATEGORY + "=" + category;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Product> products = new ArrayList<Product>();


        int _id = cursor.getColumnIndex(ProductEntry._ID);
        int name = cursor.getColumnIndex(ProductEntry.COLUMN_NAME);
        int cat = cursor.getColumnIndex(ProductEntry.COLUMN_CATEGORY);
        int price = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int desc = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        while(cursor.moveToNext()) {

            Product product = new Product(
                    cursor.getInt(_id),
                    cursor.getString(name),
                    cursor.getString(desc),
                    cursor.getInt(price),
                    cursor.getInt(cat)
            );
            products.add(product); //add the item
        }
        db.close();

        return products;
    }
    public Product getProduct(int productId){

        String selectQuery = "SELECT  * FROM " + ProductEntry.TABLE_NAME +
                    " WHERE " + ProductEntry._ID + "=" + productId;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(
                cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getString(cursor.getColumnIndex("name")),
                cursor.getString(cursor.getColumnIndex("description")),
                cursor.getInt(cursor.getColumnIndex("price")),
                cursor.getInt(cursor.getColumnIndex("category"))
        );
        cursor.close();
        db.close();
        return product;
    }





}
