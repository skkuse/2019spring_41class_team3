package com.example.flixtyle;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;

import com.example.flixtyle.AccountFragment;
import com.example.flixtyle.DiscoveryActivity;
import com.example.flixtyle.R;
import com.example.flixtyle.RecommedationFragment;


public class MainActivity extends AppCompatActivity
        implements AccountFragment.OnFragmentInteractionListener, DiscoveryFragment.OnFragmentInteractionListener, RecommedationFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentPagerAdapter adaterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adaterViewPager);


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.home_tab:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.discovery_tab:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.account_tab:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM = 3;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM;
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {

                case 0:
                    return new RecommedationFragment();
                case 1:
                    return new DiscoveryFragment();
                case 2:
                    return new AccountFragment();
                default:
                    return null;

            }
        }

    }


}
