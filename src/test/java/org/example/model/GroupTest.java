package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupTest {

    Group group;

    @BeforeEach
    void initMethod() {
        group = new Group();
    }

    @Test
    void getAndSetIdTest() {
        assertEquals(0, group.getId());
        group.setId(1L);
        assertEquals(1L, group.getId());
    }

    @Test
    void getAndSetNameTest() {
        assertNull(group.getName());
        group.setName("Кикбоксинг");
        assertEquals("Кикбоксинг", group.getName());
    }

    @Test
    void getAndSetCoachTest() {
        assertNull(group.getCoach());
        Coach coach = new Coach();

        group.setCoach(coach);
        assertEquals(coach, group.getCoach());
    }

    @Test
    void getAndSetClientsTest() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();
        assertTrue(client.getGroups().isEmpty());

        clients.add(client);
        group.setClients(clients);
        assertArrayEquals(clients.toArray(), group.getClients().toArray());
    }
}
