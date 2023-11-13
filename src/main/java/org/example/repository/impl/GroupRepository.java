package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Client;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

            while (rs.next()) {
                group.setId(rs.getLong(1));
                group.setName(rs.getString(2));
                coach.setId(rs.getLong(3));
                Client client = new Client();
                client.setId(rs.getLong(4));
                clients.add(client);
            }
            group.setCoach(coach);
            group.setClients(clients);

            return group;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        String removeLinks = "delete from clients_groups where id_group = ?;";
        String deleteById = "delete from groups where id=?";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(removeLinks + deleteById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setLong(2, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveGroup)) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setLong(2, group.getCoach().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public boolean changeCoach(Group group, Coach newCoach) {
        String setNewCoach = "update groups set id_coach=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(setNewCoach)) {
            preparedStatement.setLong(1, newCoach.getId());
            preparedStatement.setLong(2, group.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
