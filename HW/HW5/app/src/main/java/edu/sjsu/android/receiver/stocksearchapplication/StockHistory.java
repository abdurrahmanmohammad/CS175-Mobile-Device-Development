package edu.sjsu.android.receiver.stocksearchapplication;

public class StockHistory {
    public String date;
    public String open;
    public String high;
    public String low;
    public String close;
    public String volume;

    public StockHistory(String date, String open, String high, String low, String close, String volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
