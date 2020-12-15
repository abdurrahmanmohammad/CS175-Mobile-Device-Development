package edu.sjsu.android.receiver.stocksearchapplication;

public class SearchStock {
    public String ticker;
    public String name;
    public String assetType;

    public SearchStock(String ticker, String name, String assetType) {
        this.ticker = ticker;
        this.name = name;
        this.assetType = assetType;
    }

    @Override
    public String toString() {
        return String.format("%s\n%s (%s)", ticker, name, assetType);
    }
}
