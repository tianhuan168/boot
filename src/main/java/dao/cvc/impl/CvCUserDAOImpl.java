package dao.cvc.impl;


import entity.cvc.CvCUserWhere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;
import mapper.cvc.CvCUserMapper;
import entity.cvc.CvCUser;
import dao.cvc.CvCUserDAO;

import java.util.List;


/**
 * user 数据服务
 *
 * @author tianhuan 2018-12-23
 */
@Repository
public class CvCUserDAOImpl implements CvCUserDAO {

    @Autowired
    private CvCUserMapper cvCUserMapper;

    @Override
    public void save(CvCUser cvCUser) {
        Preconditions.checkNotNull(cvCUser);
        cvCUserMapper.insertSelective(cvCUser);
    }

    @Override
    public void updateById(CvCUser cvCUser) {
        Preconditions.checkNotNull(cvCUser);
        cvCUserMapper.updateByIdSelective(cvCUser);
    }

    @Override
    public CvCUser findById(int id) {
        return cvCUserMapper.selectById(id);
    }

    @Override
    public void deleteById(int id) {
        cvCUserMapper.deleteById(id);
    }

    @Override
    public List<CvCUser> findAll() {
        CvCUserWhere where = new CvCUserWhere();
        CvCUserWhere.Criteria criteria = where.createCriteria();
        criteria.andIdGreaterThan(0);
        return cvCUserMapper.selectByWhere(where);
    }
}
