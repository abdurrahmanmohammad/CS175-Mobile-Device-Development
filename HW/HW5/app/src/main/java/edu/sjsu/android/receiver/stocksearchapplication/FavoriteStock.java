package edu.sjsu.android.receiver.stocksearchapplication;

import java.util.Objects;

public class FavoriteStock {
    public String ticker;
    public Double price;
    public Double percentChange;
    public String marketCap = "";
    public String name = "";

    public FavoriteStock() {
    }

    public FavoriteStock(String ticker, Double price, Double percentChange) {
        this.ticker = ticker;
        this.price = price;
        this.percentChange = percentChange;
    }

    public FavoriteStock(String favoriteStock) {
        String fields[] = favoriteStock.split("\n");
        ticker = fields[0];
        price = Double.parseDouble(fields[1]);
        percentChange = Double.parseDouble(fields[2]);
        marketCap = fields[3];
        name = fields[4];
    }

    public void setMarketCap(Double marketCap) {
        if (marketCap > 1000000000) {
            this.marketCap = String.format("Market Cap: %.2f Billion", marketCap / 1000000000);
        } else if (marketCap > 1000000) {
            this.marketCap = String.format("Market Cap: %.2f Million", marketCap / 1000000);
        } else if (marketCap > 1000) {
            this.marketCap = String.format("Market Cap: %.2f Thousand", marketCap / 1000);
        } else {
            this.marketCap = String.format("Market Cap: %.2f", marketCap);
        }
    }

    @Override
    public String toString() {
        return String.format("%s\n%f\n%f\n%s\n%s", ticker, price, percentChange, marketCap, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteStock that = (FavoriteStock) o;
        return ticker.equals(that.ticker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, price, percentChange, marketCap, name);
    }
}
