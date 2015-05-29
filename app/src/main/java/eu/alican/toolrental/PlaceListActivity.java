package eu.alican.toolrental;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import eu.alican.toolrental.adapter.PlaceListAdapter;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Place;
import eu.alican.toolrental.models.Rental;


public class PlaceListActivity extends ActionBarActivity {
    MyDbHandler dbHandler;
    ArrayList<Place> places;
    PlaceListAdapter placeAdapter;
    ListView listView;
    Context context;


    View decor;
    View statusBar;
    View navBar;
    View actionBar;
    List<Pair<View, String>> pairs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        context = PlaceListActivity.this;



        decor = ((Activity) context).getWindow().getDecorView();
        //statusBar = decor.findViewById(android.R.id.statusBarBackground);
       // navBar = decor.findViewById(android.R.id.navigationBarBackground);
        actionBar = decor.findViewById(R.id.my_toolbar);



        pairs = new ArrayList<>();
       // pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
       // pairs.add(Pair.create(navBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
        //pairs.add(Pair.create(actionBar, "tool_bar"));



    }

    public void fetchData(){
        dbHandler = new MyDbHandler(context, null, null, 1);
        places = dbHandler.getPlaces();
        listView = (ListView) findViewById(R.id.place_list_view);
        placeAdapter = new PlaceListAdapter(context, places);
        listView.setAdapter(placeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final CardView cardView = (CardView) view.findViewById(R.id.card_view);

                pairs.add(Pair.create((View) cardView, "cardViewInfoContainer"));


                Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                        pairs.toArray(new Pair[pairs.size()])).toBundle();

                Intent intent = new Intent(context, RentalsListActivity.class);
                intent.putExtra("placeID", places.get(position).getId());
                context.startActivity(intent, options);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_list, menu);
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
