package eu.alican.toolrental.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.Iterator;
import java.util.Vector;

import eu.alican.toolrental.models.Product;

/**
 * Created by alican on 22.04.2015.
 */
public class MyDbHandler extends SQLiteOpenHelper  {

    public static abstract class ProductEntry implements BaseColumns{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CATEGORY = "category";

    }

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "Toolrental.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductEntry.COLUMN_NAME + " TEXT, "  +
                    ProductEntry.COLUMN_DESC + " TEXT, " +
                    ProductEntry.COLUMN_PRICE + " INTEGER, " +
                    ProductEntry.COLUMN_CATEGORY + " INTEGER" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;



    public MyDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }





    public void insert(String name, String description, int price){
        SQLiteDatabase db = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.COLUMN_NAME, name);
            values.put(ProductEntry.COLUMN_DESC, description);
            values.put(ProductEntry.COLUMN_PRICE, price);
            values.put(ProductEntry.COLUMN_PRICE, price);
            long newRowId;
            newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        } finally {
            db.close();
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

    public Cursor getProducts(int category){
        String selectQuery = "SELECT  * FROM " + ProductEntry.TABLE_NAME;

        if (category >= 0 && category < 4){
            selectQuery = "SELECT  * FROM " + ProductEntry.TABLE_NAME +
                    " WHERE " + ProductEntry.COLUMN_CATEGORY + "=" + category;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }





}
