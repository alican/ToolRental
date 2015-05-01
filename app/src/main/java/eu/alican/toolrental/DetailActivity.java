package eu.alican.toolrental;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nirhart.parallaxscroll.views.ParallaxScrollView;
import com.nirhart.parallaxscroll.views.ParallaxedView;

import java.io.IOException;

import eu.alican.toolrental.adapter.ProductAdapter;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Product;


public class DetailActivity extends ActionBarActivity {

    public MyDbHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(R.id.my_toolbar, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setExitTransition(fade);
        getWindow().setEnterTransition(fade);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        int productId = getIntent().getIntExtra("productId", -1);

        handler = new MyDbHandler(this, null, null, 1);
        Product product = handler.getProduct(productId);


        Button mietenButton = (Button) findViewById(R.id.button);
        mietenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, SelectPlaceActivity.class);
                startActivity(intent);
            }
        });

        TextView pname = (TextView) findViewById(R.id.productName);
        TextView pid = (TextView) findViewById(R.id.productId);
        TextView pdesc = (TextView) findViewById(R.id.productDescription);
        ImageView productImage = (ImageView) findViewById(R.id.product_image);


        Drawable image = null;
        try {
            image = Drawable.createFromStream(getAssets().open(product.getImage()), null);
            productImage.setImageDrawable(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pname.setText(product.getName());
        pdesc.setText(product.getDescription());

        pid.setText("Nr.: " + product.getProductId());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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
