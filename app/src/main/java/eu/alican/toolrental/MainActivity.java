package eu.alican.toolrental;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
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
import android.view.animation.LinearInterpolator;
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
import eu.alican.toolrental.utls.AndroidUtils;
import eu.alican.toolrental.utls.FetchJsonTask;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;


public class MainActivity extends ActionBarActivity {
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    public MyDbHandler handler;
    public ArrayList<Product> products;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final float TOOLBAR_ELEVATION = 14f;
    Toolbar toolbar;


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

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int verticalOffset;
            boolean scrollingUp;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrollingUp) {
                        if (verticalOffset > toolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    } else {
                        if (toolbar.getTranslationY() < toolbar.getHeight() * -0.6 && verticalOffset > toolbar.getHeight()) {
                            toolbarAnimateHide();
                        } else {
                            toolbarAnimateShow(verticalOffset);
                        }
                    }
                }
            }

            @Override
            public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                verticalOffset += dy;
                scrollingUp = dy > 0;
                int toolbarYOffset = (int) (dy - toolbar.getTranslationY());
                toolbar.animate().cancel();
                if (scrollingUp) {
                    if (toolbarYOffset < toolbar.getHeight()) {
                        if (verticalOffset > toolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        toolbar.setTranslationY(-toolbarYOffset);
                    } else {
                        toolbarSetElevation(0);
                        toolbar.setTranslationY(-toolbar.getHeight());
                    }
                } else {
                    if (toolbarYOffset < 0) {
                        if (verticalOffset <= 0) {
                            toolbarSetElevation(0);
                        }
                        toolbar.setTranslationY(0);
                    } else {
                        if (verticalOffset > toolbar.getHeight()) {
                            toolbarSetElevation(TOOLBAR_ELEVATION);
                        }
                        toolbar.setTranslationY(-toolbarYOffset);
                    }
                }
            }
        });

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
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toolbarSetElevation(float elevation) {
        // setElevation() only works on Lollipop
        if (AndroidUtils.isLollipop()) {
            toolbar.setElevation(elevation);
        }
    }

    private void toolbarAnimateShow(final int verticalOffset) {
        toolbar.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        toolbarSetElevation(verticalOffset == 0 ? 0 : TOOLBAR_ELEVATION);
                    }
                });
    }

    private void toolbarAnimateHide() {
        toolbar.animate()
                .translationY(-toolbar.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toolbarSetElevation(0);
                    }
                });
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
        if (id == R.id.manage_rentals) {
            Intent intent = new Intent(this, PlaceListActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
