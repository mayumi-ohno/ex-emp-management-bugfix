package jp.co.sample.emp_management.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.sample.emp_management.domain.Administrator;
import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.domain.LoginUser;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/showList")
	public String showList(Model model, Integer index, @AuthenticationPrincipal LoginUser loginUser) {

		//ログインユーザー名をセッションスコープに格納
		Administrator administrator = loginUser.getUser();
		session.setAttribute("administratorName", administrator.getName());

		// 従業員情報を10件/1ページ表示するときの総ページ数を算出
		Integer dataCount = employeeService.dataCount();
		Integer dataPerPage = 10;
		Integer totalPage;
		if (dataCount % dataPerPage == 0) {
			totalPage = dataCount / dataPerPage;
		} else {
			totalPage = dataCount / dataPerPage + 1;
		}

		// ページ番号のリストを作成
		List<Integer> pageNumbers = new ArrayList<>();
		for (int i = 1; i <= totalPage; i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("pageNumbers", pageNumbers);

		// 初回表示時はDBの1番目～10件分の従業員情報（10件）を表示する
		if (index == null) {
			List<Employee> employeeList = employeeService.findLimited(0);
			model.addAttribute("employeeList", employeeList);
			return "employee/list";
		}

		// ページ送り時は、引数のインデックス番号×10番目～10件分の従業員情報を切り出す
		// 【例】インデックス番号が2の場合：DB上の20-29番目の従業員情報を表示
		Integer topOfData = index * 10;
		List<Employee> employeeList = employeeService.findLimited(topOfData);
		model.addAttribute("employeeList", employeeList);

		return "employee/list";
	}

	/**
	 * 名前に引数の文字列を含む従業員の情報を出力します.
	 * 
	 * @param aPartOfName 名前の一部（フォームに入力した文字列）
	 * @param model       リクエストスコープ
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/search-employee")
	public String searchEmployee(String aPartOfName, Model model) {

		List<Employee> employeeList = employeeService.findByAPartOfName(aPartOfName);

		// 該当情報なしの場合はメッセージ出力・従業員情報全件出力
		if (employeeList.size() == 0) {
			model.addAttribute("nothingHit", "１件もありませんでした");
			return "forward:/employee/showList";
		}

		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/**
	 * JQueryに従業員名のリストを返す.
	 * 
	 * @return 従業員名一覧（JSON）
	 */
	@ResponseBody
	@RequestMapping("/auto-complete-api")
	public List<String> autoCompleteApi() {
		List<Employee> allEmployees = employeeService.showList();
		List<String> employeeNames = new ArrayList<>();
		for (Employee employee : allEmployees) {
			employeeNames.add(employee.getName());
		}
		return employeeNames;
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}
}
