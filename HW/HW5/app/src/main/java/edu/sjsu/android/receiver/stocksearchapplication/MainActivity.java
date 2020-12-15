package edu.sjsu.android.receiver.stocksearchapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    public AutoCompleteTextView autoCompleteTextView; // An AutoCompleteTextView for the user to enter company name or symbol
    private ProgressBar progressBarFavorites; // Progress bar to indicate that favorite stocks are being loaded
    private TextView fetchingTextView; // Text view to indicate that favorite stocks are being loaded
    private SharedPreferences sharedPreferences; // The data for the favorite list should be loaded from SharedPreferences
    private RecyclerView favoriteStocksRecyclerView; // Recycler view that holds saved favorite stocks
    private FavoriteStocksAdapter favoriteStocksAdapter; // Adapter for the favoriteStocksRecyclerView
    private boolean isStockValid = false; // Keeps track if stock entered is a valid stock

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the title of the menu bar (action bar)
        Objects.requireNonNull(getSupportActionBar()).setTitle("Stock Market Viewer");

        // ########## Start: Load fetchingTextView progress bar and text ##########
        progressBarFavorites = (ProgressBar) findViewById(R.id.progressBarFavorites);
        fetchingTextView = (TextView) findViewById(R.id.fetchingTextView);
        // Make them initially invisible - will make them visible at time of refresh
        progressBarFavorites.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar invisible
        fetchingTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar invisible
        // ########## End: Load fetchingTextView progress bar and text ##########

        // ########## Start: RecyclerView: Favorite stocks ##########
        // The data for the favorite list should be loaded from SharedPreferences
        sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        // Create a RecyclerView to hold views for favorite stocks
        favoriteStocksRecyclerView = (RecyclerView) findViewById(R.id.favoriteStocksRecyclerView);
        // Set the layout of this view to LinearLayout
        favoriteStocksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // ########## End: RecyclerView: Favorite stocks ##########

        // ########## Start: Create and initialize adapter for RecyclerView ##########
        ArrayList<FavoriteStock> emptyData = new ArrayList<>(); // Create an empty list of data
        favoriteStocksAdapter = new FavoriteStocksAdapter(this, emptyData); // Create an adapter with empty data
        favoriteStocksRecyclerView.setAdapter(favoriteStocksAdapter); // Set the Recycler View with that adapter
        // ########## End: Create and initialize adapter for RecyclerView ##########


        // ########## Start: AutoCompleteTextView ##########
        // Link AutoCompleteTextView in MainActivity
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        // While user is typing in the AutoCompleteTextView, get stock suggestions
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // The requests would be made only when the user enters a minimum of 3 characters to avoid unnecessary network calls
                if (charSequence.toString().length() >= 3)
                    getSuggestions(charSequence.toString()); // Pass in string and get stock suggestions
                isStockValid = false; // Set this to false since we have not selected a suggestion
                Log.d("Typed text", charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        // When user clicks on a stock suggestion, fill the AutoCompleteTextView with the stock's symbol
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                SearchStock selection = (SearchStock) parent.getItemAtPosition(position);
                autoCompleteTextView.setText(selection.ticker);
                isStockValid = true;
            }
        });
        // ########## End: AutoCompleteTextView ##########


        // ########## Start: Clear button ##########
        // Link clear button in MainActivity
        Button clearButton = (Button) findViewById(R.id.clearButton);
        // When clicked, clear the AutoCompleteTextView
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText(""); // Clear the text in the AutoCompleteTextView
            }
        });
        // ########## End: Clear button ##########


        // ########## Start: auto refresh switch ##########
        // Link auto refresh switch in MainActivity
        final Switch autoRefreshSwitch = (Switch) findViewById(R.id.autoRefreshSwitch);
        // A Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
        final Handler handler = new Handler();
        // A Runnable will be passed in a handler and will execute run() preiodically after a delay
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refreshStocks();
                handler.postDelayed(this, 10000); // Push this method after 10 seconds
            }
        };
        // When the switch is changed
        autoRefreshSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoRefreshSwitch.isChecked()) // If switch is on/checked
                    handler.postDelayed(runnable, 10000); // Causes the Runnable to be added to the message queue every 10 seconds
                else
                    handler.removeCallbacks(runnable); // Remove any pending posts of Runnable r that are in the message queue
            }
        });
        // ########## End: auto refresh switch ##########


        // ########## Start: Refresh image button ##########
        // Link refresh image button in MainActivity
        ImageButton refreshImageButton = (ImageButton) findViewById(R.id.refreshImageButton);
        // When clicked, refresh stocks
        refreshImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Refresh button", "Clicked");
                refreshStocks();
            }
        });
        // ########## End: Refresh image button ##########

    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshStocks(); // When you pause and then resume the app, you want to refresh the stocks
    }


    private void refreshStocks() {
        // Turn on loading/fetchingTextView symbols
        progressBarFavorites.setVisibility(View.VISIBLE); // Make fetchingTextView progress bar visible
        fetchingTextView.setVisibility(View.VISIBLE); // Make fetchingTextView text bar visible

        // Retrieve all values from the preferences: Get all favorite stocks
        Map<String, ?> favoriteList = sharedPreferences.getAll();

        // Check if there are no values to add to the view
        if (favoriteList.size() == 0) {
            progressBarFavorites.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar visible
            fetchingTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar visible
            return; // If no saved favorite stocks in SharedPreferences, return
        }


        // For every item saved in SharedPreferences (which was converted to string and dumped to a Map)
        // Step 1: If stock in sharedPreferences does not exists in favoriteStocksAdapter: Insert stock from sharedPreferences into favoriteStocksAdapter
        for (final Map.Entry<String, ?> entry : favoriteList.entrySet()) {
            // Get FavoriteStock string from sharedPreferences and convert it back into FavoriteStock
            FavoriteStock search = new FavoriteStock((String) entry.getValue()); // Stock from sharedPreferences
            // If the stock in sharedPreferences does not exist in favoriteStocksAdapter
            if (!favoriteStocksAdapter.favoriteStocks.contains(search)) {
                // Insert stock from sharedPreferences into favoriteStocksAdapter
                favoriteStocksAdapter.favoriteStocks.add(search);
                favoriteStocksAdapter.notifyDataSetChanged(); // Notify the adapter that the data changed
            }
        }

        // For every item in favoriteStocksAdapter
        for (final FavoriteStock search : favoriteStocksAdapter.favoriteStocks) {
            // Step 2: If stock in favoriteStocksAdapter does not exists in sharedPreferences: Delete
            if (!sharedPreferences.contains(search.ticker)) {
                favoriteStocksAdapter.favoriteStocks.remove(search); // Delete the stock
                favoriteStocksAdapter.notifyDataSetChanged(); // Notify the adapter that the data changed
                continue; // Go to next stock
            }
            // Step 3: If stock in favoriteStocksAdapter already exists in sharedPreferences: Update price and percent change
            // Construct API URL

            String url = "https://api.tiingo.com/iex/?tickers=" + search.ticker + "&token=" + getString(R.string.token);
            RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

            // Retrieve array result of JSON search query
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d("Fetching stock", search.ticker);
                            try {
                                // Retrieve JSON object from JSON array
                                JSONObject obj = response.getJSONObject(0);

                                // Parse array fields in JSON object
                                Double last = obj.getDouble("last"); // Current price
                                Double prevClose = obj.getDouble("prevClose");
                                Double change = ((last - prevClose) / last) * 100;

                                // Change values of stock
                                search.price = last;
                                search.percentChange = change;
                                Log.d(search.ticker + " (price)", last.toString());
                                Log.d(search.ticker + " (change)", change.toString());
                                favoriteStocksAdapter.notifyDataSetChanged(); // Notify the adapter that the data changed
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // Turn off loading/fetchingTextView symbols since data is loaded
                            progressBarFavorites.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar invisible
                            fetchingTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar invisible
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // If error
                    progressBarFavorites.setVisibility(View.INVISIBLE); // Make fetchingTextView progress bar visible
                    fetchingTextView.setVisibility(View.INVISIBLE); // Make fetchingTextView text bar visible
                    Toast.makeText(getApplicationContext(), "Failed to refresh!", Toast.LENGTH_SHORT).show();
                }
            });
            mQueue.add(jsonArrayRequest);
        }
    }

    // Execute this function when the "GET QUOTE" button is clicked
    public void getQuote(View view) {
        // Retrieve stock ticker String from the autoCompleteTextViewTextView
        String stock = autoCompleteTextView.getText().toString().trim();
        if (stock.isEmpty()) {
            // Create an AlertDialog to display error
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Please enter a Stock Name/Symbol");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Close the AlertDialog when the "OK" button is clicked
                        }
                    });
            alertDialog.show();
        } else if (!isStockValid) {
            // Only 1 suggestion should show, indicating a single valid stock
            // Create an AlertDialog to display error
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setMessage("Invalid Symbol");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); // Close the AlertDialog when the "OK" button is clicked
                        }
                    });
            alertDialog.show();
        } else {
            // Go to the StockDetails activity
            Intent intent = new Intent(this, StockDetails.class);
            intent.putExtra("stock", stock); // Pass in stock name
            intent.putExtra("isFavorite", sharedPreferences.contains(stock)); // Check if this stock is a favorite and pass in
            startActivity(intent); // Start the activity: go to page
        }
    }


    // Deletes the stock view from favorite's list on homepage
    public void deleteFavorite(String stock) {
        // SharedPreferences: https://developer.android.com/reference/android/content/SharedPreferences
        // SharedPreferences.Editor: https://developer.android.com/reference/android/content/SharedPreferences.Editor
        // Modifications to the preferences must go through an Editor
        SharedPreferences.Editor editor = sharedPreferences.edit(); // Edit the SharedPreferences object
        editor.remove(stock); // Remove a particular stock from SharedPreferences
        editor.apply(); // Commit your preferences changes back from this Editor to the SharedPreferences object it is editing
        refreshStocks(); // Refresh after delete
    }

    // Retrieves stock suggestions based on search/query string
    public void getSuggestions(String input) {
        Log.d("getSuggestions", "Getting suggestion ...");
        String url = "https://api.tiingo.com/tiingo/utilities/search?query=" + input + "&token=" + getString(R.string.token); // Compose API link
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        Log.d("getSuggestions URL", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("getSuggestions", "Got JSON array");
                        try {
                            ArrayList<SearchStock> suggestions = new ArrayList<SearchStock>(); // Create a new ArrayList to store parse result values
                            for (int i = 0; i < response.length(); i++) {
                                // Retrieve JSON object from JSON array
                                JSONObject obj = response.getJSONObject(i);
                                // Parse array fields in JSON object
                                String ticker = obj.getString("ticker"); // AAPL
                                String assetType = obj.getString("assetType"); // Stock
                                String name = obj.getString("name"); // Apple Inc
                                // Store all fields in a Stock object and add to suggestions list
                                suggestions.add(i, new SearchStock(ticker, name, assetType));
                                Log.d("Search suggestion", name);
                            }
                            // Create an adapter for the suggestions: this will create a list of suggestions under the AutoCompleteTextView
                            ArrayAdapter<SearchStock> suggestionsAdapter = new ArrayAdapter<SearchStock>(getApplicationContext(), R.layout.autocomplete, suggestions);
                            autoCompleteTextView.setAdapter(suggestionsAdapter);
                            suggestionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("Error in getSuggestions", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // TODO: Handle error
            }
        });
        mQueue.add(jsonArrayRequest);
        Log.d("getSuggestions", "Finished");
    }

}