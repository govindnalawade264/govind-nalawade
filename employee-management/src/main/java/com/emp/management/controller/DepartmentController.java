package com.emp.management.controller;

import com.emp.management.model.Department;
import com.emp.management.service.DepartmentService;
import com.emp.management.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    // List all departments
    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "department/list";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("pageTitle", "Add Department");
        return "department/form";
    }

    // Save department
    @PostMapping("/save")
    public String saveDepartment(@Valid @ModelAttribute("department") Department department,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", department.getId() == null ? "Add Department" : "Edit Department");
            return "department/form";
        }
        departmentService.saveDepartment(department);
        ra.addFlashAttribute("successMessage",
                (department.getId() == null ? "Department added" : "Department updated") + " successfully!");
        return "redirect:/departments";
    }

    // Show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Department> dept = departmentService.getDepartmentById(id);
        if (dept.isPresent()) {
            model.addAttribute("department", dept.get());
            model.addAttribute("pageTitle", "Edit Department");
            return "department/form";
        }
        ra.addFlashAttribute("errorMessage", "Department not found!");
        return "redirect:/departments";
    }

    // View department with employees
    @GetMapping("/view/{id}")
    public String viewDepartment(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Department> dept = departmentService.getDepartmentById(id);
        if (dept.isPresent()) {
            model.addAttribute("department", dept.get());
            model.addAttribute("employees", employeeService.getEmployeesByDepartment(id));
            return "department/view";
        }
        ra.addFlashAttribute("errorMessage", "Department not found!");
        return "redirect:/departments";
    }

    // Delete department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes ra) {
        try {
            departmentService.deleteDepartment(id);
            ra.addFlashAttribute("successMessage", "Department deleted successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Cannot delete: Department has employees!");
        }
        return "redirect:/departments";
    }
}
