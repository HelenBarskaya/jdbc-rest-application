package org.example.repository.impl;

import org.example.database.ConnectionManager;
import org.example.model.Coach;
import org.example.model.Group;
import org.example.repository.SimpleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachRepository implements SimpleRepository<Coach, Long> {

    private static final String SELECT_BY_ID_COMMAND = "select c.id, c.firstname, c.lastname, c.phone_number, g.id " +
            "from coaches c left join groups g on c.id=g.id_coach where c.id=?";
    private static final String GET_ALL_ID_COMMAND = "select c.id from coaches c " +
            "left join groups g on c.id=g.id_coach group by c.id";

    private static final String DELETE_BY_ID_COMMAND = "delete from coaches where id=?";

    private static final String SAVE_COACH_COMMAND = "insert into coaches(firstname, lastname,phone_number) " +
            "values (?, ?, ?)";
    private static final String UPDATE_CLIENT_COMMAND = "Update coaches set firstname=?, lastname=?, phone_number=? " +
            "where id=?";

    private final Connection connection;
    private final GroupRepository groupRepository;

    public CoachRepository(ConnectionManager connectionManager) {
        groupRepository = new GroupRepository(connectionManager);
        connection = connectionManager.getConnection();
    }

    @Override
    public Coach findById(Long id) {
        Coach coach = new Coach();
        List<Group> groups = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID_COMMAND)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                do {
                    coach.setId(resultSet.getLong(1));
                    coach.setFirstName(resultSet.getString(2));
                    coach.setLastName(resultSet.getString(3));
                    coach.setPhoneNumber(resultSet.getString(4));

                    Group group = new Group();
                    group.setId(resultSet.getLong(5));
                    groups.add(group);
                } while (resultSet.next());

                coach.setGroups(groups);
                return coach;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        groupRepository.deleteById(id);

        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_COMMAND)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return true;
    }

    @Override
    public List<Coach> findAll() {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ID_COMMAND)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Long> indexes = new ArrayList<>();
            while (resultSet.next()) {
                indexes.add(resultSet.getLong("id"));
            }

            List<Coach> coaches = new ArrayList<>();
            for (Long index : indexes) {
                coaches.add(findById(index));
            }

            return coaches;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Coach save(Coach coach) {
        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(SAVE_COACH_COMMAND, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, coach.getFirstName());
            preparedStatement.setString(2, coach.getLastName());
            preparedStatement.setString(3, coach.getPhoneNumber());
            preparedStatement.executeUpdate();

            return returnSavedCoach(preparedStatement);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Coach update(Coach coach) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_COMMAND)) {
            preparedStatement.setString(1, coach.getFirstName());
            preparedStatement.setString(2, coach.getLastName());
            preparedStatement.setString(3, coach.getPhoneNumber());
            preparedStatement.setLong(4, coach.getId());
            int resp = preparedStatement.executeUpdate();

            if (resp == 0) {
                throw new SQLException();
            }

            return coach;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private Coach returnSavedCoach(PreparedStatement preparedStatement) {
        try (ResultSet key = preparedStatement.getGeneratedKeys()) {
            key.next();

            Coach savedCoach = new Coach();
            savedCoach.setId(key.getLong("id"));
            savedCoach.setFirstName(key.getString("firstname"));
            savedCoach.setLastName(key.getString("lastname"));
            savedCoach.setPhoneNumber(key.getString("phone_number"));

            return savedCoach;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
