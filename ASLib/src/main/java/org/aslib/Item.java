package org.aslib;

import java.io.Serializable;

/**
 * The Item class represents an item that can be auctioned.
 * It contains details such as name, initial price, and the final sold price.
 */
public class Item implements Serializable {

    private String name;
    private int initialPrice;
    private int soldFor;

    /**
     * Constructs a new Item with the specified name and initial price.
     *
     * @param name the name of the item
     * @param initialPrice the initial price of the item
     */
    public Item(String name, int initialPrice) {
        this.name = name;
        this.initialPrice = initialPrice;
        this.soldFor = 0;
    }

    /**
     * Returns the name of the item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the new item name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the initial price of the item.
     *
     * @return the initial price
     */
    public int getInitialPrice() {
        return initialPrice;
    }

    /**
     * Sets the initial price of the item.
     *
     * @param initialPrice the new initial price
     */
    public void setInitialPrice(int initialPrice) {
        this.initialPrice = initialPrice;
    }

    /**
     * Returns the final sold price of the item.
     *
     * @return the sold price
     */
    public int getSoldFor() {
        return soldFor;
    }

    /**
     * Sets the final sold price of the item.
     *
     * @param soldFor the new sold price
     */
    public void setSoldFor(int soldFor) {
        this.soldFor = soldFor;
    }

    /**
     * Returns a string representation of the item.
     *
     * @return a string representation of the item
     */
    @Override
    public String toString() {
        if (soldFor == 0) {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", initialPrice=" + initialPrice + "}";
        }
        return "Item{" +
                "name='" + name + '\'' +
                ", initialPrice=" + initialPrice +
                ", soldFor=" + soldFor +
                '}';
    }
}
