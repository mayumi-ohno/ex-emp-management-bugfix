package jp.co.sample.emp_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sample.emp_management.domain.Employee;
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
	public String showList(Model model, Integer index) {
		List<Employee> allEmployees = employeeService.showList();

		//従業員情報を10件/1ページ表示するときの総ページ数を算出
		Integer dataPerPage = 10;
		Integer totalPage;
		if (allEmployees.size() % dataPerPage == 0) {
			totalPage = allEmployees.size() / dataPerPage;
		} else {
			totalPage = allEmployees.size() / dataPerPage + 1;
		}
		
		//ページ番号のリストを作成
		List<Integer> pageNumbers = new ArrayList<>();
		for(int i =1; i <= totalPage; i++) {
			pageNumbers.add(i);
		}
		model.addAttribute("pageNumbers", pageNumbers);
		
		//初回表示時はリスト0-9番目の従業員情報（10件）を表示する
		if(index==null) {
			List<Employee> employeeList = allEmployees.subList(0, 10);
			model.addAttribute("employeeList", employeeList);
			return "employee/list";
		}
		
		//リストから、インデックス番号×10番目～10件分の従業員情報を切り出す
		//【例】インデックス番号が2の場合：リストの20-29番目の従業員情報を表示
		Integer numOfTopData = index*10;
		Integer numOfButtomData = numOfTopData + 10;
		if(numOfButtomData >=  allEmployees.size()) {
			numOfButtomData = allEmployees.size();
		}
		List<Employee> employeeList = allEmployees.subList(numOfTopData, numOfButtomData);
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
		// 検索フォーム空欄でボタンを押した場合は全件出力
		if (aPartOfName == null) {
			return "foward:/employee/showList";
		}

		List<Employee> employeeList = employeeService.findByAPartOfName(aPartOfName);

		// 該当情報なしの場合はメッセージ出力・従業員情報全件出力
		if (employeeList.size() == 0) {
			model.addAttribute("employeeList", employeeList);
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
