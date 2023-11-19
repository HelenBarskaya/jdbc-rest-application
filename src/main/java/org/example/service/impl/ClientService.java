package org.example.service.impl;

import org.example.model.Client;
import org.example.model.Group;
import org.example.repository.impl.ClientRepository;
import org.example.repository.impl.GroupRepository;
import org.example.service.SimpleService;

import java.util.List;

public class ClientService implements SimpleService<Client> {

    private final GroupRepository groupRepository;
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository, GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client save(Client entity) {
        return clientRepository.save(entity);
    }

    @Override
    public Client findById(Long id) {
        Client client = clientRepository.findById(id);
        replaceGroups(client);
        return client;
    }

    public List<Client> findAll() {
        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            replaceGroups(client);
        }

        return clients;
    }

    public boolean deleteById(Long id) {
        return clientRepository.deleteById(id);
    }

    public Client update(Client client) {
        return clientRepository.update(client);
    }

    public Group addGroup(Client client, Group group) {
        return clientRepository.addGroup(client, group);
    }

    public boolean removeGroup(Client client, Group group) {
        return clientRepository.removeGroup(client, group);
    }

    private void replaceGroups(Client client) {
        List<Group> groups = client.getGroups();
        groups.replaceAll(group -> groupRepository.findById(group.getId()));
    }
}
