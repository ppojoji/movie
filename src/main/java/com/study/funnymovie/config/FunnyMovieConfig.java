package com.study.funnymovie.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import jakarta.annotation.PostConstruct;

@Configuration
public class FunnyMovieConfig {
	
	@Value("${fmovie.mode}")
	String appMode;
	
	@Value("${server.port}")
	String port;
	
	@PostConstruct // 스프링 부트가 다 준비됐을때, 이 메소드를 한 번 호출해 달라
	public void printAppMode() {
		System.out.println(" * [MODE] " + appMode);
		System.out.println(" * [PORT] " + port);
	}
	
	@Bean
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
		SqlSessionFactoryBean fBean = new SqlSessionFactoryBean();
		
		fBean.setDataSource(dataSource);
		fBean.setTypeAliasesPackage("com.study.funnymovie.domain.**");
		
		Resource[] mappers = new PathMatchingResourcePatternResolver().getResources("classpath:db/mapper/**/**.xml");
		System.out.println("[Mybatis Mappers]");
		System.out.println(" * [Mapper] " + mappers[0].getFilename());
		fBean.setMapperLocations(mappers);
		/*
		fBean.setTypeHandlers(
				new VendorTypeHanlder(),
				new UserRoleTypeHanlder(),
				new BooleanToYNTypeHanlder(),
				new PolicyTypeHandler());
		*/
		return fBean;
	}
}
