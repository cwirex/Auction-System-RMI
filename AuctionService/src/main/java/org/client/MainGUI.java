package org.client;

import org.aslib.Admin;
import org.aslib.Auction;
import org.aslib.Service;
import org.aslib.User;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.Scanner;

public class MainGUI extends GUI {
    public MainGUI(GUIHolder holder, Service service) {
        super(holder, service);
    }

    @Override
    public void showMenu() {
        String s = """
                ===== MAIN MENU =====
                * 1. User login
                * 2. Administrator login
                * 3. Auctions
                :""";
        System.out.print(s);
    }

    @Override
    public boolean selectOption(int option) {
        return switch (option) {
            case 1 -> {
                onUserLogin(3);
                yield true;
            }
            case 2 -> {
                onAdminLogin(2);
                yield true;
            }
            case 3 -> {
                onAuctions();
                yield true;
            }
            default -> false;
        };
    }

    private void onUserLogin(int attemptsLeft){
        Scanner scanner = new Scanner(System.in);
        if (attemptsLeft == 0){
            System.out.println("You have tried to log in too many times!");
            showMenuWithSelection();
        }
        System.out.print("Enter login: ");
        String login = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if(login != null && password != null){
            try {
                User user = service.login(login, password);
                if(user == null){
                    System.out.println("Invalid credentials!");
                    onUserLogin(attemptsLeft - 1);
                } else {
                    holder.swapGUI(new UserGUI(holder, service, user));
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            onUserLogin(attemptsLeft - 1);
        }
    }

    private void onAdminLogin(int attemptsLeft){
        Scanner scanner = new Scanner(System.in);
        if (attemptsLeft == 0){
            System.out.println("You have tried to log in too many times!");
            showMenuWithSelection();
        }
        System.out.print("Enter crypto-key: ");
        String key = scanner.nextLine();

        if(key != null){
            try {
                Admin admin = service.getAdmin(key);
                if(admin != null){
                    admin.sayHello();
                    holder.swapGUI(new AdminGUI(holder, service, admin));
                } else {
                    System.out.println("Invalid crypto-key!");
                    onAdminLogin(attemptsLeft - 1);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        } else {
            onAdminLogin(attemptsLeft - 1);
        }
    }

    private void onAuctions(){
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
        showMenuWithSelection();
    }
}
