package org.client;

import org.aslib.*;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;

public class AdminGUI extends GUI {
    private Admin admin;
    Scanner scanner = new Scanner(System.in);

    public AdminGUI(GUIHolder holder, Service service, Admin admin) {
        super(holder, service);
        this.admin = admin;
    }

    @Override
    void showMenu() {
        String s = """
                ===== ADMIN =====
                * 1. Create user
                ** 11. Show users
                * 2. Create item
                ** 22. Show items
                * 3. Create auction
                ** 33. Show auctions
                * 4. Logout
                :""";
        System.out.print(s);
    }

    @Override
    boolean selectOption(int option) {
        return switch (option) {
            case 1 -> {
                createUser();
                showMenuWithSelection();
                yield true;
            }
            case 11 -> {
                showUsers();
                showMenuWithSelection();
                yield true;
            }
            case 2 -> {
                createItem();
                showMenuWithSelection();
                yield true;
            }
            case 22 -> {
                showItems();
                showMenuWithSelection();
                yield true;
            }
            case 3 -> {
                createAuction();
                showMenuWithSelection();
                yield true;
            }
            case 33 -> {
                showAuctions();
                showMenuWithSelection();
                yield true;
            }
            case 4 -> {
                logout();
                holder.swapGUI(new MainGUI(holder, service));
                yield true;
            }
            default -> false;
        };
    }

    private void createUser() {
        System.out.print("Enter new user's login: ");
        String login = scanner.nextLine();
        System.out.print("Enter new user's password: ");
        String password = scanner.nextLine();

        try {
            User u = admin.createUser(login, password);
            if(u != null){
                System.out.println("User created");
            } else {
                System.out.println("User creation failed");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void showUsers(){
        try {
            Map<String, User> userMap = admin.getUsers();
            if(userMap.isEmpty()){
                System.out.println("No users found");
            }
            userMap.forEach((k, v) -> {
                try {
                    System.out.println(k + ": " + v.toReadableString());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    private void createItem() {
        System.out.println("Enter new item's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new item's price: ");
        String sPrice = scanner.nextLine();
        int price = Integer.parseInt(sPrice);

        try {
            Item i = admin.createItem(name, price);
            if(i != null){
                System.out.println("Item created");
            } else {
                System.out.println("Item creation failed");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void showItems(){
        try {
            Map<String, Item> itemMap = admin.getItems();
            if(itemMap.isEmpty()){
                System.out.println("No items found");
            }
            itemMap.forEach((k, v) -> {
                System.out.println(k + ": " + v);
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    private void createAuction()  {
        System.out.println("To create a new auction, you have to specify the item, which will be placed on it,\n" +
                "as well as the time period when it will be open for bids.");
        System.out.print("Item's ID: ");
        String itemID = scanner.nextLine();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start, end;
        try{
            System.out.println("Start (yyyy-MM-dd HH:mm:ss):");
            String dateTimeString = scanner.nextLine();
            start = LocalDateTime.parse(dateTimeString, dateTimeFormatter);
            System.out.println("End (yyyy-MM-dd HH:mm:ss):");
            dateTimeString = scanner.nextLine();
            end = LocalDateTime.parse(dateTimeString, dateTimeFormatter);

            Auction auction = admin.createAuction(itemID, start, end);
            if(auction != null){
                System.out.println("Auction created");
            } else {
                System.out.println("Auction creation failed");
            }
        } catch (DateTimeParseException parseException){
            System.out.println("! ERROR: Invalid date-time format");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAuctions(){
        try {
            Map<String, Auction> auctionMap = admin.getAuctions();
            if(auctionMap.isEmpty()){
                System.out.println("No auctions found");
            }
            auctionMap.forEach((k, v) -> {
                try {
                    System.out.println(k + ": " + v.toReadableString());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void logout(){
        System.out.println("Logging out from admin...");
    }

}
