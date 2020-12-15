package edu.sjsu.android.receiver.stocksearchapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StockInformationAdapter extends RecyclerView.Adapter<StockInformationAdapter.StockInformation> {

    private ArrayList<StockField> stockInfo;
    private LayoutInflater inflater;

    public StockInformationAdapter(Context context, ArrayList<StockField> stockInfo) {
        inflater = LayoutInflater.from(context);
        this.stockInfo = stockInfo;
    }

    @Override
    public StockInformation onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stock_information_row, parent, false);
        StockInformation holder = new StockInformation(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(StockInformation holder, int position) {
        StockField current = stockInfo.get(position); // Get position of stock in ArrayList
        // Do special tasks for the percent
        if (current.value.contains("%")) {
            holder.fieldName.setText(current.fieldName);
            if (current.value.charAt(0) != '-') { // If positive: Add + sign and make text green
                holder.value.setTextColor(Color.parseColor("green"));
                holder.value.setText("+" + current.value);
            } else { // If negative: Make text red
                holder.value.setTextColor(Color.parseColor("red"));
                holder.value.setText(current.value);
            }
        } else { // If other field
            holder.fieldName.setText(current.fieldName);
            holder.value.setText(current.value);
        }
    }

    @Override
    public int getItemCount() {
        return stockInfo.size();
    }

    static class StockInformation extends RecyclerView.ViewHolder {
        TextView fieldName;
        TextView value;

        public StockInformation(View itemView) {
            super(itemView);
            fieldName = (TextView) itemView.findViewById(R.id.stockInfoFieldName);
            value = (TextView) itemView.findViewById(R.id.stockInfoValue);
        }
    }
}
