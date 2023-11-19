package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Client;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository implements SimpleRepository<Client, Long> {

    private static final String SELECT_BY_ID_COMMAND =
            "select c.id, c.firstname, c.lastname, c.phone_number, g.id from clients c " +
                    "left join clients_groups cg on c.id = cg.id_client left join groups g on cg.id_group = g.id " +
                    "where c.id = ?";
    private static final String GET_ALL_ID = "select c.id from clients c " +
            "left join clients_groups cg on c.id = cg.id_client group by c.id";

    private static final String REMOVE_REFS_COMMAND = "delete from clients_groups where id_client = ?;";
    private static final String DELETE_BY_ID_COMMAND = "delete from clients where id = ?";

    private static final String SAVE_CLIENT = "insert into clients(firstname, lastname,phone_number) values (?, ?, ?)";
    private static final String UPDATE_CLIENT = "update clients set firstname = ?, lastname = ?, phone_number = ? " +
            "where id = ?";
    private static final String ADD_GROUP = "insert into clients_groups(id_client, id_group) values (?, ?)";

    private final Connection connection;

    public ClientRepository(ConnectionManager connectionManager) {
        connection = connectionManager.getConnection();
    }

    @Override
    public Client findById(Long id) {
        Client client = new Client();
        List<Group> groups = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_COMMAND)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    client.setId(resultSet.getLong(1));
                    client.setFirstName(resultSet.getString(2));
                    client.setLastName(resultSet.getString(3));
                    client.setPhoneNumber(resultSet.getString(4));

                    Group group = new Group();
                    group.setId(resultSet.getLong(5));
                    groups.add(group);
                } while (resultSet.next());

                client.setGroups(groups);
                return client;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(REMOVE_REFS_COMMAND + DELETE_BY_ID_COMMAND)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return true;
    }

    @Override
    public List<Client> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ID)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Long> indexes = new ArrayList<>();
            while (resultSet.next()) {
                indexes.add(resultSet.getLong("id"));
            }

            List<Client> result = new ArrayList<>();
            for (Long index : indexes) {
                Client client = findById(index);
                result.add(client);
            }

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Client save(Client client) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SAVE_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getPhoneNumber());
            preparedStatement.executeUpdate();

            return returnSavedClient(preparedStatement);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Client update(Client client) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT)) {
            preparedStatement.setString(1, client.getFirstName());
            preparedStatement.setString(2, client.getLastName());
            preparedStatement.setString(3, client.getPhoneNumber());
            preparedStatement.setLong(4, client.getId());
            int resp = preparedStatement.executeUpdate();

            if (resp == 0) {
                throw new SQLException();
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return client;
    }

    public Group addGroup(Client client, Group group) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_GROUP)) {
            preparedStatement.setLong(1, client.getId());
            preparedStatement.setLong(2, group.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return group;
    }

    public boolean removeGroup(Client client, Group group) {
        // TODO: 19.11.2023 make static private
        String addGroup = "delete from clients_groups where id_client = ? and id_group = ?";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(addGroup)) {
            preparedStatement.setLong(1, client.getId());
            preparedStatement.setLong(2, group.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Client returnSavedClient(PreparedStatement preparedStatement) {
        Client updatedClient = new Client();

        try (ResultSet key = preparedStatement.getGeneratedKeys()) {
            key.next();
            updatedClient.setId(key.getLong("id"));
            updatedClient.setFirstName(key.getString("firstname"));
            updatedClient.setLastName(key.getString("lastname"));
            updatedClient.setPhoneNumber(key.getString("phone_number"));
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return updatedClient;
    }
}
