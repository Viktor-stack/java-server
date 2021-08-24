package com.rujavacours.auth.service;

import com.rujavacours.auth.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Setter
@Getter
public class UserDetailsImpl implements UserDetails { // Impl в названии класса означает "Implementation" - реализация

    private User user; // чтобы не дублировать в этом классе все поля User - просто помещаем сюда сам объект User, к которому сможем обращаться при необходимости
    private Collection<? extends GrantedAuthority> authorities; // все права пользователя - эту переменную использует Spring контейнер


    public UserDetailsImpl(User user) {
        this.user = user;

        // переменную authorities использует Spring контейнер, поэтому в нее обязательно нужно записать все права пользователя
        authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }


    // эти гетеры прописываем вручную (не через Lombok), т.к. данные получаем из объекта User
    public long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public boolean isActivated() {
        return user.activity.isActivated();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    // если эти методы требуется по задаче - их можно реализовать (на основе данных пользователя из БД)

    @Override
    public boolean isAccountNonExpired() { // действует или нет
        return true; // всегда будет возвращать true, т.к. проверка пока не требуется по задаче
    }

    @Override
    public boolean isAccountNonLocked() { // заблокирован или нет
        return true; // всегда будет возвращать true, т.к. проверка пока не требуется по задаче
    }

    @Override
    public boolean isCredentialsNonExpired() { // пароль действителен или нет
        return true; // всегда будет возвращать true, т.к. проверка пока не требуется по задаче
    }


    /*

            Метод isEnabled вызывается автоматически Spring контейнером, где это нужно ему по логике работы.
            Но проблема в том, что метод может вызываться до проверки "логин-пароль"
            Поэтому исключение DisabledException (пользователь деактивирован) выбросится до того, как проверим верно ли введены "логин-пароль"


            Мы реализуем по-другому: чтобы пользователь проверялся на активность только после успешного ввода логина-пароля (это логичней).

            Поэтому в нужных местах кода сами будем проверять, активирован аккаунт или нет с помощью поля active из БД.

            Если пользователь неактивен - выбрасываем исключение (готовый класс DisabledException).

            Поэтому метод всегда возвращает true.

             */
    @Override
    public boolean isEnabled() { // нигде в коде не будем вызывать
        return true; // всегда возвращаем true - см. комментарий для метода
    }


}