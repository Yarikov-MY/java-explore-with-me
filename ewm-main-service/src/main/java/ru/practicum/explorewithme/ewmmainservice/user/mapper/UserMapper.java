package ru.practicum.explorewithme.ewmmainservice.user.mapper;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmmainservice.user.dto.UserDto;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDTO(@NonNull User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(@NonNull UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}