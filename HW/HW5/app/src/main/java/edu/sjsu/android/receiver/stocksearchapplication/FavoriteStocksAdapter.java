package edu.sjsu.android.receiver.stocksearchapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoriteStocksAdapter extends RecyclerView.Adapter<FavoriteStocksAdapter.FavoriteStockViewHolder> {
    ArrayList<FavoriteStock> favoriteStocks = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    // Construct a new adapter and pass in data as list: FavInfo is basically a tuple that holds many fields
    public FavoriteStocksAdapter(Context context, ArrayList<FavoriteStock> favoriteStocks) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.favoriteStocks = favoriteStocks;
    }


    // Create a view/item in the view holder (list): do not link it with a FavInfo object yet - done in onBindViewHolder
    @Override
    public FavoriteStockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.favorite_stock_row, parent, false);
        FavoriteStockViewHolder holder = new FavoriteStockViewHolder(context, view);
        return holder;
    }

    /**
     * Links a view with a stock FavoriteStock in ArrayList: initialize view's text fields
     */
    @Override
    public void onBindViewHolder(FavoriteStockViewHolder holder, int position) {
        // FavoriteStock stores stock info: Retrieve the FavoriteStock based on position in list
        final FavoriteStock current = favoriteStocks.get(position);

        // Extract fields from FavoriteStock and set TextViews
        holder.ticker.setText(current.ticker);
        holder.price.setText(String.format("$%.2f", current.price));
        holder.name.setText(current.name);
        holder.marketCap.setText(current.marketCap);

        // Set percent change: Determine if increase (change to green) or decrease (change to red)
        if (current.percentChange >= 0.0) { // Positive sign
            holder.percentChange.setBackgroundColor(Color.parseColor("green"));
            holder.percentChange.setText(String.format("+%.2f%%", current.percentChange));
        } else { // Negative sign (included in float conversion)
            holder.percentChange.setBackgroundColor(Color.parseColor("red"));
            holder.percentChange.setText(String.format("%.2f%%", current.percentChange));
        }


        // ########## Start: Long click (hold) a favorite stock to delete view ##########
        holder.favoriteStockRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Confirm with a dialog if you want to remove stock
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete favorite stock?");
                // If "CONFIRM" is clicked: delete stock from favorites
                builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) context).deleteFavorite(current.ticker); // Use the delete method in main activity
                        dialog.dismiss(); // Close the dialog
                    }
                });
                // If "CANCEL" is clicked: close the dialog and do nothing
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Do nothing: close the dialog
                    }
                });
                // Create the dialog
                AlertDialog alert = builder.create();
                alert.show(); // Display the dialog
                return false;
            }
        });
        // ########## End: Long click (hold) a favorite stock to delete view ##########


        // ########## Start: Tap/click a favorite stock to view more details ##########
        holder.favoriteStockRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to go to stock details page
                Intent intent = new Intent(((MainActivity) context), StockDetails.class);
                // Retrieve and save params to pass into next page/activity
                intent.putExtra("stock", current.ticker);
                intent.putExtra("isFavorite", true);
                // Start activity: go to stock details page
                context.startActivity(intent);
            }
        });
        // ########## End: Tap/click a favorite stock to view more details ##########
    }


    /**
     * Add a new tuple of data into this adapter's ArrayList
     */
    public void insertFavorite(FavoriteStock favoriteStock) {
        favoriteStocks.add(favoriteStock); // Do the insert
        notifyDataSetChanged(); // Notify the adapter that the data changed
    }

    /**
     * Delete a certain favorite stock from ArrayList
     */
    public void deleteFavorite(String stock) {
        for (FavoriteStock favoriteStock : favoriteStocks) // Search all the favorite stocks for the stock to delete
            if (favoriteStock.ticker.equals(stock)) { // Compare stock symbols
                favoriteStocks.remove(stock); // Delete the stock
                notifyDataSetChanged(); // Notify the adapter that the data changed
                break; // Break out of the loop
            }
    }

    /**
     * Get number of items in list
     */
    @Override
    public int getItemCount() {
        return favoriteStocks.size();
    }

    /**
     * #################################################################################
     * ############################ FavoriteStockViewHolder ############################
     * #################################################################################
     */

    // This is the view/options/tiles in the holder on the main page
    class FavoriteStockViewHolder extends RecyclerView.ViewHolder {
        LinearLayout favoriteStockRow;
        TextView ticker;
        TextView price;
        TextView percentChange;
        TextView name;
        TextView marketCap;


        /**
         * This is a view in the view holder: like an item in a list on the main page
         */
        public FavoriteStockViewHolder(final Context context, View itemView) {
            super(itemView);
            favoriteStockRow = (LinearLayout) itemView.findViewById(R.id.favoriteStockRow);
            ticker = (TextView) itemView.findViewById(R.id.tickerTextView);
            price = (TextView) itemView.findViewById(R.id.currentPriceTextView);
            percentChange = (TextView) itemView.findViewById(R.id.percentChangeTextView);
            name = (TextView) itemView.findViewById(R.id.nameTextView);
            marketCap = (TextView) itemView.findViewById(R.id.marketCapTextView);
        }
    }
}
