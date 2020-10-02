package edu.sjsu.android.hw1;

import android.graphics.Color;
import android.view.View;
import android.widget.RadioButton;

public class LoanUtil {

    // P = Principal (the amount borrowed)
    // J = Monthly interest in decimal form (annual interest rate / 1200)
    // N = Number of months of the loan
    // T = Monthly taxes and insurance, if selected (0.1% of the amount borrowed)

    public static double calculate(double P, double I, double Y, boolean isTaxesInsurance) {
        // P = Principal (the amount borrowed)
        // I = Annual interest (ex. 10)
        // Y = Years
        // T = Monthly taxes and insurance, if selected (0.1% of the amount borrowed)
        // T = P * 0.1 / N
        double J = I / 1200; // Monthly interest in decimal form (annual interest rate / 1200)
        double N = Y * 12; // (15 | 20 | 30) years * 12 months in a year
        double T = (isTaxesInsurance) ? P * 0.1 / N : 0.0;

        if(I == 0.0) return isZero( P,  N,  T);
        else return notZero( P,  J,  N,  T);
    }

    // For interest rates of 0%, the monthly payment is simply: M = (P/N) + T
    private static double isZero(double P, double N, double T) {
        return P / N + T;
    }

    // For interest rates greater than 0%, the monthly payment is: M = (P * (J / (1 - Math.pow((1 + J), -N))) + T);
    private static double notZero(double P, double J, double N, double T) {
        return (P * (J / (1 - Math.pow((1 + J), -N))) + T);
    }
}
