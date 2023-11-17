package org.example.service.impl;

import org.example.model.Client;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.sql.SQLException;
import java.util.List;

public class ClientService implements SimpleService<Client> {
    GroupRepository groupRepository = new GroupRepository();
    ClientRepository clientRepository = new ClientRepository();

    @Override
    public Client save(Client entity) throws SQLException {
        return clientRepository.save(entity);
    }

    @Override
    public Client findById(Long id) throws SQLException {
        Client client = clientRepository.findById(id);
        List<Group> groups = client.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            groups.set(i, groupRepository.findById(groups.get(i).getId()));
        }
        return client;
    }

    public List<Client> findAll() throws SQLException {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            List<Group> groups = client.getGroups();
            for (int j = 0; j < groups.size(); j++) {
                groups.set(j, groupRepository.findById(groups.get(j).getId()));
            }
        }
        return clients;
    }

    public boolean deleteById(Long id) throws SQLException {
        return clientRepository.deleteById(id);
    }

    public Client update(Client client) throws SQLException {
        return clientRepository.update(client);
    }

    public Group addGroup(Client client, Group group) throws SQLException {
        return clientRepository.addGroup(client, group);
    }

    public boolean removeGroup(Client client, Group group) throws SQLException {
        return clientRepository.removeGroup(client, group);
    }
}
