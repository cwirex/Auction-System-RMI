package org.aslib;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * The Service interface defines methods for user login and retrieval of administrative and auction data.
 */
public interface Service extends Remote {

    /**
     * Logs in a user with the specified login and password.
     *
     * @param login the login name of the user
     * @param password the password of the user
     * @return the logged-in User object
     * @throws RemoteException if a remote communication error occurs
     */
    User login(String login, String password) throws RemoteException;

    /**
     * Returns the Admin object, if authentication with the specified key is ok.
     *
     * @param key the crypto key used to retrieve the Admin
     * @return the Admin object
     * @throws RemoteException if a remote communication error occurs
     */
    Admin getAdmin(String key) throws RemoteException;

    /**
     * Returns a map of all auctions.
     *
     * @return a map of auction IDs to Auction objects
     * @throws RemoteException if a remote communication error occurs
     */
    Map<String, Auction> listAuctions() throws RemoteException;
}
