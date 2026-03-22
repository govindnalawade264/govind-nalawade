package com.emp.management.controller;

import com.emp.management.model.Employee;
import com.emp.management.service.DepartmentService;
import com.emp.management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    // List all employees
    @GetMapping
    public String listEmployees(Model model,
                                 @RequestParam(value = "keyword", required = false) String keyword) {
        List<Employee> employees;
        if (keyword != null && !keyword.isEmpty()) {
            employees = employeeService.searchEmployees(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            employees = employeeService.getAllEmployees();
        }
        model.addAttribute("employees", employees);
        model.addAttribute("totalCount", employees.size());
        return "employee/list";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentService.getAllDepartments());
        model.addAttribute("pageTitle", "Add Employee");
        return "employee/form";
    }

    // Save new employee
    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", employee.getId() == null ? "Add Employee" : "Edit Employee");
            return "employee/form";
        }

        // Check duplicate email for new employee
        if (employee.getId() == null && employeeService.emailExists(employee.getEmail())) {
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("emailError", "Email already exists!");
            model.addAttribute("pageTitle", "Add Employee");
            return "employee/form";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage",
                (employee.getId() == null ? "Employee added" : "Employee updated") + " successfully!");
        return "redirect:/employees";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            model.addAttribute("departments", departmentService.getAllDepartments());
            model.addAttribute("pageTitle", "Edit Employee");
            return "employee/form";
        }
        ra.addFlashAttribute("errorMessage", "Employee not found!");
        return "redirect:/employees";
    }

    // View employee details
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            model.addAttribute("employee", employee.get());
            return "employee/view";
        }
        ra.addFlashAttribute("errorMessage", "Employee not found!");
        return "redirect:/employees";
    }

    // Delete employee
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes ra) {
        try {
            employeeService.deleteEmployee(id);
            ra.addFlashAttribute("successMessage", "Employee deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error deleting employee!");
        }
        return "redirect:/employees";
    }
}
