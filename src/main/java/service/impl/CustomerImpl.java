package service.impl;/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/5/5 18:39
 */


import entity.User;
import org.springframework.stereotype.Service;
import service.CustomerService;

import java.util.List;

/**
 * @author
 * @create 2018-05-05 18:39
 **/
@Service
public class CustomerImpl implements CustomerService {


    @Override
    public List<User> findAll() {
         return null;
    }
}
