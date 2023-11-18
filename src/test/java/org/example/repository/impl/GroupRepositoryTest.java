package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.example.model.Group;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GroupRepositoryTest {
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres")
            .withTag("16.0"))
            .withDatabaseName("postgres-test-db")
            .withUsername("test")
            .withPassword("password")
            .withInitScript("scripts/init-test-database.sql");
    ConnectionManager connectionManager = new ConnectionManager();
    GroupRepository groupRepository;
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
        groupRepository = new GroupRepository(connectionManager);
        coachRepository = new CoachRepository(connectionManager);
    }

    @AfterEach
    void clear() {
        Connection connection = connectionManager.getConnection();
        String clearTable = "Delete from groups";
        try (PreparedStatement preparedStatement = connection.prepareStatement(clearTable)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void createGroupTest(){
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        Group group = groupRepository.save(new Group("Растяжка", coach));

        assertEquals(group, groupRepository.findById(group.getId()));
    }

    @Test
    void deleteGroupTest(){
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        Group group = groupRepository.save(new Group("Растяжка", coach));

        groupRepository.deleteById(group.getId());

        assertNotNull(coachRepository.findById(coach.getId()));
        assertNull(groupRepository.findById(group.getId()));
    }

    @Test
    void findAllTest(){
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        Group group1 = groupRepository.save(new Group("Растяжка", coach));
        Group group2 = groupRepository.save(new Group("Пилатес", coach));

        assertEquals(2, groupRepository.findAll().size());
        assertEquals(group1, groupRepository.findById(group1.getId()));
        assertEquals(group2, groupRepository.findById(group2.getId()));
    }

    @Test
    void updateTest(){
        Coach coach = coachRepository.save(new Coach("Анна", "Ахматова", "89379120428"));
        Group group = groupRepository.save(new Group("Растяжка", coach));
        group.setName("Пилатес");
        groupRepository.update(group);

        assertEquals(group, groupRepository.findById(group.getId()));
    }
}