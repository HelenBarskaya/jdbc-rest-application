package org.example.service.impl;

import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.sql.SQLException;
import java.util.List;

public class CoachService implements SimpleService<Coach> {
    CoachRepository coachRepository = new CoachRepository();
    GroupRepository groupRepository = new GroupRepository();

    @Override
    public Coach save(Coach entity) throws SQLException {
        return coachRepository.save(entity);
    }

    @Override
    public Coach findById(Long id) throws SQLException {
        Coach coach = coachRepository.findById(id);
        List<Group> groups = coach.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            groups.set(i, groupRepository.findById(groups.get(i).getId()));
        }
        return coach;
    }

    public List<Coach> findAll() throws SQLException {
        List<Coach> coaches = coachRepository.findAll();
        for (Coach coach : coaches) {
            List<Group> groups = coach.getGroups();
            for (int j = 0; j < groups.size(); j++) {
                groups.set(j, groupRepository.findById(groups.get(j).getId()));
            }
        }
        return coaches;
    }

    public boolean deleteById(Long id) throws SQLException {
        return coachRepository.deleteById(id);
    }

    public Coach changePhoneNumber(Coach coach) throws SQLException {
        return coachRepository.changePhoneNumber(coach);
    }
}
