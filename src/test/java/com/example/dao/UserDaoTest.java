package com.example.dao;

import com.example.entity.User;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Testcontainers
public class UserDaoTest {

    private static SessionFactory testSessionFactory;
    private static UserDao userDao;


    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");

    @BeforeAll
    static void setUpAll() {
        // Настраиваем Hibernate для работы с Testcontainers PostgreSQL
        Configuration configuration = new Configuration();

        // Настройки Hibernate
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.connection.url", postgres.getJdbcUrl());
        hibernateProperties.put("hibernate.connection.username", postgres.getUsername());
        hibernateProperties.put("hibernate.connection.password", postgres.getPassword());
        hibernateProperties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.put("hibernate.show_sql", "true");
        hibernateProperties.put("hibernate.format_sql", "true");

        configuration.addProperties(hibernateProperties);
        configuration.addAnnotatedClass(User.class);

        testSessionFactory = configuration.buildSessionFactory();
        userDao = new UserDao(testSessionFactory);

    }

    @BeforeEach
    void clearUserTable() {
        String url = postgres.getJdbcUrl();
        String username = postgres.getUsername();
        String password = postgres.getPassword();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE users CASCADE");

        } catch (Exception e) {
            throw new RuntimeException("Failed to clear users table", e);
        }

    }

    @AfterAll
    static void tearDownAll() {
        if (testSessionFactory != null) {
            testSessionFactory.close();
        }
    }

    @Test
    @DisplayName("Создание пользователя")
    void saveUser_ShouldSaveUser() {
        // Given
        User user = new User("John Doe",30,"john@example.com");

        // When
        User savedUser = userDao.saveUser(user);

        // Then
        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
        assertNotNull(savedUser.getCreateAt());
    }

    @Test
    @DisplayName("Нахождения пользователя по id")
    void getById_ShouldReturnUser() {
        // Given
        User user = new User("Jane Doe",25,"jane@example.com");
        User savedUser = userDao.saveUser(user);

        // When
        User foundUser = userDao.getById(savedUser.getId());

        // Then
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("Jane Doe", foundUser.getName());
        assertEquals("jane@example.com", foundUser.getEmail());
    }

    @Test
    @DisplayName("Должен возвращать значение null, если пользователь не найден")
    void getById_ShouldReturnNull_WhenUserNotFound() {
        // When
        User foundUser = userDao.getById(999);

        // Then
        assertNull(foundUser);
    }

    @Test
    @DisplayName("Должен найти всех пользователей")
    void findAll_ShouldReturnAllUsers() {
        // Given
        User user1 = new User("User 1",20,"user1@example.com");
        User user2 = new User("User 2",30,"user2@example.com");
        userDao.saveUser(user1);
        userDao.saveUser(user2);

        // When
        List<User> users = userDao.findAll();

        // Then
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getName)
                .containsExactly("User 1", "User 2");
          assertEquals(2,users.size());
    }


    @Test
    @DisplayName("Успешное обновление пользователя")
    void updateUser_ShouldUpdateUser() {
        // Given
        User user = new User("Original Name",25,"original@example.com");
        User updates = new User("Updated Name",26,"updated@example.com");
        User savedUser = userDao.saveUser(user);

        // When
        User updatedUser = userDao.updateUser(savedUser.getId(), updates);

        // Then
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(26, updatedUser.getAge());
        assertNotNull(updatedUser.getCreateAt());
    }


    @Test
    @DisplayName("Удаление пользователя")
    void deleteUser_ShouldDeleteUser() {
        // Given
        User user = new User("To Delete",40,"delete@example.com");
        User savedUser = userDao.saveUser(user);

        // When
        userDao.deleteUser(savedUser.getId());

        // Then
        User deletedUser = userDao.getById(savedUser.getId());
        assertNull(deletedUser);
    }

}
