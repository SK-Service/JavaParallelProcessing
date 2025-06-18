package com.exparal.demo.model;

public class Portfolio {
    private String id;
    private double notional;
    private double expectedReturn;
    private double volatility;

    public Portfolio(String id, double notional, double expectedReturn, double volatility) {
        this.id = id;
        this.notional = notional;
        this.expectedReturn = expectedReturn;
        this.volatility = volatility;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getNotional() { return notional; }
    public void setNotional(double notional) { this.notional = notional; }

    public double getExpectedReturn() { return expectedReturn; }
    public void setExpectedReturn(double expectedReturn) { this.expectedReturn = expectedReturn; }

    public double getVolatility() { return volatility; }
    public void setVolatility(double volatility) { this.volatility = volatility; }
}