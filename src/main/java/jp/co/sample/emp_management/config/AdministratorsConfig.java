package jp.co.sample.emp_management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
class DAdministratorsConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				//ログイン前にアクセス可とするファイル群
				.mvcMatchers("/").permitAll()
				.mvcMatchers("/css/style.css").permitAll()
				.mvcMatchers("/css/bootstrap.css").permitAll()
				.mvcMatchers("/img/e1.png").permitAll()
				.mvcMatchers("/img/e2.png").permitAll()
				.mvcMatchers("/img/header_logo_small.png").permitAll()
				.mvcMatchers("/img/header_logo.png").permitAll()
				// 上記以外のファイルは、ログイン以前のアクセス不可とする
				.anyRequest().authenticated().and()
				// LOGIN
				.formLogin()
				.loginPage("/employee/showList")
				.defaultSuccessUrl("/employee/showList")
				.failureUrl("/404")
		// end
		;
	}
}