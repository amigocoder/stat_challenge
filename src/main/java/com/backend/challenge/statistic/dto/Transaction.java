package com.backend.challenge.statistic.dto;

/**
 * Transaction data.
 */
public class Transaction {
    private final double amount;
    private final long timestamp;

    /**
     * Create {@link Transaction} instance.
     *
     * @param amount transaction amount
     * @param timestamp transaction time in epoch in millis
     */
    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    /**
     * Default constructor for Jackson serialization.
     */
    private Transaction() {
        this(0, 0);
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (Double.compare(that.amount, amount) != 0) return false;
        return timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(amount);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
