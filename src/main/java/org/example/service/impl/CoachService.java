package org.example.service.impl;

import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.util.List;

public class CoachService implements SimpleService<Coach> {

    private final CoachRepository coachRepository;
    private final GroupRepository groupRepository;

    public CoachService(CoachRepository coachRepository, GroupRepository groupRepository) {
        this.coachRepository = coachRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public Coach save(Coach entity) {
        return coachRepository.save(entity);
    }

    @Override
    public Coach findById(Long id) {
        Coach coach = coachRepository.findById(id);
        replaceGroups(coach);
        return coach;
    }

    public List<Coach> findAll() {
        List<Coach> coaches = coachRepository.findAll();
        for (Coach coach : coaches) {
            replaceGroups(coach);
        }
        return coaches;
    }

    public boolean deleteById(Long id) {
        return coachRepository.deleteById(id);
    }

    public Coach update(Coach coach) {
        return coachRepository.update(coach);
    }

    private void replaceGroups(Coach coach) {
        List<Group> groups = coach.getGroups();
        groups.replaceAll(group -> groupRepository.findById(group.getId()));
    }
}
