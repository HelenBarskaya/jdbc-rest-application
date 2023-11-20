package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    Coach coach;

    @BeforeEach
    void initMethod() {
        coach = new Coach();
    }

    @Test
    void getAndSetIdTest() {
        assertEquals(0, coach.getId());
        coach.setId(1L);
        assertEquals(1L, coach.getId());
    }

    @Test
    void getAndSetFirstNameTest() {
        assertNull(coach.getFirstName());
        coach.setFirstName("Анна");
        assertEquals("Анна", coach.getFirstName());
    }

    @Test
    void getAndSetLastNameTest() {
        assertNull(coach.getLastName());
        coach.setLastName("Морозова");
        assertEquals("Морозова", coach.getLastName());
    }

    @Test
    void getAndSetNumberPhoneTest() {
        assertNull(coach.getPhoneNumber());
        coach.setPhoneNumber("89379127450");
        assertEquals("89379127450", coach.getPhoneNumber());
    }

    @Test
    void getAndSetGroupsTest() {
        List<Group> groups = new ArrayList<>();
        Group group = new Group();
        assertTrue(coach.getGroups().isEmpty());

        groups.add(group);
        coach.setGroups(groups);
        assertArrayEquals(groups.toArray(), coach.getGroups().toArray());
    }
}
