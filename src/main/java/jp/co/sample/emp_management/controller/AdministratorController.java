package jp.co.sample.emp_management.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.form.InsertAdministratorForm;
import jp.co.sample.emp_management.form.LoginForm;
import jp.co.sample.emp_management.service.AdministratorService;

/**
 * 管理者情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/")
public class AdministratorController {

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private HttpSession session;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public InsertAdministratorForm setUpInsertAdministratorForm() {
		return new InsertAdministratorForm();
	}

	// (SpringSecurityに任せるためコメントアウトしました)
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：管理者を登録する
	/////////////////////////////////////////////////////
	/**
	 * 管理者登録画面を出力します.
	 * 
	 * @return 管理者登録画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "administrator/insert";
	}

	/**
	 * 確認用パスワードとパスワードが一致するかAjax確認する.
	 * 
	 * @param password             パスワード
	 * @param confirmationPassword 確認用パスワード
	 * @return JSONオブジェクト
	 */
	@ResponseBody
	@RequestMapping("/pass-check-api")
	public Map<String, String> passCheck(String password, String confirmationPassword) {
		Map<String, String> map = new HashMap<>();

		// 確認パスワードチェック
		if (confirmationPassword == null) {
		} else if (password.equals(confirmationPassword)) {
			map.put("duplicateMessage", "確認パスワードOK!");
		} else {
			map.put("duplicateMessage", "パスワードが一致しません");
		}

		return map;
	}

	/**
	 * 管理者情報を登録します.
	 * 
	 * @param form 管理者情報用フォーム
	 * @return ログイン画面へリダイレクト
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertAdministratorForm form, BindingResult result, Model model) {

		Administrator administratorForCheckMail = administratorService.findByMailAddress(form);

		// 確認用パスワードとパスワード不一致の場合、エラー
		if (!form.getPassword().equals(form.getConfirmationPassword())) {
			FieldError passError = new FieldError(result.getObjectName(), "password", "確認用パスワードが一致しません");
			result.addError(passError);
		}
		// メールアドレスが登録済の場合、エラー
		if (administratorForCheckMail != null) {
			FieldError mailError = new FieldError(result.getObjectName(), "mailAddress", "メールアドレスが登録済です");
			result.addError(mailError);
		}

		// １つでもエラーがあれば登録画面に戻る
		if (result.hasErrors()) {
			return toInsert();
		}

		Administrator administrator = new Administrator();
		// パスワードを暗号化
		BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
		String encode = bcrypt.encode(form.getPassword());
		// フォームからドメインにプロパティ値をコピー
		// パスワードは暗号化したものを代入
		BeanUtils.copyProperties(form, administrator);
		administrator.setPassword(encode);

		administratorService.insert(administrator);
		return "redirect:/";
	}

	/////////////////////////////////////////////////////
	// ユースケース：ログインをする
	/////////////////////////////////////////////////////
	/**
	 * ログイン画面を出力します.
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping("/")
	public String toLogin() {
		return "administrator/login";
	}

	/**
	 * ログインします.
	 * 
	 * @param form   管理者情報用フォーム
	 * @param result エラー情報格納用オブッジェクト
	 * @return ログイン後の従業員一覧画面
	 */
	/*
	 * @RequestMapping("/login") public String login(LoginForm form, Model model) {
	 * 
	 * // 入力欄空欄の場合、エラーとする if ("".equals(form.getMailAddress()) ||
	 * "".equals(form.getPassword())) { model.addAttribute("errorMessage",
	 * "メールアドレスとパスワードを入力してください。"); return toLogin(); }
	 * 
	 * // メールアドレスで管理者情報検索し、DB上のハッシュ化されたパスワードを取得する Administrator administrator =
	 * administratorService.findByMailAddress(form); String encodeInDb =
	 * administrator.getPassword();
	 * 
	 * // DB上のハッシュ化パスワードとフォームから来た平文パスワードを照合する if
	 * (passwordEncoder.matches(form.getPassword(), encodeInDb)) { //
	 * 一致した場合、名前をセッションに格納のうえ従業員情報一覧画面へ session.setAttribute("administratorName",
	 * administrator.getName()); return "forward:/employee/showList"; } else {
	 * model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です。"); return
	 * toLogin(); }
	 * 
	 * }
	 */

	/////////////////////////////////////////////////////
	// ユースケース：ログアウトをする
	/////////////////////////////////////////////////////
	/**
	 * ログアウトをします. (SpringSecurityに任せるためコメントアウトしました)
	 * 
	 * @return ログイン画面
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/";
	}

}
