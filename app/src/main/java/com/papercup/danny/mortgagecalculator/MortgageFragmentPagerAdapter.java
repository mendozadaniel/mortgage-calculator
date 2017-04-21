package com.papercup.danny.mortgagecalculator;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/*
 * Created by danielmendoza on 3/12/17.
 */

public class MortgageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final static int PAGE_COUNT = 2;

    private Context context;

    public MortgageFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return MortgageCalculatorFragment.newInstance(position + 1);
            case 1:
                return AllPaymentsFragment.newInstance(position + 1);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return context.getString(R.string.mortgage_calculator_page_title);
            case 1:
                return context.getString(R.string.all_payments_page_title);
            default:
                return null;

        }
    }

}
