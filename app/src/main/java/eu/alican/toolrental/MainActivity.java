package eu.alican.toolrental;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import eu.alican.toolrental.adapter.ProductAdapter;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.utls.FetchJsonTask;


public class MainActivity extends ActionBarActivity {
    public MyDbHandler handler;
    public Cursor productCursor;
    public  ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Alle");
        categories.add("0");
        categories.add("1");
        categories.add("2");
        categories.add("3");

        //FetchJsonTask weatherTask = new FetchJsonTask(MainActivity.this);
        //weatherTask.execute();

        MyDbHandler handler = new MyDbHandler(this, null, null, 1);
        SQLiteDatabase db = handler.getWritableDatabase();


        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                fetchProductsFromDb(arg2-1);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Log.e("klkl", "nothing");

            }
        });

        fetchProductsFromDb(-1);
        ListView lvItems = (ListView) findViewById(R.id.listView);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                productCursor.moveToPosition(position);
                int productId = productCursor.getInt(0);
                intent.putExtra("productId", productId);
                startActivity(intent);

            }
        });


    }
    public void fetchProductsFromDb(int category){
        handler = new MyDbHandler(this, null, null, 1);
        productCursor = handler.getProducts(category);

        productAdapter = new ProductAdapter(this, productCursor);
        ListView lvItems = (ListView) findViewById(R.id.listView);
        lvItems.setAdapter(productAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
