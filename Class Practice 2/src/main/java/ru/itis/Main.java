package ru.itis;

import ru.itis.dao.HumanDAO;
import ru.itis.dao.HumanDAOJDBCImpl;
import ru.itis.models.Human;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Bulat Muzipov on 10.09.2017.
 */

public class Main {
    public static void main(String[] args) {
        Connection connection;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres",
                    "postgres",
                    "123ewqpass");

            HumanDAO humansDao = new HumanDAOJDBCImpl(connection);
            Human human = Human.builder()
                    .id(3)
                    .name("Гена")
                    .color("Зеленый")
                    .age(99)
                    .build();

            Human rob = humansDao.find(1L);
            System.out.println(rob.getName());

            ArrayList<Human> list = new HumanDAOJDBCImpl(connection).findAll();
            for (Human x : list) {
                System.out.println(x.getName());
            }

            humansDao.delete(8L);

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}