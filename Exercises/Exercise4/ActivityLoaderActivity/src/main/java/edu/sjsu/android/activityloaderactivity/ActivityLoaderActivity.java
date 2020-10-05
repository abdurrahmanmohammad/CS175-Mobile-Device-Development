package edu.sjsu.android.activityloaderactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityLoaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        // Link with buttons in XML
        Button webButton = findViewById(R.id.webButton);
        Button callButton = findViewById(R.id.callButton);
        // Button actions: (https://developer.android.com/reference/android/widget/Button)
        // Intents: (https://developer.android.com/training/basics/intents/sending)
        webButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri webpage = Uri.parse("https://www.amazon.com"); // Save URL
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage); // Create an Intent for webpage
                // From: (https://developer.android.com/training/basics/intents/sending)
                String title = getResources().getString(R.string.chooser_title); // Always use string resources for UI text. This says something like "Share this photo with"
                Intent chooser = Intent.createChooser(webIntent, title); // Create intent to show chooser
                // Below if statement is not executed for some reason!!!
                if (webIntent.resolveActivity(getPackageManager()) != null) { // Verify the intent will resolve to at least one activity
                    startActivity(chooser);
                }
                startActivity(chooser);
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri number = Uri.parse("tel:+194912344444"); // Save number
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number); // Create an Intent to call
                startActivity(callIntent); // Start the Intent
            }
        });
    }
}