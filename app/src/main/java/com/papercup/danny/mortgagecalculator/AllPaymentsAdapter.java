package com.papercup.danny.mortgagecalculator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AllPaymentsAdapter extends ArrayAdapter<Payment> {

    static class ViewHolder {
        TextView paymentText;
        TextView interestText;
        TextView principalText;
        TextView balanceText;
    }

    public AllPaymentsAdapter(@NonNull Context context, Payment[] payments) {
        super(context, R.layout.custom_all_payments_row, payments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater allPaymentsInflater = LayoutInflater.from(getContext());
            convertView = allPaymentsInflater.inflate(R.layout.custom_all_payments_row, parent, false);

            holder = new ViewHolder();
            holder.paymentText = (TextView) convertView.findViewById(R.id.payment);
            holder.interestText = (TextView) convertView.findViewById(R.id.interest);
            holder.principalText = (TextView) convertView.findViewById(R.id.principal);
            holder.balanceText = (TextView) convertView.findViewById(R.id.balance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Payment payment = getItem(position);

        if (payment != null) {
            NumberFormat decimalFormatTwoDigits = new DecimalFormat("###,###,##0.00");

            String month = Integer.toString(payment.getMonth());
            String interest = decimalFormatTwoDigits.format(payment.getInterest());
            String principal = decimalFormatTwoDigits.format(payment.getPrincipal());
            String balance = decimalFormatTwoDigits.format(payment.getRemainingBalance());

            holder.paymentText.setText(month);
            holder.interestText.setText(interest);
            holder.principalText.setText(principal);
            holder.balanceText.setText(balance);
        }

        return convertView;
    }
}
