package ru.practicum.explorewithme.ewmmainservice.user.mapper;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.ewmmainservice.user.dto.UserDto;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;

@UtilityClass
public class UserMapper {
    public static UserDto toUserDTO(@NonNull User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(@NonNull UserDto userDto) {
        return new User(userDto.getId(), userDto.getName(), userDto.getEmail());
    }
}