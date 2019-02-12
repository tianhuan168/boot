package service.cvc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.cvc.CvCUser;
import service.cvc.CvCUserService;
import dao.cvc.CvCUserDAO;

import java.util.List;

/**
 * user 服务
 * 
 * @author tianhuan 2018-12-23
 */
@Service
public class CvCUserServiceImpl implements CvCUserService {
    private static final Logger logger = LoggerFactory.getLogger(CvCUserServiceImpl.class);

    @Autowired
    private CvCUserDAO cvCUserDAO;

    @Override
    public void save(CvCUser cvCUser) {
        cvCUserDAO.save(cvCUser);
    }

    @Override
    public void updateById(CvCUser cvCUser) {
        cvCUserDAO.updateById(cvCUser);
    }

    @Override
    public CvCUser findById(int id) {
        return cvCUserDAO.findById(id);
    }

    @Override
    public void deleteById(int id) {
        cvCUserDAO.deleteById(id);
    }

    @Override
    public List<CvCUser> findAll() {
        return cvCUserDAO.findAll();
    }
}
