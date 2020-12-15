package edu.sjsu.android.receiver.stocksearchapplication;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

// Basically, this class is used to manage the 2 tabs which display info
// You include the ways to navigate between the 2 tabs and so on - like using a menu bar or swiping left or right
public class StockDetails extends AppCompatActivity {

    public String stock; // Stores the stock symbol
    public boolean isFavorite; // Stores whether the stock is a favorite stock


    private SectionsPagerAdapter sectionsPagerAdapter;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_details_menu);

        // Start: Retrieve intent from main page and parse inputs
        Intent intent = getIntent();
        stock = intent.getStringExtra("stock"); // Retrieve stock name
        isFavorite = intent.getBooleanExtra("isFavorite", false); // Check if this stock is a favorite
        // End: Retrieve intent from main page and parse inputs

        // Start: Set the toolbar to navigate between the pages
        getSupportActionBar().setTitle(stock);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // End: Set the toolbar to navigate between the pages


        // Create the adapter that will return a fragment for each of the 2 primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Table layout: seems to maintain the pages in a table format
        // ViewPager: Allows you to slide between fragments (for moving from page to page)
        // Set up the ViewPager with the sections adapter
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2); // limit to 2 pages

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * Use to create a menu which manages the 2 pages: you can switch between the 2 pages
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Get which page to load/switch to: creates the page and transitions
         */
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                CurrentStockInformation tab1 = new CurrentStockInformation();
                return tab1;
            } else if (position == 1) {
                HistoricalData tab2 = new HistoricalData();
                return tab2;
            } else return null;
        }

        /**
         * Number of total pages in this activity: number of pages to include in top menu
         */
        @Override
        public int getCount() {

            return 2; // We have a total of 2 pages
        }

        /**
         * Use this to title the pages
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Current Stock information";
            else if (position == 1) return "Historical Data";
            else return null;
        }
    }
}
