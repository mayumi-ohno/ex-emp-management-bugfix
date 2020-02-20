package jp.co.sample.emp_management.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.repository.AdministratorRepository;

/**
 * 管理者情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class AdministratorService {
	
	@Autowired
	private AdministratorRepository administratorRepository;
	
	/**
	 * 管理者情報を登録します.
	 * 
	 * @param administrator　管理者情報
	 */
	public void insert(Administrator administrator) {
		administratorRepository.insert(administrator);
	}
	
	/**
	 * ログインをします.
	 * @param mailAddress メールアドレス
	 * @param password パスワード
	 * @return 管理者情報　存在しない場合はnullが返ります
	 */
	public Administrator login(String mailAddress, String passward) {
		Administrator administrator = administratorRepository.findByMailAddressAndPassward(mailAddress, passward);
		return administrator;
	}
	
	/**
	 * メールアドレスで従業員情報を検索・取得する.
	 * @param form 従業員情報
	 * @return　従業員情報
	 */
	public Administrator findByMailAddress(InsertAdministratorForm form) {
		return administratorRepository.findByMailAddress(form.getMailAddress());
	}
	
	/**
	 * メールアドレスで従業員情報を検索・取得する.
	 * @param form 従業員情報
	 * @return　従業員情報
	 */
	public Administrator findByMailAddress(LoginForm form) {
		return administratorRepository.findByMailAddress(form.getMailAddress());
	}
}
