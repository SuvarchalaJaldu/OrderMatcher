package com.ordermatcher.model;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An order has a input (buy/sell), quantity and price.
 */
public class Order {
    private static final Pattern INPUT_PATTERN =
            Pattern.compile("(?<input>([bB][uU][yY])|([sS][eE][lL][lL]))[ ]+(?<qty>[0-9]+)[ ]*@[ ]*(?<px>[0-9]+)([ ]+#(?<id>[0-9]+))?");
    private static final String ID = "id";
    private static final String INPUT = "input";
    private static final String QUANTITY = "qty";
    private static final String PRICE = "px";

    private final long id;
    private final Input input;
    private final long price;
    private long quantity;

    /**
     * Create a new order.
     * @param id the client assigned id.
     * @param input the input (buy/sell).
     * @param price the price, must be grater than 0.
     * @param quantity the quantity, must be grater than 0.
     */
    public Order(long id, Input input, long price, long quantity) {
        this.id = id;
        this.input = Objects.requireNonNull(input);
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be > 0");
        }
        this.price = price;
        if (quantity <= 0) {
            throw new IllegalArgumentException("Volume must be > 0");
        }
        this.quantity = quantity;
    }

    /**
     * Returns the client assigned order id.
     * @return the client assigned order id.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the input.
     * @return the input, not null.
     */
    public Input getInput() {
        return input;
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

    /**
     * Set the quantity.
     * @param quantity the new quantity, must be grater than 0.
     */
    public void setQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be >= 0");
        }
        this.quantity = quantity;
    }

    /**
     * Returns true if the quantity is zero.
     * @return true if the quantity is zero, otherwise false.
     */
    public boolean isEmpty() {
        return quantity == 0;
    }

    @Override
    public String toString() {
        return input + " " + quantity + "@" + price;
    }

    public static Order parse(String str) {
        Matcher m = INPUT_PATTERN.matcher(str);
        if (!m.matches()) {
            throw new IllegalArgumentException("Order format is Illegal. Expected #id buy|sell quantity@price");
        }
        String idStr = m.group(ID);
        long id = idStr != null ? Long.valueOf(idStr) : new Random().nextLong();
        Input input = Input.valueOf(m.group(INPUT).toUpperCase());
        long price = Long.valueOf(m.group(PRICE));
        long quantity = Long.valueOf(m.group(QUANTITY));

        return new Order(id, input, price, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.input != other.input) {
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
