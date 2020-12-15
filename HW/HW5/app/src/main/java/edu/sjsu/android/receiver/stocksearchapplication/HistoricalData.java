package edu.sjsu.android.receiver.stocksearchapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Objects;

public class HistoricalData extends Fragment {
    private SharedPreferences sharedPreferences; // Stores the favorite stocks
    private String stock; // Stores the stock to view
    private Context context; // Stores the context of the activity
    private ProgressBar historyStockProgressBar; // Shows loading symbol when loading stock data
    private TextView fetchingHistoricalTextView; // Text view to indicate that favorite stocks are being loaded
    private RecyclerView stockHistoryRecyclerView; // RecyclerView which stores stock information

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.historical_data, container, false);

        // ########## Start: Link layout XML content and set instance variables ##########
        context = getActivity(); // Activity context of the application
        stock = ((StockDetails) Objects.requireNonNull(getActivity())).stock; // Retrieve stock from StockDetails
        historyStockProgressBar = (ProgressBar) layout.findViewById(R.id.historyStockProgressBar); // Shows loading symbol when loading data
        fetchingHistoricalTextView = (TextView) layout.findViewById(R.id.fetchingHistoricalTextView);
        stockHistoryRecyclerView = (RecyclerView) layout.findViewById(R.id.historyStockRecyclerView);
        // ########## End: Set instance variables ##########


        // ########## Start: Retrieve and parse stock data. Load the contents in RecyclerView. ##########
        parseCurrentData();
        // ########## End: Retrieve and parse stock data. Load the contents in RecyclerView. ##########
        return layout;
    }


    private void parseCurrentData() {
        // Construct API URL
        //Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String date = simpleDateFormat.format(calendar.getTime());
        // Get annual stock data from 1/1/2010 to current date
        String url = "https://api.tiingo.com/tiingo/daily/" + stock + "/prices?startDate=2010-01-01&resampleFreq=annually&token=" + getString(R.string.token);
        RequestQueue mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // Retrieve array result of JSON search query
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<StockField> stockFields = new ArrayList<>();
                        try {
                            // Create a new ArrayList to store parse result values
                            ArrayList<StockHistory> stockHistories = new ArrayList<StockHistory>();
                            // Iterate for each row in array
                            for (int i = response.length() - 1; i >= 0; i--) {
                                // Retrieve JSON object from JSON array
                                JSONObject obj = response.getJSONObject(i);
                                // Parse the array fields in JSON object
                                String date = obj.getString("date");
                                String open = obj.getString("open");
                                String high = obj.getString("high");
                                String low = obj.getString("low");
                                String close = obj.getString("close");
                                String volume = obj.getString("volume");
                                // Store parsed fields in an object
                                StockHistory stockHistory = new StockHistory(date, open, high, low, close, volume);
                                // Store object in ArrayList which will go to the adapter
                                stockHistories.add(stockHistory);
                            }
                            // Add dividers to the views in the RecyclerView: https://developer.android.com/reference/androidx/recyclerview/widget/DividerItemDecoration
                            stockHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            stockHistoryRecyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                            // Create an adapter for the RecyclerView and attach it to the RecyclerView
                            StockHistoryAdapter adapter = new StockHistoryAdapter(context, stockHistories);
                            // Set the adapter
                            stockHistoryRecyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Turn off loading/fetchingTextView symbols since data is loaded
                        historyStockProgressBar.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar invisible
                        fetchingHistoricalTextView.setVisibility(View.INVISIBLE); // Make fetching text invisible
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                historyStockProgressBar.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar visible
                fetchingHistoricalTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar visible
                Toast.makeText(context, "Failed to refresh!", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(jsonArrayRequest);
    }
}
