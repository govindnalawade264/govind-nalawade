package com.emp.management.config;

import com.emp.management.model.Department;
import com.emp.management.model.Employee;
import com.emp.management.model.Employee.EmployeeStatus;
import com.emp.management.model.Employee.Gender;
import com.emp.management.model.User;
import com.emp.management.repository.DepartmentRepository;
import com.emp.management.repository.EmployeeRepository;
import com.emp.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Create default admin user
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Default admin created: admin / admin123");
        }

        // Create departments
        if (departmentRepository.count() == 0) {
            Department it = new Department("Information Technology", "IT & Software Development", "Raj Kumar");
            Department hr = new Department("Human Resources", "HR & Recruitment", "Priya Sharma");
            Department finance = new Department("Finance", "Accounts & Finance", "Amit Gupta");
            Department sales = new Department("Sales & Marketing", "Sales & Business Development", "Neha Singh");
            Department ops = new Department("Operations", "Operations & Logistics", "Suresh Patel");

            departmentRepository.save(it);
            departmentRepository.save(hr);
            departmentRepository.save(finance);
            departmentRepository.save(sales);
            departmentRepository.save(ops);

            // Create sample employees
            createEmployee("Rahul", "Sharma", "rahul.sharma@company.com", "9876543210",
                    "Senior Developer", Gender.MALE, LocalDate.of(1990, 5, 15),
                    LocalDate.of(2020, 1, 10), 75000.0, EmployeeStatus.ACTIVE, it);

            createEmployee("Priya", "Patel", "priya.patel@company.com", "9876543211",
                    "HR Manager", Gender.FEMALE, LocalDate.of(1988, 8, 22),
                    LocalDate.of(2019, 3, 1), 65000.0, EmployeeStatus.ACTIVE, hr);

            createEmployee("Amit", "Verma", "amit.verma@company.com", "9876543212",
                    "Financial Analyst", Gender.MALE, LocalDate.of(1992, 2, 10),
                    LocalDate.of(2021, 6, 15), 60000.0, EmployeeStatus.ACTIVE, finance);

            createEmployee("Sneha", "Reddy", "sneha.reddy@company.com", "9876543213",
                    "Sales Executive", Gender.FEMALE, LocalDate.of(1995, 11, 3),
                    LocalDate.of(2022, 8, 20), 45000.0, EmployeeStatus.ON_LEAVE, sales);

            createEmployee("Vikram", "Singh", "vikram.singh@company.com", "9876543214",
                    "Operations Lead", Gender.MALE, LocalDate.of(1987, 7, 18),
                    LocalDate.of(2018, 4, 5), 80000.0, EmployeeStatus.ACTIVE, ops);

            System.out.println("✅ Sample data initialized successfully!");
        }
    }

    private void createEmployee(String firstName, String lastName, String email, String phone,
                                 String jobTitle, Gender gender, LocalDate dob, LocalDate joinDate,
                                 Double salary, EmployeeStatus status, Department dept) {
        Employee emp = new Employee();
        emp.setFirstName(firstName);
        emp.setLastName(lastName);
        emp.setEmail(email);
        emp.setPhone(phone);
        emp.setJobTitle(jobTitle);
        emp.setGender(gender);
        emp.setDateOfBirth(dob);
        emp.setJoinDate(joinDate);
        emp.setSalary(salary);
        emp.setStatus(status);
        emp.setDepartment(dept);
        employeeRepository.save(emp);
    }
}
