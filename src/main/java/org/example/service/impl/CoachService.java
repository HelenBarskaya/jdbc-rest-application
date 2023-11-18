package org.example.service.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.util.List;

public class CoachService implements SimpleService<Coach> {
    ConnectionManager connectionManager;
    CoachRepository coachRepository;
    GroupRepository groupRepository;

    public CoachService(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        coachRepository = new CoachRepository(connectionManager);
        groupRepository = new GroupRepository(connectionManager);
    }

    @Override
    public Coach save(Coach entity){
        return coachRepository.save(entity);
    }

    @Override
    public Coach findById(Long id){
        Coach coach = coachRepository.findById(id);
        List<Group> groups = coach.getGroups();
        groups.replaceAll(group -> groupRepository.findById(group.getId()));
        return coach;
    }

    public List<Coach> findAll(){
        List<Coach> coaches = coachRepository.findAll();
        for (Coach coach : coaches) {
            List<Group> groups = coach.getGroups();
            groups.replaceAll(group -> groupRepository.findById(group.getId()));
        }
        return coaches;
    }

    public boolean deleteById(Long id){
        return coachRepository.deleteById(id);
    }

    public Coach update(Coach coach){
        return coachRepository.update(coach);
    }
}
