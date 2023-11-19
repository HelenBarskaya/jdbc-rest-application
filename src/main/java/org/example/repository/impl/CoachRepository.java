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
    private final GroupRepository groupRepository;

    public CoachRepository(ConnectionManager connectionManager) {
        groupRepository = new GroupRepository(connectionManager);
        connection = connectionManager.getConnection();
    }

    @Override
    public Coach findById(Long id) {
        Coach coach = new Coach();
        List<Group> groups = new ArrayList<>();

        // TODO: 19.11.2023 make static private
        String selectById = "select c.id, c.firstname, c.lastname, c.phone_number, g.id from coaches c " +
                "left join groups g on c.id=g.id_coach where c.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectById)) {
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

        // TODO: 19.11.2023 make static private
        String deleteById = "delete from coaches where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteById)) {
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return true;
    }

    @Override
    public List<Coach> findAll() {
        // TODO: 19.11.2023 make static private
        String getAllId = "select c.id from coaches c left join groups g on c.id=g.id_coach group by c.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAllId)) {
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
        // TODO: 19.11.2023 make static private
        String saveCoach = "insert into coaches(firstname, lastname,phone_number) values (?, ?, ?)";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(saveCoach, Statement.RETURN_GENERATED_KEYS)) {
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
        // TODO: 19.11.2023 make static private
        String updateClient = "Update coaches set firstname=?, lastname=?, phone_number=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateClient)) {
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

    public Coach changePhoneNumber(Coach coach) {
        // TODO: 19.11.2023 make static private
        String updatePhoneNumber = "update coaches set phone_number=? where id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updatePhoneNumber,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, coach.getPhoneNumber());
            preparedStatement.setLong(2, coach.getId());
            preparedStatement.execute();

            return returnSavedCoach(preparedStatement);
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
