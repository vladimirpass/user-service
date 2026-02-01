package com.example.menu;

import com.example.entity.User;
import com.example.service.UserService;

import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService service = new UserService();
    private static final String menu ="== Меню == \n1. Создать пользователя \n2. Посмотреть всех пользователей " +
            "\n3. Обновить данные пользователя \n4. Удалить пользователя \n5. Найти пользователя по ID \n0. Выход \n>";

    public void mainMenu() {
        boolean exit = false;
        System.out.println(menu);

        while (!exit) {
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> create();
                case "2" -> view();
                case "3" -> update();
                case "4" -> delete();
                case "5" -> findById();
                case "0" -> exit = true;
                default -> System.out.println(menu);
            }
        }
        scanner.close();
    }

    private static void create() {
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();

        System.out.println("Введите email пользователя:");
        String email = scanner.nextLine();

        System.out.println("Введите возраст:");
        if (scanner.hasNextInt()) {
            Integer age = scanner.nextInt();
            User user = new User(name, age, email);
            service.saveUser(user);
        }else {
            System.out.println("Введено неверное значение! Пользователь не может быть создан.");
        }
    }

    private static void view() {
        System.out.println("Список пользователей...");
        service.findAllUsers();
        System.out.println(menu);
    }

    private static void update() {
        System.out.println("Ведите новое имя пользователя:");
        String name = scanner.nextLine();

        System.out.println("Введите новый email пользователя:");
        String email = scanner.nextLine();

        System.out.println("Введите новый возраст пользователя:");
        if (scanner.hasNextInt()) {
            Integer age = scanner.nextInt();
            User userUpdate = new User(name, age, email);

            System.out.print("Введите ID пользователя для обновления его данных: ");
            if (scanner.hasNextInt()) {
                Integer id = scanner.nextInt();
                service.updateUser(id, userUpdate);
            } else {
                System.out.println("Введено неверное значение! Пользователь не может быть создан.");
            }

        }else {
            System.out.println("Введено неверное значение! Пользователь не может быть создан.");
        }
    }

    private static void delete() {
        System.out.print("Введите ID пользователя для его удаления: ");
        if (scanner.hasNextInt()) {
            Integer id = scanner.nextInt();
            service.deleteUser(id);
        }else {
            System.out.println("Введено неверное значение! Пользователь не может быть удален.");
        }
    }

    private static void findById() {
        System.out.print("Введите ID искомого пользователя: ");
        Integer id = scanner.nextInt();
        service.findUser(id);
    }

}






