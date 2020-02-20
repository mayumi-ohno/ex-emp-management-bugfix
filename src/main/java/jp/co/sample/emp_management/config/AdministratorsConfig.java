package jp.co.sample.emp_management.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class AdministratorsConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity web) throws Exception{
		
		web.formLogin().loginPage("/").defaultSuccessUrl("list").failureUrl("505").permitAll();
//		web.authorizeRequests().antMatchers("/css/**", "/images/**", "/js/**").permitAll().anyRequest().authenticated();
	}
	

}
