package com.papercup.danny.mortgagecalculator;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by danielmendoza on 3/12/17.
 */

public class MortgageCalculatorFragment extends Fragment {
    public final static String ARG_PAGE = "FRAGMENT_MORTGAGE";

    private final static String REGEX_ONLY_DIGITS = "\\d*";
    private final static NumberFormat NO_DECIMAL_FORMAT = new DecimalFormat("###");
    private final static NumberFormat DECIMAL_FORMAT = new DecimalFormat("###,###,##0.00");

    private int mPage;
    private View rootView;
    private EditText homeValueEdit;
    private EditText downPaymentEdit;
    private EditText interestRateEdit;
    private EditText homeInsuranceEdit;
    private EditText propertyTaxesEdit;
    private TextView monthlyPaymentsText;
    private Button calculateButton;
    private Spinner loanTermSpinner;
    private OnFragmentInteractionListener mListener;

    public static MortgageCalculatorFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MortgageCalculatorFragment fragment = new MortgageCalculatorFragment();
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
        rootView = inflater.inflate(R.layout.mortgage_calculator_fragment_page, container, false);

        initializeVariablesAndFunctionality();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                        + " must implement OnFragmentInteractionListener.");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("payment", monthlyPaymentsText.getText().toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            monthlyPaymentsText.setText((String)savedInstanceState.get("payment"));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void initializeVariablesAndFunctionality() {
        homeValueEdit = (EditText) rootView.findViewById(R.id.homeValue);
        downPaymentEdit = (EditText) rootView.findViewById(R.id.downPayment);
        interestRateEdit = (EditText) rootView.findViewById(R.id.interestRate);
        homeInsuranceEdit = (EditText) rootView.findViewById(R.id.homeInsurance);
        propertyTaxesEdit = (EditText) rootView.findViewById(R.id.propertyTaxes);

        monthlyPaymentsText = (TextView) rootView.findViewById(R.id.monthlyPayment);

        calculateButton = (Button) rootView.findViewById(R.id.calculateButton);

        // Spinner element
        loanTermSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        // Spinner Drop down elements
        String[] terms = getResources().getStringArray(R.array.loan_terms_list);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_style, terms);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_style);

        // attaching data adapter to spinner
        loanTermSpinner.setAdapter(dataAdapter);

        /* Home Value */
        homeValueEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().matches(REGEX_ONLY_DIGITS)) {
                /* Remove the last non-digit */
                    s.delete(s.length() - 1, s.length());
                }

            /* Update percentages in down payment and property taxes */
                EditText downPaymentValue = (EditText) rootView.findViewById(R.id.downPayment);
                String result = getPercentageOfHomeValue(s.toString(),
                        downPaymentValue.getText().toString());

                TextView downPaymentPercentage = (TextView) rootView.findViewById(R.id.downPaymentPercentage);
                downPaymentPercentage.setText(result);

                EditText propertyTaxesValue = (EditText) rootView.findViewById(R.id.propertyTaxes);
                result = getPercentageOfHomeValue(s.toString(),
                        propertyTaxesValue.getText().toString());

                TextView propertyTaxesPercentage = (TextView) rootView.findViewById(R.id.propertyTaxesPercentage);
                propertyTaxesPercentage.setText(result);
            }
        });

        /* Down Payment */
        downPaymentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().matches(REGEX_ONLY_DIGITS)) {
                    /* Remove the last non-digit */
                    s.delete(s.length() - 1, s.length());
                }

                EditText homeValueEditText = (EditText) rootView.findViewById(R.id.homeValue);
                String result = getPercentageOfHomeValue(homeValueEditText.getText().toString(),
                        s.toString());

                TextView downPaymentPercentageTextView = (TextView) rootView.findViewById(R.id.downPaymentPercentage);
                downPaymentPercentageTextView.setText(result);
            }
        });

        /* Property taxes */
        propertyTaxesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                EditText homeValueEditText = (EditText) getActivity().findViewById(R.id.homeValue);

                String result = getPercentageOfHomeValue(homeValueEditText.getText().toString(),
                        s.toString());

                TextView propertyTaxesPercentage = (TextView) rootView.findViewById(R.id.propertyTaxesPercentage);
                propertyTaxesPercentage.setText(result);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Need these basics requirements to calculate a monthly payment.
                * Home Value, Interest Rate and Loan Term (automatically selected) */
                if (homeValueEdit.getText().toString().isEmpty()) {
                    /* Use getActivity() to get context since the activity is associated with
                     * a fragment. The activity is a context since Activity extends Context. */
                    Toast.makeText(getActivity().getBaseContext(), "Please enter a loan amount.",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else if (interestRateEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity().getBaseContext(), "Please enter an interest rate.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                /* Calculate Adjusted Loan Amount: Loan Amount - Down Payment */
                double p = adjustedLoanAmount();

                /* Determine the number of payments in months, n */
                int n = convertLoanTermYearsToMonths();

                /* Calculate monthly interest rate: r */
                double r = monthlyInterestRate();

                double monthlyPayment = p * ( (r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1) );

                double homeInsurance = 0;
                if (!homeInsuranceEdit.getText().toString().isEmpty()) {
                    homeInsurance = Double.parseDouble(homeInsuranceEdit.getText().toString()) / 12;
                }

                double propertyTaxes = 0;
                if (!propertyTaxesEdit.getText().toString().isEmpty()) {
                    propertyTaxes = Double.parseDouble(propertyTaxesEdit.getText().toString()) / 12;
                }

                /* Send info to All Payments Fragment. */
                if (mListener != null) {
                    mListener.onFragmentInteraction(p, n, r, monthlyPayment);
                }

                monthlyPayment += (homeInsurance + propertyTaxes);

                String monthlyPaymentsString = "$ " + DECIMAL_FORMAT.format(monthlyPayment);

                monthlyPaymentsText.setText(monthlyPaymentsString);
            }
        });
    }

    private double adjustedLoanAmount() {

        /* Calculate Adjusted Loan Amount: Loan Amount - Down Payment */
        double loanAmount = Double.parseDouble(homeValueEdit.getText().toString());

        double downPaymentAmount = 0;
        if (!downPaymentEdit.getText().toString().isEmpty()) {
            downPaymentAmount = Double.parseDouble(downPaymentEdit.getText().toString());
        }

        return loanAmount - downPaymentAmount;
    }

    private int convertLoanTermYearsToMonths() {

        String result = loanTermSpinner.getSelectedItem().toString();

        int years = 0;
        if ("30 years".equals(result)) {
            years = 30;
        } else if ("20 years".equals(result)) {
            years = 20;
        } else if ("15 years".equals(result)) {
            years = 15;
        }
        return years * 12;
    }

    private double monthlyInterestRate() {
        return (Double.parseDouble(interestRateEdit.getText().toString())/100) / 12;
    }

    private String getPercentageOfHomeValue(String homeValueStr, String otherStr) {
        String result = "0";

        double homeValue = 0;
        if (!homeValueStr.isEmpty()) {
            homeValue = Double.parseDouble(homeValueStr);
        }

        double otherValue = 0;
        if (!otherStr.isEmpty()) {
            otherValue = Double.parseDouble(otherStr);
        }

        if (homeValue > 0 && homeValue >= otherValue) {
            double percentage = Math.ceil((otherValue / homeValue) * 100);
            result = NO_DECIMAL_FORMAT.format(percentage);
        } else if (otherValue > homeValue) {
            result = "100";
        }
        return result + "%";
    }

    /**
     * This interface must be implemented by activities that contain this fragment
     * to allow an interaction in this fragment to be communicated to the activity and
     * potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(double balance, int months, double monthlyRate, double payment);
    }

}
