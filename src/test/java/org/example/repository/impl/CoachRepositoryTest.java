package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class CoachRepositoryTest {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres")
            .withTag("16.0"))
            .withDatabaseName("postgres-test-db")
            .withUsername("test")
            .withPassword("password")
            .withInitScript("scripts/init-test-database.sql");
    ConnectionManager connectionManager = new ConnectionManager();
    CoachRepository coachRepository;

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
        coachRepository = new CoachRepository(connectionManager);
    }

    @AfterEach
    void clear() {
        Connection connection = connectionManager.getConnection();
        String clearTable = "Delete from coaches";
        try (PreparedStatement preparedStatement = connection.prepareStatement(clearTable)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createCoachTest() {
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        assertEquals(coach, coachRepository.findById(coach.getId()));
    }

    @Test
    void deleteCoachTest(){
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        coachRepository.deleteById(coach.getId());
        assertNull(coachRepository.findById(coach.getId()));
    }

    @Test
    void findAllTest(){
        Coach coach1 = coachRepository.save(new Coach("Маргарита", "Мастерова", "86666666666"));
        Coach coach2 = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));

        assertEquals(2, coachRepository.findAll().size());
        assertEquals(coach1, coachRepository.findById(coach1.getId()));
        assertEquals(coach2, coachRepository.findById(coach2.getId()));
    }

    @Test
    void updateTest(){
        Coach coach = coachRepository.save(new Coach("Маргарита", "Мастерова", "86666666666"));
        coach.setFirstName("Мастер");
        coach.setLastName("Маргаритов");
        coachRepository.update(coach);

        assertEquals(coach, coachRepository.findById(coach.getId()));
    }
}