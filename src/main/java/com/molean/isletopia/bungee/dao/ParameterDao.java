package com.molean.isletopia.bungee.dao;

import org.bouncycastle.jcajce.provider.asymmetric.rsa.ISOSignatureSpi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParameterDao {

    private static boolean checked = false;

    public static void init() {
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    create table if not exists parameter
                    (
                        id     integer
                            primary key autoincrement,
                        type   varchar(100) not null,
                        target varchar(100) not null,
                        key    varchar(100) not null,
                        value  text         not null,
                        constraint parameter_pk
                            unique (type, target, key)
                    );
                                      
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static String get(String type, String obj, String key) {
        if (!checked) {
            init();
            checked = true;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       select value
                       from parameter
                       where type = ?
                         and target = ?
                         and key = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, key);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void set(String type, String obj, String key, String value) {
        if (!checked) {
            init();
            checked = true;
        }
        if (get(type, obj, key) == null) {
            insert(type, obj, key, value);
            return;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                        update parameter
                        set value = ?
                        where type = ?
                          and target = ?
                          and key = ?;
                        """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, obj);
            preparedStatement.setString(4, key);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public static void insert(String type, String obj, String key, String value) {
        if (!checked) {
            init();
            checked = true;
        }
        if (get(type, obj, key) != null) {
            set(type, obj, key, value);
            return;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                    insert into parameter(type, target, key, value)
                    values (?, ?, ?, ?)
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, key);
            preparedStatement.setString(4, value);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void delete(String type, String obj, String key) {
        if (!checked) {
            init();
            checked = true;
        }
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                        delete
                        from parameter
                        where type = ?
                          and target = ?
                          and key = ?;
                     """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            preparedStatement.setString(2, obj);
            preparedStatement.setString(3, key);
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<String> targets(String type) {
        if (!checked) {
            init();
            checked = true;
        }
        ArrayList<String> strings = new ArrayList<>();
        try (Connection connection = DataSourceUtils.getConnection()) {
            String sql = """
                       select target
                       from parameter
                       where type = ?
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String string = resultSet.getString(1);
                strings.add(string);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return strings;
    }
}
