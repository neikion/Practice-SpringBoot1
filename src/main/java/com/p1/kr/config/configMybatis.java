package com.p1.kr.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(
		basePackages = {"com.p1.kr.mybatis"},
		annotationClass = org.apache.ibatis.annotations.Mapper.class,
		sqlSessionFactoryRef = "SessionFactory"
		)
public class configMybatis {
	@Bean(name="SessionFactory")
	public SqlSessionFactory SessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception{
		SqlSessionFactoryBean factory=new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:com/p1/kr/mybatis/*Mapper.xml"));
		return factory.getObject();
	}
	
}
 