package mapper.cvc;

import entity.cvc.CvCUser;
import entity.cvc.CvCUserWhere;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CvCUserMapper {
     /** 表： cvc_user */
    long countByWhere(CvCUserWhere example);

     /** 表： cvc_user */
    int deleteByWhere(CvCUserWhere example);

     /** 表： cvc_user */
    int deleteById(Integer id);

     /** 表： cvc_user */
    int insert(CvCUser record);

     /** 表： cvc_user */
    int insertSelective(CvCUser record);

     /** 表： cvc_user */
    List<CvCUser> selectByWhere(CvCUserWhere example);

     /** 表： cvc_user */
    CvCUser selectById(Integer id);

     /** 表： cvc_user */
    int updateByWhereSelective(@Param("record") CvCUser record, @Param("example") CvCUserWhere example);

     /** 表： cvc_user */
    int updateByWhere(@Param("record") CvCUser record, @Param("example") CvCUserWhere example);

     /** 表： cvc_user */
    int updateByIdSelective(CvCUser record);

     /** 表： cvc_user */
    int updateById(CvCUser record);
}