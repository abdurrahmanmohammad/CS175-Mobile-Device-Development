package edu.sjsu.android.exercise1;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GetName extends Activity implements  android.view.View.OnClickListener {
    android.widget.EditText name;
    android.widget.Button button;
    public void onClick(View arg0) {
        Intent myIntent = new Intent(this,MainActivity.class);
        myIntent.putExtra("uname",name.getText().toString());
        this.startActivity(myIntent);
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_get_name);
        name = (EditText) this.findViewById(R.id.editText);
        button = (Button) this.findViewById(R.id.button);
        button.setOnClickListener(this);
    }
}