package dao;


import entity.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();
}
