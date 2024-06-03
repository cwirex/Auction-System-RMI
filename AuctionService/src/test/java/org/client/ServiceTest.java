package org.client;

import org.aslib.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class ServiceTest {
    private static Registry registry;
    private static Service service;
    private static final String admin_key = "admin_crypto_key";

    @BeforeClass
    public static void setUp() throws Exception {
        registry = LocateRegistry.createRegistry(8899);
        service = new org.server.ServiceImpl();
        Naming.rebind("rmi://127.0.0.1:8899/service", service);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Unbind the service and stop the registry
        Naming.unbind("rmi://127.0.0.1:8899/service");
        UnicastRemoteObject.unexportObject(service, true);
        UnicastRemoteObject.unexportObject(registry, true);
    }

    @Test
    public void testGetService() throws Exception {
        Service lookedUpService = (Service) Naming.lookup("rmi://127.0.0.1:8899/service");
        assertNotNull(lookedUpService);
    }

    @Test
    public void testGetAdmin() throws Exception {
        Admin admin = service.getAdmin(admin_key);
        assertNotNull(admin);
        assertEquals("HELLO!", admin.sayHello());
    }

    @Test
    public void testGetAdminFail() throws Exception {
        Admin admin = service.getAdmin("invalid_key");
        assertNull(admin);
    }

    @Test
    public void testCreateItem() throws Exception {
        Admin admin = service.getAdmin(admin_key);
        Item item = admin.createItem("item1", 11);
        assertNotNull(item);
        assertEquals("item1", item.getName());
        assertEquals(11, item.getInitialPrice());
    }

    @Test
    public void testCreateUser() throws Exception {
        Admin admin = service.getAdmin(admin_key);
        User user = admin.createUser("login1", "password1");
        assertNotNull(user);
        assertEquals("login1", user.getId());
    }

    @Test
    public void testCreateAuction() throws Exception {
        Admin admin = service.getAdmin(admin_key);
        Item item = admin.createItem("item2", 22);
        Auction auction = admin.createAuction(item, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
        assertNotNull(auction);
    }

    @Test
    public void testAuctionStatus() throws Exception {
        Admin admin = service.getAdmin(admin_key);
        Item item = admin.createItem("item3", 33);
        Auction auction = admin.createAuction(item, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2));
        assertEquals(auction.getStatus(), AuctionStatus.NEW);
        Thread.sleep(1200);
        assertEquals(auction.getStatus(), AuctionStatus.OPEN);
        Thread.sleep(1200);
        assertEquals(auction.getStatus(), AuctionStatus.CLOSED);
    }

    @Test
    public void testLogin() throws Exception {
        //given
        Admin admin = service.getAdmin(admin_key);
        admin.createUser("login2", "password2");
        //when
        User user = service.login("login2", "password2");
        //then
        assertNotNull(user);
        assertEquals("login2", user.getId());
    }
}
