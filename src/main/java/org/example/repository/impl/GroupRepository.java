package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Client;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository implements SimpleRepository<Group, Long> {

    private final Connection connection;

    public GroupRepository() {
        connection = ConnectionManager.getConnection();
    }

    @Override
    public Group findById(Long id) {
        Group group = new Group();
        Coach coach = new Coach();
        List<Client> clients = new ArrayList<>();

        String findById = "select g.id, g.name, g.id_coach, c.id from groups g left join clients_groups cg " +
                "on g.id = cg.id_group left join clients c on cg.id_client = c.id where g.id = ?\n";

        try (PreparedStatement preparedStatement = connection.prepareStatement(findById)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                do {
                    group.setId(rs.getLong(1));
                    group.setName(rs.getString(2));
                    coach.setId(rs.getLong(3));
                    Client client = new Client();
                    client.setId(rs.getLong(4));
                    clients.add(client);
                } while (rs.next());
                group.setCoach(coach);
                group.setClients(clients);
                return group;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        String removeLinks = "delete from clients_groups where id_group = ?;";
        String deleteById = "delete from groups where id=?";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(removeLinks + deleteById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Group> findAll() {
        String getAllId = "select g.id from groups g left join clients_groups cg on g.id=cg.id_group group by g.id";

        try (PreparedStatement stmt = connection.prepareStatement(getAllId)) {
            ResultSet rs = stmt.executeQuery();

            List<Long> indexes = new ArrayList<>();
            while (rs.next()) {
                indexes.add(rs.getLong("id"));
            }

            List<Group> groups = new ArrayList<>();
            for (Long index : indexes) {
                groups.add(findById(index));
            }
            return groups;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Group save(Group group) {
        String saveGroup = "insert into groups(name, id_coach) values (?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveGroup, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setLong(2, group.getCoach().getId());
            preparedStatement.executeQuery();

            Group savedGroup = new Group();
            try (ResultSet key = preparedStatement.getGeneratedKeys()) {
                Coach coach = new Coach();
                coach.setId(key.getLong("id_coach"));

                savedGroup.setId(key.getLong("id"));
                savedGroup.setName(key.getString("name"));
                savedGroup.setCoach(coach);

                return savedGroup;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Group update(Group group) {
        String updateGroup = "Update groups set name=?, id_coach=? where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateGroup)) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setLong(2, group.getCoach().getId());
            preparedStatement.setLong(3, group.getId());
            int resp = preparedStatement.executeUpdate();

            if (resp == 0) throw new SQLException();

            return group;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
