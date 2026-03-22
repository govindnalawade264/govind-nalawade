package com.emp.management.repository;

import com.emp.management.model.Employee;
import com.emp.management.model.Employee.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search by name, email, job title
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Employee> searchEmployees(@Param("keyword") String keyword);

    // Paginated search
    @Query("SELECT e FROM Employee e WHERE " +
           "LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Employee> searchEmployeesPaged(@Param("keyword") String keyword, Pageable pageable);

    // Find by department
    List<Employee> findByDepartmentId(Long departmentId);

    // Find by status
    List<Employee> findByStatus(EmployeeStatus status);

    // Count by status
    long countByStatus(EmployeeStatus status);

    // Count by department
    long countByDepartmentId(Long departmentId);

    // Find by email
    Employee findByEmail(String email);

    // Check email exists
    boolean existsByEmail(String email);
}
