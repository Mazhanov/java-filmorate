package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Slf4j
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int otherId) { //
        final String sqlQuery = "insert into FRIENDSHIP(USER_ID, FRIEND_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, otherId);
    }

    @Override
    public void removeFriend(int userId, int otherId) { //
        final String sqlQuery = "delete from FRIENDSHIP where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, userId, otherId);
    }

    @Override
    public List<User> getFriends(int userId) {
        final String sqlQuery = "select u.USER_ID, u.EMAIL, u.LOGIN, u.NAME, u.BIRTHDAY " +
                "from FRIENDSHIP AS f " +
                "join USERS AS u ON f.FRIEND_ID = u.USER_ID " +
                "where f.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::makeUser, userId);
    }

    @Override
    public List<User> getFriendsCommon (Integer userId, Integer otherId) { //
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

    private User makeUser(ResultSet rs, int id) throws SQLException {
        return new User(rs.getInt("USER_ID"),
                rs.getString("EMAIL"),
                rs.getString("LOGIN"),
                rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate()
        );
    }
}
