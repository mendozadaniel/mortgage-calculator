package com.papercup.danny.mortgagecalculator;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements MortgageCalculatorFragment.OnFragmentInteractionListener,
                   AllPaymentsFragment.OnFragmentInteractionListener {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ViewPager and set it's PagerAdapter  so that it can display items
        // Controls the swiping between pages
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MortgageFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the TabLayout the ViewPager
        // Easy to implement tabs with design styling.
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction(double balance, int months, double monthlyRate, double payment) {

        /* Send data from Mortgage Calculate Fragment to All Payments Fragment. */
        FragmentManager fm = getSupportFragmentManager();
        AllPaymentsFragment apf = (AllPaymentsFragment) fm.findFragmentById(R.id.viewpager);
        apf.updateAllPayments(balance, months, monthlyRate, payment);
    }
}

