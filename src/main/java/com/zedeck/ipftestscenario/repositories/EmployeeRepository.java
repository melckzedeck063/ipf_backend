package com.zedeck.ipftestscenario.repositories;

import com.zedeck.ipftestscenario.models.Employee;
import com.zedeck.ipftestscenario.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>  {

    Optional<Employee> findFirstByIdAndDeletedFalse(Long id);

    Optional<Employee> findFirstByEmail(String email);

    Page<Employee> findAllByDeletedFalse(Pageable pageable);
}
