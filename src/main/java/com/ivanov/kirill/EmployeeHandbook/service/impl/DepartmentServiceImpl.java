package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.model.Department;
import com.ivanov.kirill.EmployeeHandbook.repository.DepartmentRepository;
import com.ivanov.kirill.EmployeeHandbook.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> getMatchingDepartments(Example<Department> departmentExample) {
        return departmentRepository.findAll(departmentExample);
    }

    @Override
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public Boolean deleteDepartment(Long id) {
        try {
            departmentRepository.deleteById(id);
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }

    @Override
    public Optional<Department> addDepartment(Department department) {
        return Optional.of(departmentRepository.save(department));
    }

    @Override
    public Optional<Department> updateDepartment(Long id, Department department) {
        Optional<Department> departmentFromDB = departmentRepository.findById(id);
        if (!departmentFromDB.isPresent())
            return Optional.empty();

        department.setId(departmentFromDB.get().getId());

        return Optional.of(departmentRepository.save(department));
    }
}
