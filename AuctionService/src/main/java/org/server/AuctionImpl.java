package org.server;

import org.aslib.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AuctionImpl extends UnicastRemoteObject implements Auction, Serializable {
    private final String id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private AuctionStatus status;
    private final Item item;
    private final Map<String, User> bidders;
    private int highestBid;
    private User highestBidder;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    AuctionImpl(String id, LocalDateTime start, LocalDateTime end, Item item) throws RemoteException {
        super();
        this.id = id;
        this.start = start;
        this.end = end;
        this.item = item;
        this.bidders = new HashMap<>();
        this.highestBid = 0;
        this.onInit();
    }

    @Override
    public String toString() {
        return "AuctionImpl{" +
                "id='" + id + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", status=" + status +
                ", item=" + item +
                ", bidders=" + bidders +
                ", highestBid=" + highestBid +
                ", highestBidder=" + highestBidder +
                '}';
    }

    private void onInit(){
        this.status = AuctionStatus.NEW;
        long delay = Duration.between(LocalDateTime.now(), start).toMillis();
        scheduler.schedule(this::onOpen, delay, TimeUnit.MILLISECONDS);
        System.out.println("(Auction) #" + this.id + " scheduled to open on " + start+ " [" + LocalTime.now() + "]");
    };

    private void onOpen(){
        this.status = AuctionStatus.OPEN;
        System.out.println("(Auction) #" + this.id + " opened."+ " [" + LocalTime.now() + "]");

        long delay = Duration.between(LocalDateTime.now(), end).toMillis();
        scheduler.schedule(this::onClose, delay, TimeUnit.MILLISECONDS);
    };

    private void onClose(){
        this.status = AuctionStatus.CLOSED;
        System.out.println("(Auction) #" + this.id + " closed." + " [" + LocalTime.now() + "]");

        if(highestBidder != null){
            try {
                item.setSoldFor(highestBid);
                System.out.printf("(Auction) Sending item %s to %s", item.getName(), highestBidder.getId());
                System.out.println(" [" + LocalTime.now() + "]");
                highestBidder.claimItem(item);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        scheduler.shutdown();
    }

    private void onNewHighestBidder(User user, int bid){
        if(highestBidder != null){
            try {
                highestBidder.refillCredits("000000", highestBid);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        highestBid = bid;
        highestBidder = user;
        try {
            String msg = String.format("Auction %s has new highest bid of %d from %s", id, bid, highestBidder.getId());
            notifyAllBidders(msg);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean makeBid(User user, int bid) throws RemoteException {
        if(user == null) return false;  // if user is not logged in
        if(bid <= highestBid) return false; // if bid is invalid
        if(this.status != AuctionStatus.OPEN) return false; // if auction is not taking bids

        bidders.put(user.getId(), user);
        onNewHighestBidder(user, bid);
        return true;
    }

    @Override
    public void notifyAllBidders(String message) throws RemoteException {
        for(User bidder : bidders.values()){
            bidder.notify(this, message);
        }
    }

    @Override
    public String getId() throws RemoteException {
        return this.id;
    }

    @Override
    public AuctionStatus getStatus() throws RemoteException {
        return this.status;
    }

    @Override
    public Item getItem() throws RemoteException {
        return this.item;
    }

    @Override
    public int getHighestBid() throws RemoteException {
        return this.highestBid;
    }

    @Override
    public Map<String, User> getBidders() throws RemoteException {
        return this.bidders;
    }

    @Override
    public LocalDateTime getStartTime() throws RemoteException {
        return start;
    }

    @Override
    public LocalDateTime getEndTime() throws RemoteException {
        return end;
    }

    @Override
    public User getHighestBidder() throws RemoteException {
        return highestBidder;
    }

    @Override
    public String toReadableString() throws RemoteException {
        return this.toString();
    }
}
