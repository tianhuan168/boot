package dao.impl;/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/4/18 17:57
 */


import dao.UserDAO;
import entity.User;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author
 * @create 2018-04-18 17:57
 **/
@Service
public class UserDaoImpl implements UserDAO {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findAll() {
        return userMapper.findAll();
    }
}
