package org.server;

import org.aslib.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ServiceImpl extends UnicastRemoteObject implements Service, Serializable {
    Map<String, User> users = new HashMap<>();
    Map<String, Item> items = new HashMap<>();
    Map<String, Auction> auctions = new HashMap<>();

    public ServiceImpl() throws RemoteException {}

    @Override
    public User login(String login, String password) throws RemoteException {
        return users.get(login + ":" + password);   // or null
    }

    @Override
    public Admin getAdmin(String key) throws RemoteException {
        return AdminImpl.getInstance(key, this);
    }

    @Override
    public Map<String, Auction> listAuctions() throws RemoteException {
        return auctions;
    }
}
