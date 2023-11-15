package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Client;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository implements SimpleRepository<Client, Long> {

    private final Connection connection;

    public ClientRepository() {
        connection = ConnectionManager.getConnection();
    }

    public Client changePhoneNumber(Client client) throws SQLException {
        String updatePhoneNumber = "update clients set phone_number=? where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePhoneNumber,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getPhoneNumber());
            preparedStatement.setLong(2, client.getId());
            preparedStatement.execute();

            return returnSavedClient(preparedStatement);
        }
    }

    @Override
    public Client findById(Long id) throws SQLException {
        Client client = new Client();
        List<Group> groups = new ArrayList<>();

        String selectById = "select c.id, c.firstname, c.lastname, c.phone_number, g.id from clients c " +
                "left join clients_groups cg on c.id = cg.id_client left join groups g on cg.id_group = g.id " +
                "where c.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectById)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                client.setId(rs.getLong(1));
                client.setFirstName(rs.getString(2));
                client.setLastName(rs.getString(3));
                client.setPhoneNumber(rs.getString(4));

                Group group = new Group();
                group.setId(rs.getLong(5));
                groups.add(group);
            }
            client.setGroups(groups);
            return client;
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        String removeLinks = "delete from clients_groups where id_client = ?;";
        String deleteById = "delete from clients where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeLinks + deleteById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, id);
            return preparedStatement.execute();
        }
    }

    @Override
    public List<Client> findAll() throws SQLException {
        String getAllId = "select c.id from clients c left join clients_groups cg on c.id=cg.id_client group by c.id";
        try (PreparedStatement stmt = connection.prepareStatement(getAllId)) {
            ResultSet rs = stmt.executeQuery();

            List<Long> indexes = new ArrayList<>();
            while (rs.next()) {
                indexes.add(rs.getLong("id"));
            }

            List<Client> result = new ArrayList<>();
            for (Long index : indexes) {
                Client client = findById(index);
                result.add(client);
            }
            return result;
        }
    }

    @Override
    public Client save(Client client) throws SQLException {
        String saveClient = "insert into clients(firstname, lastname,phone_number) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveClient, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getPhoneNumber());
            preparedStatement.executeUpdate();

            return returnSavedClient(preparedStatement);
        }
    }

    public Group addGroup(Client client, Group group) throws SQLException {
        String addGroup = "insert into clients_groups(id_client, id_group) values (?, ?)";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(addGroup)) {
            preparedStatement.setLong(1, client.getId());
            preparedStatement.setLong(2, group.getId());
            preparedStatement.execute();
        }
        return group;
    }

    public boolean removeGroup(Client client, Group group) throws SQLException {
        String addGroup = "delete from clients_groups where id_client = ? and id_group = ?";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(addGroup)) {
            preparedStatement.setLong(1, client.getId());
            preparedStatement.setLong(2, group.getId());
            return preparedStatement.execute();
        }
    }

    private Client returnSavedClient(PreparedStatement preparedStatement) throws SQLException {
        Client updatedClient = new Client();
        try (ResultSet key = preparedStatement.getGeneratedKeys()) {
            key.next();
            updatedClient.setId(key.getLong("id"));
            updatedClient.setFirstName(key.getString("firstname"));
            updatedClient.setLastName(key.getString("lastname"));
            updatedClient.setPhoneNumber(key.getString("phone_number"));
        }
        return updatedClient;
    }
}
