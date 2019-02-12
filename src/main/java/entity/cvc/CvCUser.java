package entity.cvc;

import java.io.Serializable;
import java.util.Date;

/**
 * tianhuan.cvc_user  
 *
 * @author tianhuan
 * @date 2018-12-23
 * 该类为自动生成，请勿修改
 */
public class CvCUser implements Serializable {
    private Integer id;

    private String name;

    /** 0未删除 1已删除 表字段：cvc_user.deleted */
    private Boolean deleted;

    private Boolean rLv;

    private Date ctime;

    private Date utime;

    private Integer creater;

    private static final long serialVersionUID = 1L;

    /** 表字段： cvc_user.id */
    public Integer getId() {
        return id;
    }

    /** 表字段： cvc_user.id */
    public void setId(Integer id) {
        this.id = id;
    }

    /** 表字段： cvc_user.name */
    public String getName() {
        return name;
    }

    /** 表字段： cvc_user.name */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /** 表字段： cvc_user.deleted */
    public Boolean getDeleted() {
        return deleted;
    }

    /** 表字段： cvc_user.deleted */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /** 表字段： cvc_user.r_lv */
    public Boolean getrLv() {
        return rLv;
    }

    /** 表字段： cvc_user.r_lv */
    public void setrLv(Boolean rLv) {
        this.rLv = rLv;
    }

    /** 表字段： cvc_user.ctime */
    public Date getCtime() {
        return ctime;
    }

    /** 表字段： cvc_user.ctime */
    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    /** 表字段： cvc_user.utime */
    public Date getUtime() {
        return utime;
    }

    /** 表字段： cvc_user.utime */
    public void setUtime(Date utime) {
        this.utime = utime;
    }

    /** 表字段： cvc_user.creater */
    public Integer getCreater() {
        return creater;
    }

    /** 表字段： cvc_user.creater */
    public void setCreater(Integer creater) {
        this.creater = creater;
    }
}