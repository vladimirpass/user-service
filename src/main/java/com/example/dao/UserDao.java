package com.example.dao;

import com.example.entity.User;
import com.example.utils.HibernateUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor
public class UserDao {

    public UserDao(SessionFactory testSessionFactory) {
    }

    public User saveUser(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();

        }catch(Exception e){
            if(transaction != null) transaction.rollback();
            throw new RuntimeException(e);

        }finally {
            session.close();
        }
        return user;
    }

    public void deleteUser(Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            if(session.get(User.class,id) != null) {
                User userForDelete = session.get(User.class,id);
                session.remove(userForDelete);
                transaction.commit();
            } else{
                System.out.println("Пользователь с указанным индексом отсутствует.");
            }
        }catch (Exception e){
            if(transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }finally {
            session.close();
        }
    }

    public User getById(Integer id){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if(session.get(User.class,id) != null) {
            User user = session.get(User.class,id);
            System.out.println(user);
            return user;
        } else{
            System.out.println("Пользователь с введенным индексом отсутствует");
        }

        session.close();
        return null;
    }

    public List<User> findAll(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> allUsers = session
                .createQuery("SELECT s FROM User s", User.class)
                .list();
        session.close();
        return allUsers;
    }

    public User updateUser(Integer id, User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try{
            transaction = session.beginTransaction();
            User userUpdate = session.get(User.class,id);
            if(session.get(User.class,id) != null) {
                //userUpdate.setId(id);
                userUpdate.setName(user.getName());
                userUpdate.setAge(user.getAge());
                userUpdate.setEmail(user.getEmail());
                userUpdate.setCreateAt(LocalDateTime.now());
                transaction.commit();
            } else{
                System.out.println("Отсутствует пользователь с введенным индексом");
            }
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            throw new RuntimeException(e);
        }finally {
            session.close();
        }
        return user;
    }








}
