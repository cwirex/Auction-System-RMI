package org.client;

import org.aslib.Auction;
import org.aslib.Item;
import org.aslib.Service;
import org.aslib.User;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UserGUI extends GUI {
    private User user;
    private final Scanner scanner = new Scanner(System.in);

    public UserGUI(GUIHolder holder, Service service, User user) {
        super(holder, service);
        this.user = user;
    }

    @Override
    void showMenu() {
        int balance = 0;
        String user_id = "";
        try {
            balance = user.getCredits();
            user_id = user.getId();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        String s = String.format("""
                ===== USER (%s) =====
                --- Credits: $%d ---
                * 1. Refill credits
                * 2. Show auctions
                * 3. Make a bid
                * 4. Show owned items
                * 5. Logout
                :""", user_id, balance);
        System.out.print(s);
    }

    @Override
    boolean selectOption(int option) {
        return switch (option) {
            case 1 -> {
                refillCredits();
                showMenuWithSelection();
                yield true;
            }
            case 2 -> {
                showAuctions();
                showMenuWithSelection();
                yield true;
            }
            case 3 -> {
                makeBid();
                showMenuWithSelection();
                yield true;
            }
            case 4 -> {
                showItems();
                showMenuWithSelection();
                yield true;
            }
            case 5 -> {
                logout();
                holder.swapGUI(new MainGUI(holder, service));
                yield true;
            }
            default -> false;
        };
    }

    private void showItems() {
        try {
            List<Item> items = user.getItems();
            if(items.isEmpty()){
                System.out.println("You have no owned items yet!");
            } else {
                System.out.printf("Total number of items: %d\n", items.size());
                items.forEach(item -> System.out.println("- " + item.toString()));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeBid() {
        System.out.print("Please enter auction ID: ");
        String id = scanner.nextLine();
        System.out.println("Please enter the amount to bid: ");
        String sAmount = scanner.nextLine();

        try {
            int amount = Integer.parseInt(sAmount);
            if(amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            if(user.makeBid(id, amount)){
                System.out.println("The bid placed successfully! You are the highest bidder now.");
            } else {
                System.out.println("The bid got rejected! Check if auction ID is correct, if bid is high enough " +
                        "and if you have enough credits.");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e){
            System.out.println("Illegal bid value! " + e.getMessage());
        }
    }

    private void showAuctions() {
        try {
            Map<String, Auction> auctionMap = service.listAuctions();
            auctionMap.forEach((k, v) -> {
                try {
                    System.out.println(k + ": " + v.toReadableString());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            if(auctionMap.isEmpty()){
                System.out.println("No auctions found!");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void logout() {
        System.out.println("Logging out from user...");
    }

    private void refillCredits() {
        System.out.println("You can refill credits using BL1K method only. Money will be charged from your banking account. By proceeding you accept the ToS.");
        System.out.print("Enter refill amount: ");
        String sAmount = scanner.nextLine();
        System.out.print("Enter BL1K code: ");
        String code = scanner.nextLine();

        try {
            int amount = Integer.parseInt(sAmount);
            if(amount <= 0) throw new IllegalArgumentException("Amount must be positive");
            boolean success = user.refillCredits(code, amount);
            if(success) System.out.println("Successfully refilled credits!");
            else{
                System.out.println("External Service rejected transaction. Invalid BL1K code. It must contain 6 digits.");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid refill amount: " + e);
        }
    }
}
