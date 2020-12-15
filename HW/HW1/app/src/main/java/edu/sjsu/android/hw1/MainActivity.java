package edu.sjsu.android.hw1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // Instance Variables / Input Fields
    private double interestRate;
    private EditText AmountBorrowed;
    private TextView IRTextView;
    private SeekBar IRSeekBar;
    private RadioGroup LoanTerm;
    private CheckBox TaxesInsurance;
    private TextView MonthlyPayment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AmountBorrowed = (EditText) findViewById(R.id.AmountBorrowed);
        IRTextView = (TextView) findViewById(R.id.IRTextView);
        IRSeekBar = (SeekBar) findViewById(R.id.IRSeekBar);
        LoanTerm = (RadioGroup) findViewById(R.id.LoanTerm);
        TaxesInsurance = (CheckBox) findViewById(R.id.TaxesInsurance);
        MonthlyPayment = (TextView) findViewById(R.id.MonthlyPayment);

        IRSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    interestRate = IRSeekBar.getProgress() / 10.0; // Retrieve Annual Interest Rate
                    IRTextView.setText(interestRate + " % "); // Update TextView for Interest Rate
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onClick(View view) {
        // P = Principal (the amount borrowed)
        // I = Annual interest (ex. 10)
        // Y = Years
        String input = AmountBorrowed.getText().toString(); // Retrieve Amount Borrowed as String

        // Check if input is valid
        if(input.length() == 0) { // If String is empty
            Toast.makeText(this, "Amount borrowed cannot be empty!", Toast.LENGTH_LONG).show();
            return;
        } else if(!input.matches("\\d+(\\.\\d+)?")) { // If string not a number (ex. 1, 1.1)
            Toast.makeText(this, "Invalid number!", Toast.LENGTH_LONG).show();
            return;
        }
        double P = Double.parseDouble(AmountBorrowed.getText().toString()); // Retrieve Amount Borrowed

        double I = IRSeekBar.getProgress() / 10.0; // Retrieve Annual Interest Rate (progress bar / 10)
        // Determine number of years
        double Y; // Loan Term
        if (((RadioButton) findViewById(R.id.fifteen)).isChecked()) // Check if 15 was selected
            Y = 15;
        else if (((RadioButton) findViewById(R.id.twenty)).isChecked()) // Check if 20 was selected
            Y = 20;
        else if (((RadioButton) findViewById(R.id.thirty)).isChecked()) // Check if 30 was selected
            Y = 30;
        else { // Error (no selection)
            Toast.makeText(this, "Select a loan term!", Toast.LENGTH_LONG).show();
            return;
        }
        boolean isTaxesInsurance = TaxesInsurance.isChecked(); // Check if taxes are included
        // Tax = P * 0.1 / (Years * 12) = P * 0.1 / N
        double M = LoanUtil.calculate(P, I, Y, isTaxesInsurance); // Get Monthly Payment
        String output = String.format("$%.2f", M);
        MonthlyPayment.setText("Monthly Payment: " + output); // Display Monthly Payment
    }
}