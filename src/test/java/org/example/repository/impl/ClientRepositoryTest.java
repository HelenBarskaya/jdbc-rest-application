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

    static PostgreSQLContainer<?> postgreSQLContainer;
    ClientRepository clientRepository;

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres")
                .withTag("16.0"))
                .withDatabaseName("postgres-test-db")
                .withUsername("postgre")
                .withPassword("mdzs")
                .withInitScript("scripts/init-test-database.sql");
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void beforeEach() {
        clientRepository = new ClientRepository();
    }

    @AfterEach
    void afterEach() {
        Connection connection = ConnectionManager.getConnection();
        String clearTable = "Delete from clients";
        try (PreparedStatement preparedStatement = connection.prepareStatement(clearTable)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createClientTest() {
        Client client = clientRepository.save(new Client("Анна", "Ахматова", "89379120428"));
        assertEquals(client.getId(), clientRepository.findById(client.getId()).getId());
    }

    @Test
    void deleteClientTest(){
        Client client = clientRepository.save(new Client("Анна", "Ахматова", "89379120428"));
        clientRepository.deleteById(client.getId());
        assertThrows(RuntimeException.class, () -> clientRepository.findById(client.getId()));
    }

    @Test
    void findAllTest(){
        Client client1 = clientRepository.save(new Client("Маргарита", "Мастерова", "86666666666"));
        Client client2 = clientRepository.save(new Client("Анна", "Ахматова", "89379120428"));

        assertEquals(2, clientRepository.findAll().size());
    }

}