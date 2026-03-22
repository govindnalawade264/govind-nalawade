package com.emp.management.service;

import com.emp.management.model.Employee;
import com.emp.management.model.Employee.EmployeeStatus;
import com.emp.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    // Get all employees with sorting
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "firstName"));
    }

    // Get paginated employees
    public Page<Employee> getEmployeesPaged(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equals("asc") ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return employeeRepository.findAll(pageable);
    }

    // Get employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    // Save employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Update employee
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Search employees
    public List<Employee> searchEmployees(String keyword) {
        return employeeRepository.searchEmployees(keyword);
    }

    // Get employees by department
    public List<Employee> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    // Get employees by status
    public List<Employee> getEmployeesByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status);
    }

    // Get dashboard statistics
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalEmployees", employeeRepository.count());
        stats.put("activeEmployees", employeeRepository.countByStatus(EmployeeStatus.ACTIVE));
        stats.put("inactiveEmployees", employeeRepository.countByStatus(EmployeeStatus.INACTIVE));
        stats.put("onLeaveEmployees", employeeRepository.countByStatus(EmployeeStatus.ON_LEAVE));
        return stats;
    }

    // Check email exists
    public boolean emailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }

    // Total count
    public long getTotalCount() {
        return employeeRepository.count();
    }
}
