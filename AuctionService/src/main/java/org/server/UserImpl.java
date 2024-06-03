package org.server;

import org.aslib.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserImpl extends UnicastRemoteObject implements User, Serializable {
    private final String id;
    private int credits;
    private final List<Item> items = new ArrayList<>();
    private final Service service;

    UserImpl(String login, Service service) throws RemoteException {
        super();
        this.id = login;
        this.service = service;
    }

    @Override
    public String getId() throws RemoteException {
        return id;
    }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id='" + id + '\'' +
                ", credits=" + credits +
                ", items=" + items +
                '}';
    }

    @Override
    public boolean makeBid(Auction auction, int bid) throws RemoteException {
        System.out.println("(User) #" + id + " makeBid(" + auction + ", " + bid + ")"+ " [" + LocalTime.now() + "]");
        if(bid <= credits){
            boolean success = auction.makeBid(this, bid);
            if(success){
                credits -= bid;
            } else {
                System.out.printf("Auction %s rejected the bid from %s\n!", auction.getId(), id);
            }
            return success;
        } else {
            System.out.println("Not enough credits to fulfill the bid!");
            return false;
        }
    }

    @Override
    public boolean makeBid(String auctionId, int bid) throws RemoteException {
        Auction auction = service.listAuctions().get(auctionId);
        if(auction != null){
            return makeBid(auction, bid);
        }
        return false;
    }

    @Override
    public void claimItem(Item item) throws RemoteException {
        items.add(item);
        System.out.printf("(User) #%s claimed the item #%s", id, item.getName());
        System.out.println("[" + LocalTime.now() + "]");
    }

    @Override
    public boolean refillCredits(String code, int amount) throws RemoteException {
        System.out.println("(User) #" + id + " passing BL1K code to *External Service*" + " [" + LocalTime.now() + "]");
        return externalBl1kService(code, amount);

    }

    private boolean externalBl1kService(String BL1KCode, int amount) throws RemoteException {
        if(BL1KCode.length() == 6 && BL1KCode.matches("[0-9]+")){
            credits += amount;
            System.out.printf("(BL1K Service) Success! Added %d credits to account #%s: %d\n", amount, id, credits);
            return true;
        }
        return false;
    }

    @Override
    public int getCredits() throws RemoteException {
        return credits;
    }

    @Override
    public void showItems() throws RemoteException {
        System.out.print("Items of user " + id + ": ");
        for(Item item : items){
            System.out.print(item.getName() + " ");
        }
        System.out.println();
    }

    @Override
    public void notify(Auction iAuction, String msg) throws RemoteException {
        //todo: pass to remote client?
        System.out.printf("(User) #%s got notification from auction #%s: %s\n", id, iAuction.getId(), msg);
    }

    @Override
    public String toReadableString() throws RemoteException {
        return this.toString();
    }

    @Override
    public List<Item> getItems() {
        return items;
    }
}
