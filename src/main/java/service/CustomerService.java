package service;/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/5/5 18:38
 */


import entity.User;

import java.util.List;

/**
 *
 * @author
 * @create 2018-05-05 18:38
 **/
public interface CustomerService {

    List<User> findAll();
}
