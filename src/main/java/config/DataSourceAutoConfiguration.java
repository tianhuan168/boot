package config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/19 18:23
 */
@Configuration
@ConditionalOnClass(DataSource.class)
@ConfigurationProperties
@EnableConfigurationProperties(value = {DataSourceProperties.class})
public class DataSourceAutoConfiguration {

    private DataSourceProperties properties;
    public DataSourceAutoConfiguration(DataSourceProperties properties) {
        this.properties = properties;
    }



    @Primary
    @Bean(initMethod = "init",destroyMethod = "close")
    public DataSource datasource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(properties.getUrl());
        ds.setDriverClassName(properties.getDriverClassName());
        ds.setUsername(properties.getUsername());
        ds.setPassword(properties.getPassword());
        ds.setInitialSize(properties.getInitialSize());// 配置初始化大小、最小、最大
        ds.setMinIdle(properties.getMinIdle());
        ds.setMaxActive(properties.getMaxActive());
        ds.setMaxWait(properties.getMaxWait()); // 配置获取连接等待超时的时间
        ds.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        ds.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis()); // 连接保持空闲而不被驱逐的最长时间，单位是毫秒
        ds.setValidationQuery("SELECT 0");
        ds.setTestWhileIdle(properties.isTestWhileIdle());
        ds.setTestOnBorrow(properties.isTestOnBorrow());
        ds.setTestOnReturn(properties.isTestOnReturn());
        ds.setPoolPreparedStatements(false); // 在mysql下建议关闭。
        ds.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
        WallFilter filter = new WallFilter();
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(properties.isMultiStatementAllow());
        filter.setConfig(config);
        List<Filter> filters=new ArrayList<Filter>();
        filters.add(filter);
        ds.setProxyFilters(filters);
        return ds;
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource){
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        try {
            return factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
