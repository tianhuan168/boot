package service.cvc;

import entity.cvc.CvCUser;

import java.util.List;

/**
 * user 服务
 *
 * @author tianhuan 2018-12-23
 */
public interface CvCUserService {

        /**
         * 保存
         *
         */
    void save(CvCUser cvCUser);

        /**
         * 修改
         */
    void updateById(CvCUser cvCUser);

        /**
         * 指定id查询
         */
    CvCUser findById(int id);

        /**
         * 指定id删除
         */
    void deleteById(int id);


    List<CvCUser> findAll();
}
