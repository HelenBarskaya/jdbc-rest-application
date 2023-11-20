package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Client;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClientRepositoryTest {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres")
            .withTag("16.0"))
            .withDatabaseName("postgres-test-db")
            .withUsername("test")
            .withPassword("password")
            .withInitScript("scripts/init-test-database.sql");
    ConnectionManager connectionManager = new ConnectionManager();
    ClientRepository clientRepository;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void setUp() {
        connectionManager = new ConnectionManager();
        connectionManager.setUrl(postgreSQLContainer.getJdbcUrl());
        connectionManager.setUsername(postgreSQLContainer.getUsername());
        connectionManager.setPassword(postgreSQLContainer.getPassword());
        clientRepository = new ClientRepository(connectionManager);
    }

    @AfterEach
    void clear() {
        Connection connection = connectionManager.getConnection();
        String clearTable = "Delete from clients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(clearTable)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createClientTest() {
        Client client = clientRepository.save(
                new Client("Анна", "Ахматова", "89379120428")
        );
        assertEquals(client, clientRepository.findById(client.getId()));
    }

    @Test
    void deleteClientTest() {
        Client client = clientRepository.save(
                new Client("Анна", "Ахматова", "89379120428")
        );
        clientRepository.deleteById(client.getId());
        assertThrows(IllegalArgumentException.class, () -> clientRepository.findById(client.getId()));
    }

    @Test
    void findAllTest() {
        Client client1 = clientRepository.save(
                new Client("Маргарита", "Мастерова", "86666666666")
        );
        Client client2 = clientRepository.save(
                new Client("Анна", "Ахматова", "89379120428")
        );

        assertEquals(2, clientRepository.findAll().size());
        assertEquals(client1, clientRepository.findById(client1.getId()));
        assertEquals(client2, clientRepository.findById(client2.getId()));
    }

    @Test
    void updateTest() {
        Client client = clientRepository.save(
                new Client("Маргарита", "Мастерова", "86666666666")
        );
        client.setFirstName("Мастер");
        client.setLastName("Маргаритов");
        clientRepository.update(client);

        assertEquals(client, clientRepository.findById(client.getId()));
    }
}
