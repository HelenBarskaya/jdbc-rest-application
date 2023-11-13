package org.example.service.impl;

import org.example.model.Client;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.util.List;
import java.util.UUID;

public class ClientService implements SimpleService<Client> {
    GroupRepository groupRepository = new GroupRepository();
    ClientRepository clientRepository= new ClientRepository();

    @Override
    public Client save(Client entity) {
        List<Group> groupList = entity.getGroups();
        for (Group g: groupList) {
            groupRepository.save(g);
        }
        return new Client();
    }

    @Override
    public Client findById(UUID uuid) {
        return null;
    }
}
