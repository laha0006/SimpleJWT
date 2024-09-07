package dev.tolana.simplejwt.login.model;

public record RegisterRequestDto(String firstName,
                                 String userName,
                                 String password) {
}
