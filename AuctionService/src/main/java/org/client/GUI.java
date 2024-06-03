package org.client;

import org.aslib.Service;

import java.util.Scanner;

public abstract class GUI {
    protected GUIHolder holder;
    protected Service service;

    public GUI(GUIHolder holder, Service service) {
        this.holder = holder;
        this.service = service;
    }

    abstract void showMenu();
    abstract boolean selectOption(int option);

    void showMenuWithSelection(){
        showMenu();
        Scanner scanner = new Scanner(System.in);
        try{
            String s = scanner.nextLine();
            int option = Integer.parseInt(s);
            boolean legalOption = selectOption(option);
            if(!legalOption){
                System.out.println("! ERROR: Invalid option!");
                showMenuWithSelection();
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    };
}


