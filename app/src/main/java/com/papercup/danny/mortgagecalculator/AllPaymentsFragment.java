package com.papercup.danny.mortgagecalculator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/*
 * Created by danielmendoza on 3/12/17.
 */
public class AllPaymentsFragment extends Fragment {
    public static final String ARG_PAGE = "FRAGMENT_ALL_PAYMENTS";
    public static final NumberFormat DECIMAL_FORMAT = new DecimalFormat("###,###,##0.00");

    private static final String BALANCE = "BALANCE";
    private static final String MONTHS = "MONTHS";
    private static final String MONTHLY_RATE = "MONTHLY_RATE";
    private static final String PAYMENT = "PAYMENT";

    private TextView monthlyPaymentValue;
    private TextView loanAmountValue;
    private TextView totalCostValue;
    private OnFragmentInteractionListener mListener;
    private int mPage;
    private int months;
    private double balance;
    private double monthlyRate;
    private double payment;

    public static AllPaymentsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        AllPaymentsFragment fragment = new AllPaymentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_payments, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putDouble(BALANCE, balance);
        outState.putInt(MONTHS, months);
        outState.putDouble(MONTHLY_RATE, monthlyRate);
        outState.putDouble(PAYMENT, payment);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            updateAllPayments((double)savedInstanceState.get(BALANCE),
                    (int)savedInstanceState.get(MONTHS),
                    (double)savedInstanceState.get(MONTHLY_RATE),
                    (double)savedInstanceState.get(PAYMENT));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AllPaymentsFragment.OnFragmentInteractionListener) {
            mListener = (AllPaymentsFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener.");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        monthlyPaymentValue = (TextView) getActivity().findViewById(R.id.all_payments_monthly_payment);
        loanAmountValue = (TextView) getActivity().findViewById(R.id.loanAmount);
        totalCostValue = (TextView) getActivity().findViewById(R.id.totalCost);

        monthlyPaymentValue.setText("N/A");
        loanAmountValue.setText("N/A");
        totalCostValue.setText("N/A");
    }

    public void updateAllPayments(double balance, int months, double monthlyRate, double payment) {

        this.balance = balance;
        this.months = months;
        this.monthlyRate = monthlyRate;
        this.payment = payment;

        monthlyPaymentValue.setText(DECIMAL_FORMAT.format(payment));
        loanAmountValue.setText(DECIMAL_FORMAT.format(balance));

        Payment[] payments = generateAllPayments(balance, months, monthlyRate, payment);

        ListView lv = (ListView) getActivity().findViewById(R.id.payments_list);
        // Add HeaderView to ListView
        // ViewGroup headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.custom_all_payments_row_heading, null);
        // lv.addHeaderView(headerView);
        ListAdapter adapter = new AllPaymentsAdapter(getActivity(), payments);
        lv.setAdapter(adapter);
    }

    private Payment[] generateAllPayments(double balance, int months, double monthlyRate, double monthlyPayment) {

        Payment[] payments = new Payment[months];

        Payment payment;
        double interest;
        double principal;
        double totalInterest = 0;
        double totalCostOfLoan = balance;
        for (int i = 1; i <= months; i++) {

            interest = balance * monthlyRate;
            principal = monthlyPayment - interest;
            balance = balance - principal; /* Update the balance */
            totalInterest += interest;

            payment = new Payment();
            payment.setMonth(i);
            payment.setInterest(interest);
            payment.setPrincipal(principal);
            payment.setRemainingBalance(balance);

            payments[i-1] = payment;
        }
        /* Initial loan balance and interest */
        totalCostOfLoan += totalInterest;
        totalCostValue.setText(DECIMAL_FORMAT.format(totalCostOfLoan));

        return payments;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(double balance, int months, double monthlyRate, double payment);
    }
}