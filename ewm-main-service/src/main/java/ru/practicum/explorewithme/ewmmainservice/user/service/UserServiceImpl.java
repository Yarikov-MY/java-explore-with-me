package ru.practicum.explorewithme.ewmmainservice.user.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.ewmmainservice.user.exception.UserAlreadyExistsException;
import ru.practicum.explorewithme.ewmmainservice.user.model.User;
import ru.practicum.explorewithme.ewmmainservice.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<User> getAllUsersByIds(List<Integer> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllByIdIn(ids, pageable).getContent();
        } else {
            return userRepository.findAll(pageable).getContent();
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с именем " + user.getName() + " уже существует!");
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
