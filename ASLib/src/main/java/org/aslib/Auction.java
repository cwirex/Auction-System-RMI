package org.aslib;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * The Auction interface defines methods for interacting with an auction.
 * It allows bidding, notification, and retrieval of auction details.
 */
public interface Auction extends Remote {

    /**
     * Places a bid in the auction.
     *
     * @param user the user placing the bid
     * @param bid the bid amount
     * @return true if the bid is successfully placed, false otherwise
     * @throws RemoteException if a remote communication error occurs
     */
    boolean makeBid(User user, int bid) throws RemoteException;

    /**
     * Sends a notification message to all bidders.
     *
     * @param message the notification message
     * @throws RemoteException if a remote communication error occurs
     */
    void notifyAllBidders(String message) throws RemoteException;

    /**
     * Returns the auction ID.
     *
     * @return the auction ID
     * @throws RemoteException if a remote communication error occurs
     */
    String getId() throws RemoteException;

    /**
     * Returns the current status of the auction.
     *
     * @return the auction status
     * @throws RemoteException if a remote communication error occurs
     */
    AuctionStatus getStatus() throws RemoteException;

    /**
     * Returns the item being auctioned.
     *
     * @return the item being auctioned
     * @throws RemoteException if a remote communication error occurs
     */
    Item getItem() throws RemoteException;

    /**
     * Returns the highest bid placed in the auction.
     *
     * @return the highest bid
     * @throws RemoteException if a remote communication error occurs
     */
    int getHighestBid() throws RemoteException;

    /**
     * Returns the user who placed the highest bid.
     *
     * @return the user with the highest bid
     * @throws RemoteException if a remote communication error occurs
     */
    User getHighestBidder() throws RemoteException;

    /**
     * Returns a map of all bidders.
     *
     * @return a map of user IDs to User objects
     * @throws RemoteException if a remote communication error occurs
     */
    Map<String, User> getBidders() throws RemoteException;

    /**
     * Returns the start time of the auction.
     *
     * @return the start time of the auction
     * @throws RemoteException if a remote communication error occurs
     */
    LocalDateTime getStartTime() throws RemoteException;

    /**
     * Returns the end time of the auction.
     *
     * @return the end time of the auction
     * @throws RemoteException if a remote communication error occurs
     */
    LocalDateTime getEndTime() throws RemoteException;

    /**
     * Returns a human-readable string representation of the auction.
     *
     * @return a human-readable string
     * @throws RemoteException if a remote communication error occurs
     */
    String toReadableString() throws RemoteException;
}
