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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import eu.alican.toolrental.MainActivity;
import eu.alican.toolrental.models.Place;
import eu.alican.toolrental.models.Product;
import eu.alican.toolrental.models.Rental;
import eu.alican.toolrental.utls.FetchJsonTask;

/**
 * Created by alican on 22.04.2015.
 */
public class MyDbHandler extends SQLiteOpenHelper  {

    private Context mContext;
    public static final int DATABASE_VERSION = 15;
    public static final String DATABASE_NAME = "Toolrental.db";

    public static abstract class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_PID = "productId";
        public static final String COLUMN_IMAGE = "image";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME + " TEXT, "  +
                        COLUMN_DESC + " TEXT, " +
                        COLUMN_PID + " TEXT, " +
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
        public static final String COLUMN_CREATED_AT = "created_at";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME + " TEXT, "  +
                        COLUMN_ADDRESS + " TEXT, " +
                        COLUMN_CREATED_AT +" datetime DEFAULT CURRENT_TIMESTAMP" +
                        " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static abstract class RentalEntry implements BaseColumns{
        public static final String TABLE_NAME = "rentals";
        public static final String COLUMN_PID = "productId";
        public static final String COLUMN_LID = "locationId";
        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_END_DATE = "end_date";

        public static final int FILTER_ALL = 0;
        public static final int FILTER_RETURNED = 1;
        public static final int FILTER_STILL_BORROWED = 2;



        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_PID + " INTEGER, "  +
                        COLUMN_LID + " INTEGER, " +
                        COLUMN_START_DATE +" datetime DEFAULT CURRENT_TIMESTAMP, " +
                        COLUMN_END_DATE +" datetime " +
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
        db.execSQL(RentalEntry.SQL_CREATE_ENTRIES);
        FetchJsonTask weatherTask = new FetchJsonTask(mContext);
        weatherTask.execute();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ProductEntry.SQL_DELETE_ENTRIES);
        db.execSQL(PlaceEntry.SQL_DELETE_ENTRIES);
        db.execSQL(RentalEntry.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }


    public long newRental(int productId, int placeId){

        try (SQLiteDatabase db = getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(RentalEntry.COLUMN_PID, productId);
            values.put(RentalEntry.COLUMN_LID, placeId);

            long newRowId = db.insert(RentalEntry.TABLE_NAME, null, values);
            return newRowId;
        }

    }
    public void bringBackRental(int rentalId){
        String updateQuery = "UPDATE "+ RentalEntry.TABLE_NAME +
                " SET "+ RentalEntry.COLUMN_END_DATE +
                " = date('now') WHERE "+ RentalEntry._ID +" = "+
                rentalId;
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(updateQuery);


    }

    public Rental getRentalById(int id) throws ParseException {
        String selectQuery = "SELECT  * FROM " + RentalEntry.TABLE_NAME +
                " WHERE " + RentalEntry._ID + "=" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

//    public Rental(int id, int productId, int locationId, Date startDate, Date endDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/YYYY", Locale.getDefault());
        assert cursor != null;
        java.util.Date startDate = sdf.parse(cursor.getString(cursor.getColumnIndex(RentalEntry.COLUMN_START_DATE)));
        java.util.Date endDate = sdf.parse(cursor.getString(cursor.getColumnIndex(RentalEntry.COLUMN_END_DATE)));

        Rental rental = new Rental(
                cursor.getInt(cursor.getColumnIndex("_id")),
                cursor.getInt(cursor.getColumnIndex(RentalEntry.COLUMN_PID)),
                cursor.getInt(cursor.getColumnIndex(RentalEntry.COLUMN_LID)),
                startDate,
                endDate
        );
        cursor.close();
        db.close();
        return rental;

    }

    public ArrayList<Rental> getRentalsByPlace(int id, int filter) {
        String filterString = "";

        switch (filter){
            case RentalEntry.FILTER_ALL:
                filterString = " ";
                break;
            case RentalEntry.FILTER_RETURNED:
                filterString = " WHERE "+ RentalEntry.COLUMN_END_DATE +" IS NOT NULL";
                break;
            case RentalEntry.FILTER_STILL_BORROWED:
                filterString = " WHERE "+ RentalEntry.COLUMN_END_DATE +" IS NULL";
        }

        String selectQuery = "SELECT  * FROM " + RentalEntry.TABLE_NAME +
                filterString +
                " ORDER BY "+ RentalEntry.COLUMN_START_DATE +" DESC" ;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<Rental> rentals = new ArrayList<>();

        if (cursor != null)
            cursor.moveToFirst();

        //2015-05-08 12:54:06

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.getDefault());


        int _idIndex = cursor.getColumnIndex(RentalEntry._ID);
        int productIndex = cursor.getColumnIndex(RentalEntry.COLUMN_PID);
        int locationIndex = cursor.getColumnIndex(RentalEntry.COLUMN_LID);
        int startDateIndex = cursor.getColumnIndex(RentalEntry.COLUMN_START_DATE);
        int endDateIndex = cursor.getColumnIndex(RentalEntry.COLUMN_END_DATE);

        while(cursor.moveToNext()) {
           // Log.e("asdsadsa", DatabaseUtils.dumpCursorToString(cursor) );

            assert cursor != null;
            java.util.Date startDate = null;
            java.util.Date endDate = null;
            String startDateString =  cursor.getString(startDateIndex);
            String endDateString =  cursor.getString(endDateIndex);
            try {
                startDate = sdf.parse(startDateString);
                if (endDateString != null){
                    endDate = sdf.parse(endDateString);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


// Rental(int id, int productId, int locationId, Date startDate, Date endDate) {

            Rental rental = new Rental(
                    cursor.getInt(_idIndex),
                    cursor.getInt(productIndex),
                    cursor.getInt(locationIndex),
                    startDate,
                    endDate
            );
            rentals.add(rental); //add the item
        }
        db.close();

        return rentals;

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
        String selectQuery = "SELECT  * FROM " + PlaceEntry.TABLE_NAME + " ORDER BY created_at DESC";

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
        int pid = cursor.getColumnIndex(ProductEntry.COLUMN_PID);
        int cat = cursor.getColumnIndex(ProductEntry.COLUMN_CATEGORY);
        int price = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        int desc = cursor.getColumnIndex(ProductEntry.COLUMN_PRICE);
        while(cursor.moveToNext()) {

            Product product = new Product(
                    cursor.getInt(_id),
                    cursor.getString(name),
                    cursor.getString(desc),
                    cursor.getInt(price),
                    cursor.getString(pid),
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
                cursor.getString(cursor.getColumnIndex("productId")),
                cursor.getInt(cursor.getColumnIndex("category"))
        );
        cursor.close();
        db.close();
        return product;
    }





}
