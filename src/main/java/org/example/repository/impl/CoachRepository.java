package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachRepository implements SimpleRepository<Coach, Long> {

    private final Connection connection;
    private final GroupRepository groupRepository = new GroupRepository();

    public CoachRepository() {
        connection = ConnectionManager.getConnection();
    }

    @Override
    public Coach findById(Long id) throws SQLException {
        Coach coach = new Coach();
        List<Group> groups = new ArrayList<>();

        String selectById = "select c.id, c.firstname, c.lastname, c.phone_number, g.id from coaches c " +
                "left join groups g on c.id=g.id_coach where c.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectById)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                do {
                    coach.setId(rs.getLong(1));
                    coach.setFirstName(rs.getString(2));
                    coach.setLastName(rs.getString(3));
                    coach.setPhoneNumber(rs.getString(4));
                    Group group = new Group();
                    group.setId(rs.getLong(5));
                    groups.add(group);
                } while (rs.next());
                coach.setGroups(groups);
                return coach;
            } else throw new SQLException();
        }
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        groupRepository.deleteById(id);
        String deleteById = "delete from coaches where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            return true;
        }
    }

    @Override
    public List<Coach> findAll() throws SQLException {
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
        }
    }

    @Override
    public Coach save(Coach coach) throws SQLException {
        String saveCoach = "insert into coaches(firstname, lastname,phone_number) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection
                .prepareStatement(saveCoach, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, coach.getFirstName());
            preparedStatement.setString(2, coach.getLastName());
            preparedStatement.setString(3, coach.getPhoneNumber());
            preparedStatement.executeUpdate();

            return returnSavedCoach(preparedStatement);
        }
    }

    public Coach update(Coach coach) throws SQLException {
        String updateClient = "Update coaches set firstname=?, lastname=?, phone_number=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateClient)) {
            preparedStatement.setString(1, coach.getFirstName());
            preparedStatement.setString(2, coach.getLastName());
            preparedStatement.setString(3, coach.getPhoneNumber());
            preparedStatement.setLong(4, coach.getId());
            int resp = preparedStatement.executeUpdate();

            if (resp == 0) throw new SQLException();

            return coach;
        }
    }

    public Coach changePhoneNumber(Coach coach) throws SQLException {
        String updatePhoneNumber = "update coaches set phone_number=? where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePhoneNumber,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, coach.getPhoneNumber());
            preparedStatement.setLong(2, coach.getId());
            preparedStatement.execute();

            return returnSavedCoach(preparedStatement);
        }
    }

    private Coach returnSavedCoach(PreparedStatement preparedStatement) throws SQLException {
        Coach savedCoach = new Coach();
        try(ResultSet key = preparedStatement.getGeneratedKeys()) {
            key.next();
            savedCoach.setId(key.getLong("id"));
            savedCoach.setFirstName(key.getString("firstname"));
            savedCoach.setLastName(key.getString("lastname"));
            savedCoach.setPhoneNumber(key.getString("phone_number"));
        }
        return savedCoach;
    }
}
