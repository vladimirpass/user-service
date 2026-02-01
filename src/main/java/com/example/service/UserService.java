package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


@Slf4j
public class UserService {

    private final UserDao userDao = new UserDao();


    public User findUser(Integer id) {
        if(id > 0) {
            log.info("Get User id: " + id);
            return userDao.getById(id);
        }else{
            System.out.println("Введено не верное значение!");
        }
        return null;
    }

    public void saveUser(User user) {
        if(user.getAge() > 0) {
            log.info("Create user");
            userDao.saveUser(user);
        }else{
            System.out.println("Введено не верное значение! Пользователь не может быть создан.");
        }
    }

    public void deleteUser(Integer id) {
        if(id > 0) {
            log.info("Delete user id: " + id);
            userDao.deleteUser(id);
        }else{
            System.out.println("Введено не верное значение! Пользователь не может быть удален.");
        }
    }

    public void updateUser(Integer id, User user) {
        if(id > 0) {
            log.info("Update user id: " + id);
            userDao.updateUser(id, user);
        }else{
            System.out.println("Введено не верное значение!");
        }
    }

    public List<User> findAllUsers() {
        log.info("Get all users");
        for(User s : userDao.findAll()){
            System.out.println(s);
        }
        return userDao.findAll();
    }
}
