package eu.alican.toolrental;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Place;


public class SelectPlaceActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_place);

        ListView listView = (ListView) findViewById(R.id.listView);

        MyDbHandler dbHandler = new MyDbHandler(this, null, null, 1);

        ArrayList<Place> places =dbHandler.getPlaces();

        ArrayAdapter<Place> placesAdapter =
                new ArrayAdapter<Place>(SelectPlaceActivity.this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(placesAdapter);

        Button newPlace = (Button) findViewById(R.id.newPlace);
        newPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPlaceActivity.this, AddPlaceActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_place, menu);
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
