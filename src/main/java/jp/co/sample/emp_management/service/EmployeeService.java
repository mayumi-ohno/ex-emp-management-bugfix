package jp.co.sample.emp_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 * 
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * 従業員情報を全件取得します.
	 * 
	 * @return 従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}
	
	/**
	 * 従業員情報のデータ数を取得します.
	 * @return データ数
	 */
	public Integer dataCount() {
		return employeeRepository.dataCount();
	}

	/**
	 * 従業員情報を取得します.
	 * 
	 * @param id ID
	 * @return 従業員情報
	 * @throws 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 従業員情報を更新します.
	 * 
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}

	/**
	 * 名前に引数の文字列を含む従業員の情報を取得します.
	 * 
	 * @param aPartOfName 名前の一部
	 * @return 従業員情報
	 */
	public List<Employee> findByAPartOfName(String aPartOfName) {
		List<Employee> employeeList = employeeRepository.findByAPartOfName(aPartOfName);
		return employeeList;
	}

	/**
	 * 引数データ番号から10件目のデータまで、合計10件の従業員情報を取得する.
	 * 
	 * @param topOfData    一番先頭のデータ番号
	 * @return 従業員一覧
	 */
	public List<Employee> findLimited(Integer topOfData) {
		return employeeRepository.findLimited(topOfData);
	}
}
