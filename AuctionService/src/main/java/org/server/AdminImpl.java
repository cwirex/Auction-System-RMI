package org.server;

import org.aslib.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

public class AdminImpl extends UnicastRemoteObject implements Admin, Serializable {
    private static final String KEY = "admin_crypto_key";
    private final ServiceImpl service;

    private AdminImpl(ServiceImpl service) throws RemoteException {
        super();
        this.service = service;
    };

    static AdminImpl getInstance(String key, ServiceImpl service) throws RemoteException {
        if(!key.equals(KEY)) return null;
        return new AdminImpl(service);
    }

    @Override
    public User createUser(String login, String password) throws RemoteException {
        System.out.println("(Admin) Processing request of creating user with credentials " + login + ":" + password + " [" + LocalTime.now() + "]");
        String keyHash = login + ":" + password;
        if(service.users.containsKey(keyHash))
            return null;
        UserImpl user = new UserImpl(login, service);
        service.users.put(keyHash, user);
        return user;
    }

    @Override
    public Item createItem(String name, int value) throws RemoteException {
        System.out.println("(Admin) Processing request of creating item " + name + ", $" + value + " [" + LocalTime.now() + "]");
        Item item = new Item(name, value);
        if(service.items.containsKey(name))
            return null;
        service.items.put(name, item);
        return item;
    }


    @Override
    public Auction createAuction(Item item, LocalDateTime start, LocalDateTime end) throws RemoteException {
        System.out.println("(Admin) Processing request of creating auction of " + item.getName() + " [" + LocalTime.now() + "]");
        int hash_id = start.hashCode()%9000;
        if(hash_id < 0) hash_id = -hash_id + 1000;
        String id = item.getName().toLowerCase() + ":" + hash_id;
        if(service.auctions.containsKey(id))
            return null;
        AuctionImpl auction = new AuctionImpl(id, start, end, item);
        service.auctions.put(id, auction);
        return auction;
    }

    @Override
    public Auction createAuction(String itemId, LocalDateTime start, LocalDateTime end) throws RemoteException {
        Item item = service.items.get(itemId);
        if(item == null){
            return null;
        } else {
            return createAuction(item, start, end);
        }
    }

    @Override
    public Map<String, User> getUsers() throws RemoteException {
        System.out.println("(Admin) Request for users list [" + LocalTime.now() + "]");
        return service.users;
    }

    @Override
    public Map<String, Item> getItems() throws RemoteException {
        System.out.println("(Admin) Request for items list [" + LocalTime.now() + "]");
        return service.items;
    }

    @Override
    public Map<String, Auction> getAuctions() throws RemoteException {
        System.out.println("(Admin) Request for auctions list [" + LocalTime.now() + "]");
        return service.auctions;
    }

    public String sayHello() throws RemoteException {
        System.out.println("(Admin) Hello! " + " [" + LocalTime.now() + "]");
        return "HELLO!";
    }
}
