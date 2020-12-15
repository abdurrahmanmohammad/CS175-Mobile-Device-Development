package edu.sjsu.android.receiver.stocksearchapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CurrentStockInformation extends Fragment {
    private SharedPreferences sharedPreferences; // Stores the favorite stocks
    private String stock; // Stores the stock to view
    private Context context; // Stores the context of the activity
    private ProgressBar progressBar; // Shows loading symbol when loading stock data
    private TextView fetchingCurrentTextView; // Text view to indicate that favorite stocks are being loaded
    private RecyclerView currentStockRecyclerView; // RecyclerView which stores stock information

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.current_stock_information, container, false);

        // ########## Start: Link layout XML content and set instance variables ##########
        context = getActivity(); // Activity context of the application
        stock = ((StockDetails) Objects.requireNonNull(getActivity())).stock; // Retrieve stock from StockDetails
        progressBar = (ProgressBar) layout.findViewById(R.id.currentStockProgressBar); // Shows loading symbol when loading data
        fetchingCurrentTextView = (TextView) layout.findViewById(R.id.fetchingCurrentTextView);
        currentStockRecyclerView = (RecyclerView) layout.findViewById(R.id.currentStockRecyclerView);
        // ########## End: Set instance variables ##########

        // ########## Start: Link and initialize favorite stock switch ##########
        // Retrieve the SharedPreferences which stores the favorite stocks
        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("favorite", Context.MODE_PRIVATE);
        // Link favoriteSwitch
        final Switch favoriteSwitch = (Switch) layout.findViewById(R.id.favoriteSwitch);
        // Check/flip switch if stock is a favorite
        if (sharedPreferences.contains(stock)) favoriteSwitch.setChecked(true); // Check the switch
        // When the switch is changed

        // Edit sharedPreferences: insert if checked, remove if unchecked
        favoriteSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favoriteSwitch.isChecked()) { // If switch is on/checked
                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //editor.putString(stock, stock); // Insert this stock in sharedPreferences
                    //editor.apply();
                    FavoriteStock favoriteStock = new FavoriteStock();
                    favoriteStock.ticker = stock;
                    saveStockPriceAndPercentChange(favoriteStock); // Will set all the fields in a chain of function calls
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(stock); // Remove this stock in sharedPreferences
                    editor.apply();
                }
            }
        });
        // ########## End: Link and initialize favorite stock switch ##########

        // ########## Start: Retrieve and parse stock data. Load the contents in RecyclerView. ##########
        parseCurrentData();
        // ########## End: Retrieve and parse stock data. Load the contents in RecyclerView. ##########
        return layout;
    }


    private void parseCurrentData() {
        // Construct API URL
        String url = "https://api.tiingo.com/iex/?tickers=" + stock + "&token=" + getString(R.string.token);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // Retrieve array result of JSON search query
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<StockField> stockFields = new ArrayList<>();
                        try {
                            // Retrieve JSON object from JSON array
                            JSONObject obj = response.getJSONObject(0);
                            // Parse fields required to calculate percent change
                            Double lastFloat = obj.getDouble("last"); // Current price
                            Double prevCloseFloat = obj.getDouble("prevClose");
                            Double changeFloat = ((lastFloat - prevCloseFloat) / lastFloat) * 100.0;
                            // Parse the rest of the array fields in JSON object
                            // Check if certain fields are null before accessing
                            // Parse field, create a StockField, and add it to ArrayList stockFields
                            stockFields.add(new StockField("Ticker", obj.getString("ticker")));
                            stockFields.add(new StockField("Last", String.format("$%.2f", lastFloat)));
                            stockFields.add(new StockField("Change ", String.format("%.2f%%", changeFloat)));
                            stockFields.add(new StockField("Timestamp", obj.getString("timestamp")));
                            stockFields.add(new StockField("Quote Timestamp", obj.getString("quoteTimestamp")));
                            if (!obj.isNull("lastSaleTimeStamp"))
                                stockFields.add(new StockField("Last Sale Timestamp", obj.getString("lastSaleTimeStamp")));
                            else stockFields.add(new StockField("Last Sale Timestamp", "-"));
                            if (!obj.isNull("lastSize"))
                                stockFields.add(new StockField("Last Size", obj.getString("lastSize")));
                            else stockFields.add(new StockField("Last Size", "-"));
                            stockFields.add(new StockField("Tiingo Last", "$" + obj.getString("tngoLast")));
                            stockFields.add(new StockField("Prev Close", String.format("$%.2f", prevCloseFloat)));
                            stockFields.add(new StockField("Open", "$" + obj.getString("open")));
                            stockFields.add(new StockField("High", "$" + obj.getString("high")));
                            if (!obj.isNull("mid"))
                                stockFields.add(new StockField("Mid", "$" + obj.getString("mid")));
                            else stockFields.add(new StockField("Mid", "-"));
                            stockFields.add(new StockField("Low", "$" + obj.getString("low")));
                            stockFields.add(new StockField("Volume", obj.getString("volume")));
                            if (!obj.isNull("bidSize"))
                                stockFields.add(new StockField("Bid Size", obj.getString("bidSize")));
                            else stockFields.add(new StockField("Bid Size", "-"));
                            if (!obj.isNull("bidPrice"))
                                stockFields.add(new StockField("Bid Price", "$" + obj.getString("bidPrice")));
                            else stockFields.add(new StockField("Bid Price", "-"));
                            if (!obj.isNull("askSize"))
                                stockFields.add(new StockField("Ask Size", obj.getString("askSize")));
                            else stockFields.add(new StockField("Ask Size", "-"));
                            if (!obj.isNull("askPrice"))
                                stockFields.add(new StockField("Ask Price", "$" + obj.getString("askPrice")));
                            else stockFields.add(new StockField("Ask Price", "-"));
                            // Add dividers to the views in the RecyclerView: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
                            currentStockRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            currentStockRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                            // Create an adapter for the RecyclerView and attach it to the RecyclerView
                            StockInformationAdapter adapter = new StockInformationAdapter(context, stockFields);
                            currentStockRecyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Turn off loading/fetchingTextView symbols since data is loaded
                        progressBar.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar invisible
                        fetchingCurrentTextView.setVisibility(View.INVISIBLE); // Make fetching text invisible
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // If error
                progressBar.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar visible
                fetchingCurrentTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar visible
                Toast.makeText(context, "Failed to refresh!", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonArrayRequest);
    }


    public void saveStockPriceAndPercentChange(final FavoriteStock favoriteStock) {
        // Construct API URL
        String url = "https://api.tiingo.com/iex/?tickers=" + stock + "&token=" + getString(R.string.token);
        final RequestQueue mQueue = Volley.newRequestQueue(context);
        // Retrieve array result of JSON search query
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,  null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Retrieve JSON object from JSON array
                            JSONObject obj = response.getJSONObject(0);
                            // Parse array fields in JSON object
                            Double last = obj.getDouble("last"); // Current price
                            Double prevClose = obj.getDouble("prevClose");
                            Double change = ((last - prevClose) / last) * 100;
                            favoriteStock.price = last;
                            favoriteStock.percentChange = change;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        saveStockName(favoriteStock, mQueue);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(jsonArrayRequest);
    }

    public void saveStockName(final FavoriteStock favoriteStock, final RequestQueue mQueue) {
        // Construct API URL
        String url = "https://api.tiingo.com/tiingo/utilities/search?query=" + stock + "&token=" + getString(R.string.token);
        //RequestQueue mQueue = Volley.newRequestQueue(context);
        // Retrieve array result of JSON search query
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // Retrieve JSON object from JSON array
                                JSONObject obj = response.getJSONObject(i);
                                if (obj.getString("ticker").equals(stock)) { // If tickers match
                                    // Parse array fields in JSON object
                                    favoriteStock.name = obj.getString("name"); // Current price
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Stage", "1");
                        Log.d("Favorite Stock", favoriteStock.toString());
                        saveStockMarketCap(favoriteStock, mQueue);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        mQueue.add(jsonArrayRequest);
    }

    public void saveStockMarketCap(final FavoriteStock favoriteStock, RequestQueue mQueue) {
        // Construct API URL
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(calendar.getTime());
        String url = "https://api.tiingo.com/tiingo/fundamentals/" + stock + "/daily?startDate=" + date + "&token=" + getString(R.string.token);
        //RequestQueue mQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()).getApplicationContext());
        // Retrieve array result of JSON search query
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Retrieve JSON object from JSON array
                            JSONObject obj = response.getJSONObject(0);
                            // Parse array fields in JSON object
                            // If stock not in free plan or is an empty array
                            favoriteStock.setMarketCap(obj.getDouble("marketCap")); // Set market cap
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Stage", "2");
                        Log.d("Favorite Stock", favoriteStock.toString());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(stock, favoriteStock.toString()); // Insert this stock in sharedPreferences
                        editor.apply();
                        Log.d("Saved successfully\n", favoriteStock.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                favoriteStock.marketCap = "NA";
                Log.d("Stage", "2");
                Log.d("Favorite Stock", favoriteStock.toString());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(stock, favoriteStock.toString()); // Insert this stock in sharedPreferences
                editor.apply();
                Log.d("Saved with error", favoriteStock.toString());
            }
        });
        mQueue.add(jsonArrayRequest);
    }
}
