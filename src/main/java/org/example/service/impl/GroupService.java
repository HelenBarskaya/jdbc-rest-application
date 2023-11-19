package org.example.service.impl;

import org.example.model.Client;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.CoachRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.util.List;

public class GroupService implements SimpleService<Group> {
    GroupRepository groupRepository;
    ClientRepository clientRepository;
    CoachRepository coachRepository;

    public GroupService(GroupRepository groupRepository, ClientRepository clientRepository, CoachRepository coachRepository) {
        this.groupRepository = groupRepository;
        this.clientRepository = clientRepository;
        this.coachRepository = coachRepository;
    }

    @Override
    public Group save(Group entity) {
        return groupRepository.save(entity);
    }

    @Override
    public Group findById(Long id) {
        Group group = groupRepository.findById(id);
        Coach coach = coachRepository.findById(group.getCoach().getId());
        List<Client> clients = group.getClients();

        clients.replaceAll(client -> clientRepository.findById(client.getId()));
        group.setCoach(coach);
        return group;
    }

    public List<Group> findAll() {
        List<Group> groups = groupRepository.findAll();

        for (Group group : groups) {
            Coach coach = coachRepository.findById(group.getCoach().getId());
            group.setCoach(coach);

            List<Client> clients = group.getClients();
            clients.replaceAll(client -> clientRepository.findById(client.getId()));
        }
        return groups;
    }

    public boolean deleteById(Long id) {
        return groupRepository.deleteById(id);
    }

    public Group update(Group group) {
        return groupRepository.update(group);
    }
}
