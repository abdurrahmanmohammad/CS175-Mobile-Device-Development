package edu.sjsu.android.receiver.stocksearchapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StockHistoryAdapter extends RecyclerView.Adapter<StockHistoryAdapter.StockHistoryView> {

    private ArrayList<StockHistory> stockHistories;
    private LayoutInflater inflater;

    public StockHistoryAdapter(Context context, ArrayList<StockHistory> stockHistories) {
        inflater = LayoutInflater.from(context);
        this.stockHistories = stockHistories;
    }

    @Override
    public StockHistoryView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.stock_history_row, parent, false);
        StockHistoryView holder = new StockHistoryView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StockHistoryView holder, int position) {
        StockHistory current = stockHistories.get(position); // Get position of stock in ArrayList
        holder.dateValue.setText(current.date);
        holder.openValue.setText(current.open);
        holder.highValue.setText(current.high);
        holder.lowValue.setText(current.low);
        holder.closeValue.setText(current.close);
        holder.volumeValue.setText(current.volume);
    }

    @Override
    public int getItemCount() {
        return stockHistories.size();
    }

    static class StockHistoryView extends RecyclerView.ViewHolder {
        private final TextView dateValue;
        private final TextView openValue;
        private final TextView highValue;
        private final TextView lowValue;
        private final TextView closeValue;
        private final TextView volumeValue;

        public StockHistoryView(@NonNull View itemView) {
            super(itemView);
            dateValue = (TextView) itemView.findViewById(R.id.dateValue);
            openValue = (TextView) itemView.findViewById(R.id.openValue);
            highValue = (TextView) itemView.findViewById(R.id.highValue);
            lowValue = (TextView) itemView.findViewById(R.id.lowValue);
            closeValue = (TextView) itemView.findViewById(R.id.closeValue);
            volumeValue = (TextView) itemView.findViewById(R.id.volumeValue);
        }
    }
}
