package ru.yandex.practicum.filmorate.exception;

public class UsersAlreadyFriendshipException extends RuntimeException{
    public UsersAlreadyFriendshipException(String message) {
        super(message);
    }
}
