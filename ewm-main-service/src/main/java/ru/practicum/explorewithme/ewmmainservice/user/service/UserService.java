package ru.practicum.explorewithme.ewmmainservice.user.service;


import ru.practicum.explorewithme.ewmmainservice.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsersByIds(List<Integer> ids, Integer from, Integer size);

    User createUser(User user);

    void deleteUser(Integer userId);
}
