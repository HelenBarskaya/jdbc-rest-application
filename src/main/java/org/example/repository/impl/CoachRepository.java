package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoachRepository implements SimpleRepository<Coach, Long> {

    private final Connection connection;
    private final GroupRepository groupRepository = new GroupRepository();

    public CoachRepository() {
        connection = ConnectionManager.getConnection();
    }

    @Override
    public Coach findById(Long id) {
        Coach coach = new Coach();
        List<Group> groups = new ArrayList<>();
        String selectById = "select c.id, c.firstname, c.lastname, c.phone_number, g.id from coaches c " +
                "left join groups g on c.id=g.id_coach where c.id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectById)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                coach.setId(rs.getLong(1));
                coach.setFirstName(rs.getString(2));
                coach.setLastName(rs.getString(3));
                coach.setPhoneNumber(rs.getString(4));
                Group group = new Group();
                group.setId(rs.getLong(5));
                groups.add(group);
            }

            coach.setGroups(groups);
            return coach;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) {
        groupRepository.deleteById(id);
        String deleteById = "delete from coaches where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Coach> findAll() {
        String getAllId = "select c.id from coaches c left join groups g on c.id=g.id_coach group by c.id";
        try (PreparedStatement stmt = connection.prepareStatement(getAllId)) {
            ResultSet rs = stmt.executeQuery();

            List<Long> indexes = new ArrayList<>();
            while (rs.next()) {
                indexes.add(rs.getLong("id"));
            }

            List<Coach> coaches = new ArrayList<>();
            for (Long index : indexes) {
                coaches.add(findById(index));
            }
            return coaches;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Coach save(Coach coach) {
        String saveCoach = "insert into coaches(firstname, lastname,phone_number) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(saveCoach)) {
            preparedStatement.setString(1, coach.getFirstName());
            preparedStatement.setString(2, coach.getLastName());
            preparedStatement.setString(3, coach.getPhoneNumber());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coach;
    }
}
