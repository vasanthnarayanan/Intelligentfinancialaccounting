package com.curious365.ifa;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import com.curious365.ifa.util.SaveSecurityContextDetailsHandler;

@Configuration
@EnableWebSecurity
public class IFASWebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	 DataSource dataSource;
	
	@Bean
	public SaveSecurityContextDetailsHandler saveSecurityContextDetailsHandler(){
		return new SaveSecurityContextDetailsHandler();
	}

	 @Autowired
	 public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	   auth.jdbcAuthentication().dataSource(dataSource)
	  .usersByUsernameQuery(
	   "select username,password, enabled from user_authentication where username=?")
	  .authoritiesByUsernameQuery(
	   "select username, role from user_authorization where username=?");
	 }
	 
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {

	   http.headers().addHeaderWriter(
		        new XFrameOptionsHeaderWriter(
		                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
	   .and().authorizeRequests()
	   .antMatchers("/resources/**").permitAll()
	   .antMatchers("/removeCustomer").access("hasRole('ROLE_ADMIN')")  
	   .anyRequest().authenticated()
	   .and()
	    .formLogin().loginPage("/login").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/home").failureUrl("/login?error").successHandler(saveSecurityContextDetailsHandler())
	    .permitAll()
	   .and()
	    .logout().logoutSuccessUrl("/login?logout").permitAll() 
	   .and()
	   .exceptionHandling().accessDeniedPage("/403")
	   .and()
	   .csrf().disable();

	 }
}
