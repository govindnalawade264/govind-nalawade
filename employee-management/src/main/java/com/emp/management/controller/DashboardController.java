package com.emp.management.controller;

import com.emp.management.service.DepartmentService;
import com.emp.management.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Long> stats = employeeService.getDashboardStats();
        model.addAttribute("totalEmployees", stats.get("totalEmployees"));
        model.addAttribute("activeEmployees", stats.get("activeEmployees"));
        model.addAttribute("inactiveEmployees", stats.get("inactiveEmployees"));
        model.addAttribute("onLeaveEmployees", stats.get("onLeaveEmployees"));
        model.addAttribute("totalDepartments", departmentService.getTotalCount());
        model.addAttribute("recentEmployees", employeeService.getAllEmployees().stream().limit(5).toList());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
