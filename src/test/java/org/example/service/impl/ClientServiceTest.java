package org.example.service.impl;

import org.example.model.Client;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ClientServiceTest {
    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);
    ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, groupRepository);
    }

    @Test
    void findByIdTest() {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        Mockito.when(clientRepository.findById(1L)).thenReturn(client);

        assertEquals(client, clientService.findById(client.getId()));
    }

    @Test
    void findAllTest() {
        Client client1 = new Client("Маргарита", "Мастерова", "86666666666");
        client1.setId(1);

        Client client2 = new Client("Анна", "Ахматова", "89379120428");
        client2.setId(2);

        List<Client> clients = new ArrayList<>();
        clients.add(client1);
        clients.add(client2);

        Mockito.when(clientRepository.findAll()).thenReturn(clients);

        assertArrayEquals(clients.toArray(), clientService.findAll().toArray());
    }

    @Test
    void saveTest() {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        Mockito.when(clientRepository.save(client)).thenReturn(client);

        assertEquals(client, clientService.save(client));
    }

    @Test
    void deleteByIdTest() {
        Mockito.when(clientRepository.deleteById(1L)).thenReturn(true);

        assertTrue(clientService.deleteById(1L));
    }

    @Test
    void updateTest() {
        Client client = new Client("Маргарита", "Мастерова", "86666666666");
        client.setId(1);

        Mockito.when(clientRepository.update(client)).thenReturn(client);

        assertEquals(client, clientService.update(client));
    }
}