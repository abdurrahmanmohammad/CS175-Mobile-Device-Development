package edu.sjsu.android.mybrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MyBrowser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_browser);
        // Display URL
        TextView text = findViewById(R.id.textView); // Link with TextView in XML
        String url = getIntent().getDataString(); // Retrieve URL from previous app
        text.setText(url); // Display URL in TextView
    }
}
