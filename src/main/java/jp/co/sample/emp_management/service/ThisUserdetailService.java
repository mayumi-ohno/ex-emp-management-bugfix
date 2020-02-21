package jp.co.sample.emp_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.LoginUser;
import jp.co.sample.emp_management.repository.AdministratorRepository;

@Service
public class ThisUserdetailService implements UserDetailsService {

	@Autowired
	private AdministratorRepository administratorRepository;

	@Override
	public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {

		try {
			Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
			return new LoginUser(administrator);
		} catch (Exception e) {
			throw new UsernameNotFoundException("not found : " + mailAddress);
		}

	}

}
