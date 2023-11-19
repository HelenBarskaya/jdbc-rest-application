package org.example.service.impl;

import org.example.model.Coach;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoachServiceTest {

    CoachService coachService;
    CoachRepository coachRepository = Mockito.mock(CoachRepository.class);
    GroupRepository groupRepository = Mockito.mock(GroupRepository.class);

    @BeforeEach
    void setUp(){
        coachService = new CoachService(coachRepository,groupRepository);
    }

    @Test
    void findByIdTest(){
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Mockito.when(coachRepository.findById(1L)).thenReturn(coach);

        assertEquals(coach, coachService.findById(coach.getId()));
    }

    @Test
    void findAllTest(){
        Coach coach1 = new Coach("Маргарита", "Мастерова", "86666666666");
        coach1.setId(1);

        Coach coach2 = new Coach("Анна", "Ахматова", "89379120428");
        coach2.setId(2);

        List<Coach> coaches = new ArrayList<>();
        coaches.add(coach1);
        coaches.add(coach2);

        Mockito.when(coachRepository.findAll()).thenReturn(coaches);

        assertArrayEquals(coaches.toArray(), coachService.findAll().toArray());
    }

    @Test
    void saveTest(){
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Mockito.when(coachRepository.save(coach)).thenReturn(coach);

        assertEquals(coach, coachService.save(coach));
    }

    @Test
    void deleteByIdTest(){
        Mockito.when(coachRepository.deleteById(1L)).thenReturn(true);

        assertTrue(coachService.deleteById(1L));
    }

    @Test
    void updateTest(){
        Coach coach = new Coach("Маргарита", "Мастерова", "86666666666");
        coach.setId(1);

        Mockito.when(coachRepository.update(coach)).thenReturn(coach);

        assertEquals(coach, coachService.update(coach));
    }
}
