package jp.co.sample.emp_management.domain;

import org.springframework.security.core.authority.AuthorityUtils;

/**
 * ユーザー情報を表すドメイン.
 * 
 * @author mayumiono
 *
 */
@SuppressWarnings("serial")
public class LoginUser extends org.springframework.security.core.userdetails.User {

	/** ログイン情報 */
	private final Administrator administrator;

	/**
	 * コンストラクタ
	 * 
	 * @param user
	 */
	public LoginUser(Administrator user) {
		// スーパークラスのユーザーID、パスワードに値をセットする
		// 実際の認証はスーパークラスのユーザーID、パスワードで行われる
		super(user.getMailAddress(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
		this.administrator = user;
	}

	/**
	 *
	 * @return
	 */
	public Administrator getUser() {
		return this.administrator;
	}
}
