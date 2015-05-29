package eu.alican.toolrental;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import eu.alican.toolrental.adapter.RentalAdapter;
import eu.alican.toolrental.db.MyDbHandler;
import eu.alican.toolrental.models.Place;
import eu.alican.toolrental.models.Rental;


public class RentalsListActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    MyDbHandler dbHandler;
    public static Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentals_list);


        // Initialize the ViewPager and set an adapter


        // Bind the tabs to the ViewPager



        dbHandler = new MyDbHandler(this, null, null, 1);

        final int placeID = getIntent().getIntExtra("placeID", -1);
        place = dbHandler.getPlace(placeID);

        TextView placeName = (TextView) findViewById(R.id.place_name);

        placeName.setText(place.getName());


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rentals_list, menu);
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


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        MyDbHandler dbHandler;
        ArrayList<Rental> rentals;
        ArrayAdapter<Rental> rentalAdapter;
        Context context;
        private static final String ARG_SECTION_NUMBER = "section_number";
        int sectionNumber;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_rentals_list, container, false);
            context = getActivity();

            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            setRentalList(listView);


            return rootView;
        }

        public void setRentalList(ListView cardListView){
            dbHandler = new MyDbHandler(context, null, null, 1);
            sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);

            int filterSelection = MyDbHandler.RentalEntry.FILTER_ALL;
            switch (sectionNumber){
                case 1:
                    filterSelection = MyDbHandler.RentalEntry.FILTER_ALL;
                    break;
                case 2:
                    filterSelection = MyDbHandler.RentalEntry.FILTER_STILL_BORROWED;
                    break;
                case 3:
                    filterSelection = MyDbHandler.RentalEntry.FILTER_RETURNED;
                    break;
             }


            rentals = dbHandler.getRentalsByPlace(place.getId(), filterSelection);

            //rentalAdapter = new RentalAdapter(context, rentals);
            rentalAdapter = new RentalAdapter(context, rentals);
            cardListView.setAdapter(rentalAdapter);




        }

        @Override
        public void onResume() {
            super.onResume();

        }
    }

}
