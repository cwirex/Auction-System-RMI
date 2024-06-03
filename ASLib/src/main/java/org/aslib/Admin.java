package org.aslib;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * The Admin interface defines administrative actions in the auction service.
 * It allows the creation of users, items, and auctions, and provides access to user, item, and auction data.
 */
public interface Admin extends Remote {

    /**
     * Creates a new user with the specified login and password.
     *
     * @param login the login name of the user
     * @param password the password of the user
     * @return the created User object
     * @throws RemoteException if a remote communication error occurs
     */
    User createUser(String login, String password) throws RemoteException;

    /**
     * Creates a new item with the specified name and initial value.
     *
     * @param itemName the name of the item
     * @param value the initial value of the item
     * @return the created Item object
     * @throws RemoteException if a remote communication error occurs
     */
    Item createItem(String itemName, int value) throws RemoteException;

    /**
     * Creates a new auction with the specified item and start/end times.
     *
     * @param item the item to be auctioned
     * @param start the start time of the auction
     * @param end the end time of the auction
     * @return the created Auction object
     * @throws RemoteException if a remote communication error occurs
     */
    Auction createAuction(Item item, LocalDateTime start, LocalDateTime end) throws RemoteException;

    /**
     * Creates a new auction with the specified item name and start/end times.
     *
     * @param itemName the name of the item to be auctioned
     * @param start the start time of the auction
     * @param end the end time of the auction
     * @return the created Auction object
     * @throws RemoteException if a remote communication error occurs
     */
    Auction createAuction(String itemName, LocalDateTime start, LocalDateTime end) throws RemoteException;

    /**
     * Returns a hello message.
     *
     * @return a hello message string
     * @throws RemoteException if a remote communication error occurs
     */
    String sayHello() throws RemoteException;

    /**
     * Returns a map of all users.
     *
     * @return a map of user IDs to User objects
     * @throws RemoteException if a remote communication error occurs
     */
    Map<String, User> getUsers() throws RemoteException;

    /**
     * Returns a map of all items.
     *
     * @return a map of item names to Item objects
     * @throws RemoteException if a remote communication error occurs
     */
    Map<String, Item> getItems() throws RemoteException;

    /**
     * Returns a map of all auctions.
     *
     * @return a map of auction IDs to Auction objects
     * @throws RemoteException if a remote communication error occurs
     */
    Map<String, Auction> getAuctions() throws RemoteException;
}
