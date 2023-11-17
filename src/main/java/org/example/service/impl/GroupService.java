package org.example.service.impl;

import org.example.model.Client;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.sql.SQLException;
import java.util.List;

public class GroupService implements SimpleService<Group> {
    GroupRepository groupRepository = new GroupRepository();
    ClientRepository clientRepository = new ClientRepository();
    CoachRepository coachRepository = new CoachRepository();

    @Override
    public Group save(Group entity) throws SQLException {
        return groupRepository.save(entity);
    }

    @Override
    public Group findById(Long id) throws SQLException {
        Group group = groupRepository.findById(id);
        Coach coach = coachRepository.findById(group.getCoach().getId());
        List<Client> clients = group.getClients();

        for (int i = 0; i < clients.size(); i++) {
            clients.set(i, clientRepository.findById(clients.get(i).getId()));
        }
        group.setCoach(coach);
        return group;
    }

    public List<Group> findAll() throws SQLException {
        List<Group> groups = groupRepository.findAll();

        for (Group group : groups) {
            Coach coach = coachRepository.findById(group.getCoach().getId());
            group.setCoach(coach);

            List<Client> clients = group.getClients();
            for (int j = 0; j < clients.size(); j++) {
                clients.set(j, clientRepository.findById(clients.get(j).getId()));
            }
        }
        return groups;
    }

    public boolean deleteById(Long id) throws SQLException {
        return groupRepository.deleteById(id);
    }

    public Group update(Group group) throws SQLException {
        return groupRepository.update(group);
    }
}
