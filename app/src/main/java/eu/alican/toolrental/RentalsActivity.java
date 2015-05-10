package eu.alican.toolrental;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.util.ArrayList;

import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Place;
import eu.alican.toolrental.models.Rental;


public class RentalsActivity extends ActionBarActivity {
    ListView listView;
    MyDbHandler dbHandler;
    ArrayList<Rental> rentals;
    ArrayAdapter<Rental> rentalAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals);


        listView = (ListView) findViewById(R.id.rentalListView);
        dbHandler = new MyDbHandler(this, null, null, 1);
        rentals = dbHandler.getRentalsByPlace(3, MyDbHandler.RentalEntry.FILTER_RETURNED);

        rentalAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, rentals);
        listView.setAdapter(rentalAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rentals, menu);
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
