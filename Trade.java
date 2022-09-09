package com.ordermatcher.model;

/**
 * A matched trade.
 */
public class Trade {
    private final long newOrderId;
    private final long passiveOrderId;
    private final long price;
    private final long quantity;

    /**
     * Create a new trade.
     * @param newOrderId the new client assigned order id.
     * @param passiveOrderId the passive client assigned order id.
     * @param price the price.
     * @param quantity the quantity.
     */
    public Trade(long newOrderId, long passiveOrderId, long price, long quantity) {
        this.newOrderId = newOrderId;
        this.passiveOrderId = passiveOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Returns the new client assigned order id.
     * @return the new client assigned order id.
     */
    public long getNewOrderId() {
        return newOrderId;
    }

    /**
     * Returns the passive client assigned order id.
     * @return the passive client assigned order id.
     */
    public long getPassiveOrderId() {
        return passiveOrderId;
    }

    /**
     * Returns the price.
     * @return the price.
     */
    public long getPrice() {
        return price;
    }

    /**
     * Returns the quantity.
     * @return the quantity.
     */
    public long getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "TRADE " + quantity + "@" + price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Trade other = (Trade) obj;
        if (this.newOrderId != other.newOrderId) {
            return false;
        }
        if (this.passiveOrderId != other.passiveOrderId) {
            return false;
        }
        if (this.price != other.price) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        return true;
    }

    
    
}
