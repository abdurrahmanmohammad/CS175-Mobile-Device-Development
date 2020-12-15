package edu.sjsu.android.receiver.phone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Also, in your MainActivity, request runtime permission for READ_PHONE_STATE and READ_CALL_LOG, similar to what you did in part 1 for WRITE_EXTERNAL_STORAGE permission
        // Note that the second parameter of ActivityCompat.requestPermissions is an array of string, which can request multiple permissions.
        if (checkPermission()) startService(new Intent(this, MyPhoneReceiver.class));
        else ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.READ_CALL_LOG}, 100);
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED;
    }
}