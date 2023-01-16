package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Qualifier
@Component
@Slf4j
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User getUser(int id) {
        final String sqlQuery = "select * from USERS where user_id = ?";
        final List<User> users = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    private User makeUser(ResultSet rs, int id) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }

    @Override
    public List<User> getUsers() {
        final String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User createUser(User user) {
        final String sqlQuery = "insert into USERS(EMAIL, LOGIN, NAME, BIRTHDAY) values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());

            LocalDate birthday = user.getBirthday();
            if (birthday == null) {
                stmt.setNull(4, Types.DATE);
            } else {
                stmt.setDate(4, Date.valueOf(birthday));
            }
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update USERS set EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? where USER_ID = ?";
        int changes = jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());

        if (changes == 1) {
            return user;
        }
        return null;
    }

    @Override
    public void removeUser(User user) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void addFriend(int userId, int otherId) {
        final String sqlQuery = "insert into FRIENDSHIP(USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, otherId);
    }

    @Override
    public void removeFriend(int userId, int otherId) {
        final String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, otherId);
    }

    @Override
    public List<User> getFriends(int userId) {
        final String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from FRIENDSHIP AS f " +
                "join USERS AS u ON f.FRIEND_ID = u.USER_ID " +
                "where f.USER_ID = ?";
        List<User> friendsUser = jdbcTemplate.query(sqlQuery, this::makeUser, userId);
        return friendsUser;
    }

    @Override
    public List<User> getFriendsCommon (Integer userId, Integer otherId) {
        final String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from FRIENDSHIP AS f " +
                "join USERS AS u ON f.FRIEND_ID = u.USER_ID " +
                "where f.USER_ID = ? AND " +
                "f.FRIEND_ID IN (" +
                "select FRIEND_ID " +
                "from FRIENDSHIP " +
                "where USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId, otherId);
    }
}
