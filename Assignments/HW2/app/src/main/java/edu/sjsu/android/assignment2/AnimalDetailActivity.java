package edu.sjsu.android.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnimalDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);
        // Retrieve info from previous activity: ID's of strings and image are retrieved
        Intent intent = getIntent(); // Return the intent that started this activity
        Bundle extras = intent.getExtras(); // Retrieves a map of extended data from the intent
        int title = extras.getInt("title");
        int description = extras.getInt("description");
        int image = extras.getInt("image");
        // Set animal image
        ImageView animalPic = findViewById(R.id.animalPic);
        animalPic.setImageResource(image);
        // Print animal title
        TextView animalTitle = findViewById(R.id.animalTitle);
        animalTitle.setText(title);
        // Print animal description
        TextView text = findViewById(R.id.animalDescription);
        text.setText(description);
    }

    // ********** The 2 methods below create the menu (Information, Uninstall) **********
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.information: // View information
                // Intent: Redirect to ZooInformationActivity
                Intent myIntent = new Intent(this, ZooInformationActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.uninstall: // Uninstall application
                // Intent: Uninstall application
                Uri packageURI = Uri.parse("package:" + getApplicationContext().getPackageName()); // Uninstall package
                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
                startActivity(uninstallIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}