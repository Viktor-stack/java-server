package com.rujavacours.auth.service;

import com.rujavacours.auth.entity.User;
import com.rujavacours.auth.repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService { // Impl в названии класса означает "Implementation" - реализация

    private final UserRepos userRepository; // доступ к БД

    @Autowired
    public UserDetailsServiceImpl(UserRepos userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    // метод ищет пользователя по username или email (любое совпадение)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // этот метод используется при аутентификации пользователя

        // используем обертку Optional - контейнер, который хранит значение или null - позволяет избежать ошибки NullPointerException
        Optional<User> userOptional = userRepository.findByUsername(username); // сначала пытаемся найти по имени

        if (userOptional.isEmpty()) { // если не нашли по имени
            userOptional = userRepository.findByEmail(username); // пытаемся найти по email
        }

        if (userOptional.isEmpty()) { // если не нашли ни по имени, ни по email
            throw new UsernameNotFoundException("User Not Found with username or email: " + username); // выбрасываем исключение, которое можно отправить клиенту
        }

        return new UserDetailsImpl(userOptional.get()); // если пользователь в БД найден - создаем объект UserDetailsImpl (с объектом User внутри), который потом будет добавлен в Spring контейнер и в объект Principal
    }
}
