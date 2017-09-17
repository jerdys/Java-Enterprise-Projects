package ru.itis.dao;

import ru.itis.models.Human;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HumanDAOJDBCImpl implements HumanDAO {
    //language=SQL
    private final static String SQL_INSERT_OWNER ="INSERT INTO student (name, age, color) VALUES (?,?,?)";
    private static final String SQL_FIND_ALL ="SELECT * FROM student" ;
    private static final String SQL_DELETE_STUDENT = "DELETE FROM student WHERE student.id = ?";
    private static final String SQL_UPDATE_STUDENT = "";

    private Connection connection;
    //language=SQL
    private String SQL_FIND_OWNER = "SELECT * FROM student WHERE student.id = ?";

    public HumanDAOJDBCImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Human model) {
        try {
            PreparedStatement statement =
                    connection.prepareStatement(SQL_INSERT_OWNER,
                            new String[] {"id"});
            statement.setString(1, model.getName());
            statement.setInt(2, model.getAge());
            statement.setString(3, model.getColor());
            statement.executeUpdate();

            // получаем указатель на результирующие строки
            // результирующие строки - сгенерированный id
            ResultSet resultSet = statement.getGeneratedKeys();
            // одновременно сдвигаем итератор и проверяем есть там че или нет
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                model.setId(id);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public Human find(Long id) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(SQL_FIND_OWNER);
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return Human.builder()
                        .age(resultSet.getInt("age"))
                        .color(resultSet.getString("color"))
                        .name(resultSet.getString("name"))
                        .id(resultSet.getLong("id"))
                        .build();
            } else throw new IllegalArgumentException("user not found");
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public ArrayList<Human> findAll() {
        PreparedStatement statement;
        ArrayList<Human> list = new ArrayList<>();

        try {
            statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                list.add(Human.builder()
                        .age(resultSet.getInt("age"))
                        .color(resultSet.getString("color"))
                        .name(resultSet.getString("name"))
                        .id(resultSet.getLong("id"))
                        .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void delete(Long id) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(SQL_DELETE_STUDENT);
            statement.setLong(1,id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Human human, Long id) {
        PreparedStatement statement;
        try {
             statement = connection.prepareStatement(SQL_UPDATE_STUDENT,
                    new String[] {"id"});
            statement.setString(1, human.getName());
            statement.setInt(2, human.getAge());
            statement.setString(3, human.getColor());
            statement.setLong(4, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
