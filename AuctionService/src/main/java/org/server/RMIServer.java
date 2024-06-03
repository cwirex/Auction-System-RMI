package org.server;

import org.aslib.Service;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class RMIServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(8899);
            Service service = new ServiceImpl();
            Naming.rebind("rmi://127.0.0.1:8899/service", service);

            System.out.println("RMI server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}