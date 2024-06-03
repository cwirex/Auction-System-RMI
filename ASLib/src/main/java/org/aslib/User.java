package org.aslib;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The User interface defines methods for user interactions such as bidding, claiming items, and managing credits.
 */
public interface User extends Remote {

    /**
     * Returns the user ID.
     *
     * @return the user ID
     * @throws RemoteException if a remote communication error occurs
     */
    String getId() throws RemoteException;

    /**
     * Claims the specified item.
     *
     * @param item the item to be claimed
     * @throws RemoteException if a remote communication error occurs
     */
    void claimItem(Item item) throws RemoteException;

    /**
     * Places a bid in the specified auction.
     *
     * @param auction the auction to bid in
     * @param bid the bid amount
     * @return true if the bid is successfully placed, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean makeBid(Auction auction, int bid) throws RemoteException;

    /**
     * Places a bid in the auction with the specified ID.
     *
     * @param auctionId the ID of the auction to bid in
     * @param bid the bid amount
     * @return true if the bid is successfully placed, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean makeBid(String auctionId, int bid) throws RemoteException;

    /**
     * Refills the user's credits with the specified amount using a code.
     *
     * @param code the code to use for refilling credits
     * @param amount the amount of credits to add
     * @return true if the credits are successfully refilled, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean refillCredits(String code, int amount) throws RemoteException;

    /**
     * Returns the user's current credit balance.
     *
     * @return the credit balance
     * @throws RemoteException if a remote communication error occurs
     */
    int getCredits() throws RemoteException;

    /**
     * Returns a list of items owned by the user.
     *
     * @return a list of items
     * @throws RemoteException if a remote communication error occurs
     */
    List<Item> getItems() throws RemoteException;

    /**
     * Displays the user's items.
     *
     * @throws RemoteException if a remote communication error occurs
     */
    void showItems() throws RemoteException;

    /**
     * Notifies the user with a message from a specified auction.
     *
     * @param source the auction sending the notification
     * @param message the notification message
     * @throws RemoteException if a remote communication error occurs
     */
    void notify(Auction source, String message) throws RemoteException;

    /**
     * Returns a human-readable string representation of the user.
     *
     * @return a human-readable string
     * @throws RemoteException if a remote communication error occurs
     */
    String toReadableString() throws RemoteException;
}
