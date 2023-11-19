package org.example.service.impl;

import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GroupServiceTest {
    GroupService groupService;
    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);
    ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
    CoachRepository coachRepository = Mockito.mock(CoachRepository.class);

    @BeforeEach
    void setUp() {
        groupService = new GroupService(groupRepository, clientRepository, coachRepository);
    }

    @Test
    void findByIdTest() {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        Mockito.when(groupRepository.findById(1L)).thenReturn(group);

        assertEquals(group, groupService.findById(group.getId()));
    }

    @Test
    void findAllTest() {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group("Растяжка", coach));
        groups.add(new Group("Пилатес", coach));

        Mockito.when(groupRepository.findAll()).thenReturn(groups);

        assertArrayEquals(groups.toArray(), groupService.findAll().toArray());
    }

    @Test
    void saveTest() {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        Mockito.when(groupRepository.save(group)).thenReturn(group);

        assertEquals(group, groupService.save(group));
    }

    @Test
    void deleteByIdTest() {
        Mockito.when(groupRepository.deleteById(1L)).thenReturn(true);

        assertTrue(groupService.deleteById(1L));
    }

    @Test
    void updateTest() {
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Group group = new Group("Растяжка", coach);
        group.setId(1);

        Mockito.when(groupRepository.update(group)).thenReturn(group);

        assertEquals(group, groupService.update(group));
    }

}