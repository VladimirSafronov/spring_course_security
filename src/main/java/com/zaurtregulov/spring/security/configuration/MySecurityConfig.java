package com.zaurtregulov.spring.security.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

//конфигурация для Spring Security
//в данном классе прописываем usernames, пароли и роли для работников компании
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  DataSource dataSource;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    UserBuilder userBuilder = User.withDefaultPasswordEncoder();

    //информация о юзерах и ролях находится в базе данных, поэтому текст ниже комментирую
    auth.jdbcAuthentication().dataSource(dataSource);

//    auth.inMemoryAuthentication()
//        .withUser(userBuilder.username("zaur").password("zaur").roles("EMPLOYEE"))
//        .withUser(userBuilder.username("elena").password("elena").roles("HR"))
//        .withUser(userBuilder.username("ivan").password("ivan").roles("MANAGER", "HR"));
  }

  //присвоение ролям доступа для входа по адресу
  @Override
  protected void configure(HttpSecurity http) throws Exception {

    //указываем какие роли могут просматривать определенные url адреса
    http.authorizeRequests().antMatchers("/").hasAnyRole("EMPLOYEE", "HR", "MANAGER")
        .antMatchers("/hr_info").hasRole("HR")
        .antMatchers("/manager_info").hasRole("MANAGER")
        .and().formLogin().permitAll(); //форма логина и пароля будет запрашиваться у всех
  }
}
