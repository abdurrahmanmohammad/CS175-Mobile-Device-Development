tpackage edu.sjsu.android.assignment2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ZooInformationActivity extends AppCompatActivity {
    private Button phoneButton;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        // Create buttons
        phoneButton = (Button) findViewById(R.id.phoneButton);
        backButton = findViewById(R.id.backButton);
        // Make action listener for buttons
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Phone button: call phone number
                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:8888888"));
                startActivity(call);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Back button: go to main activity
                Intent main = new Intent(getBaseContext(), MainActivity.class);
                startActivity(main);
            }
        });
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