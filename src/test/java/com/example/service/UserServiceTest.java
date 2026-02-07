package com.example.service;

import com.example.dao.UserDao;
import com.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest{

    @Mock
    private UserDao userDao;

    @Spy
    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User("Вася",25,"vasiy@mail.ru");
        userService = new UserService(userDao);

    }

    @Test
    void findUser_WithPositiveId_ShouldReturnUser() {
        Integer id = 1;

        when(userDao.getById(id)).thenReturn(validUser);
        User result = userService.findUser(id);

        assertNotNull(result);
        assertEquals(validUser, result);
        verify(userDao).getById(id);
    }

    @Test
    void findAllUsersTest(){
        List<User> users = new ArrayList<>();
        users.add(validUser);

        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.findAllUsers();

        assertNotNull(result);
        assertEquals(1,result.size());
    }

    @Test
    void saveUsersTest(){
        userService.saveUser(validUser);

        verify(userDao).saveUser(validUser);
    }

    @Test
    void deleteUserTest(){
        userService.saveUser(validUser);

        userService.deleteUser(1);
        verify(userDao).deleteUser(1);
    }

    @Test
    void updateUserTest(){
        User userForUpdate = new User("Anatoly", 29,"NewEmail");
        userService.saveUser(validUser);

        userService.updateUser(1,userForUpdate);

        verify(userDao).updateUser(1,userForUpdate);
    }


}
