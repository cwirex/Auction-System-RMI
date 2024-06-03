package org.client;

import org.aslib.*;

import java.rmi.Naming;

public class Client {
    public static void main(String[] args) {
        try {
            // Look up the remote service
            Service service = (Service) Naming.lookup("rmi://127.0.0.1:8899/service");
            ClientRunner runner = new ClientRunner(service);
            runner.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
