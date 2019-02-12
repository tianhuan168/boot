package dao.cvc;

import entity.cvc.CvCUser;

import java.util.List;

/**
 * user 数据服务
 *
 * @author tianhuan 2018-12-23
 */
public interface CvCUserDAO {

    void save(CvCUser cvCUser);

    void updateById(CvCUser cvCUser);

    CvCUser findById(int id);

    void deleteById(int id);

    List<CvCUser> findAll();
}
