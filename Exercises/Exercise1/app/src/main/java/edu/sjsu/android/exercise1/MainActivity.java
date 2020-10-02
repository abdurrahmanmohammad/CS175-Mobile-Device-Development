package edu.sjsu.android.exercise1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle myInput = this.getIntent().getExtras();

        TextView t = new TextView(this);
        t = findViewById(R.id.textView2);
        t.setText("Hello " + (myInput.getString("uname")));
    }
}