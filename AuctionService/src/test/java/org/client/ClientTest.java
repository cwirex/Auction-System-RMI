package org.client;

import org.aslib.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class ClientTest {
    private static Registry registry;
    private static Service service;
    private static Item item;
    private static User user;
    private static Auction auction;
    private static final String admin_key = "admin_crypto_key";
    private static Admin admin;

    @BeforeClass
    public static void setUp() throws Exception {
        registry = LocateRegistry.createRegistry(8899);
        service = new org.server.ServiceImpl();
        Naming.rebind("rmi://127.0.0.1:8899/service", service);
        admin = service.getAdmin(admin_key);
        user = admin.createUser("user", "password");
        item = admin.createItem("item", 100);
        auction = admin.createAuction(item, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // Unbind the service and stop the registry
        Naming.unbind("rmi://127.0.0.1:8899/service");
        UnicastRemoteObject.unexportObject(service, true);
        UnicastRemoteObject.unexportObject(registry, true);
    }

    @Test
    public void testUserRefill() throws Exception {
        //given
        int init = user.getCredits();
        int amount = 1000;
        //when
        boolean success = user.refillCredits("210012", amount);
        //then
        assertTrue(success);
        assertEquals(init + amount, user.getCredits());
    }

    @Test
    public void testUserRefillInvalidBL1K() throws Exception {
        //given
        int init = user.getCredits();
        int amount = 1000;
        //when
        boolean success = user.refillCredits("not a blik code", amount);
        //then
        assertFalse(success);
        assertEquals(init, user.getCredits());
    }

    @Test
    public void testUserPlaceBid() throws Exception {
        //given
        int init = user.getCredits();
        int amount = 1000;
        user.refillCredits("231799", amount);
        //when
        assertTrue(user.makeBid(auction, 1000));
        assertTrue(auction.getBidders().containsKey(user.getId()));
    }

    @Test
    public void testUserClaimItem() throws Exception {
        //given
       Auction quickAuction = admin.createAuction(item, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1));
       user.refillCredits("210012", 2000);
       Thread.sleep(100);

       //when
        user.makeBid(quickAuction, 2000);
        Thread.sleep(1000);

        //then
        assertFalse(user.getItems().isEmpty());
        assertTrue(user.getItems().contains(item));
    }

    @Test
    public void testMultipleBidders() throws Exception {
        //given
        User u1 = admin.createUser("m_user_1", "password");
        User u2 = admin.createUser("m_user_2", "password");
        User u3 = admin.createUser("m_user_3", "password");
        u1.refillCredits("123456", 100);
        u2.refillCredits("123456", 10);
        u3.refillCredits("123456", 1);
        Auction mAuction = admin.createAuction(item, LocalDateTime.now(), LocalDateTime.now().plusSeconds(1));
        Thread.sleep(10);

        // when scenario
        assertFalse(u3.makeBid(mAuction, 10));  // not enough credits!
        Thread.sleep(10);
        assertTrue(u3.makeBid(mAuction, 1));    // ok
        Thread.sleep(10);
        assertTrue(u1.makeBid(mAuction, 5));    // ok
        Thread.sleep(10);
        assertFalse(u2.makeBid(mAuction, 5));   // the same bid value!
        Thread.sleep(10);
        assertTrue(u2.makeBid(mAuction, 10));   // ok
        Thread.sleep(10);
        assertTrue(u1.makeBid(mAuction, 20));   // ok
        Thread.sleep(1000);
        assertFalse(u1.makeBid(mAuction, 50));  // auction closed!

        // then
        assertTrue(u1.getItems().contains(item));
        assertEquals(80, u1.getCredits());
        assertEquals(10, u2.getCredits());
        assertEquals(1, u3.getCredits());
    }
}
