package eu.alican.toolrental;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import eu.alican.toolrental.adapter.ProductAdapter;
import eu.alican.toolrental.adapter.RecyclerAdapter;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Product;
import eu.alican.toolrental.utls.FetchJsonTask;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;


public class MainActivity extends ActionBarActivity {
    public MyDbHandler handler;
    public ArrayList<Product> products;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(R.id.my_toolbar, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        handler = new MyDbHandler(this, null, null, 1);

        products = handler.getProducts(-1);

        mAdapter = new RecyclerAdapter(MainActivity.this, products);

        mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(mAdapter));

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Alle");
        categories.add("0");
        categories.add("1");
        categories.add("2");
        categories.add("3");


        Spinner categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                products = handler.getProducts(arg2-1);
                mAdapter = new RecyclerAdapter(MainActivity.this, products);
                mRecyclerView.setAdapter(new SlideInBottomAnimationAdapter(mAdapter));

                //mRecyclerView.swapAdapter(mAdapter, true);
              //  mAdapter.notifyItemRangeRemoved(1,2);


                mAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                Log.e("klkl", "nothing");

            }
        });

//        fetchProductsFromDb(-1);
//        ListView lvItems = (ListView) findViewById(R.id.listView);
//
//        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//
//                productCursor.moveToPosition(position);
//                int productId = productCursor.getInt(0);
//                intent.putExtra("productId", productId);
//                startActivity(intent);
//
//            }
//        });


    }
//    public void fetchProductsFromDb(int category){
//        handler = new MyDbHandler(this, null, null, 1);
//        productCursor = handler.getProducts(category);
//
//        productAdapter = new ProductAdapter(this, productCursor, false);
//        ListView lvItems = (ListView) findViewById(R.id.listView);
//        lvItems.setAdapter(productAdapter);
//
//    }


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
