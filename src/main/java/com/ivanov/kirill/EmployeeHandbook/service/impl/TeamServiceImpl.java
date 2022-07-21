package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.model.Employee;
import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.repository.EmployeeRepository;
import com.ivanov.kirill.EmployeeHandbook.repository.TeamRepository;
import com.ivanov.kirill.EmployeeHandbook.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Team> getMatchingTeams(Example<Team> teamExample, Pageable rules) {
        return teamRepository.findAll(teamExample, rules).toList();
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

    @Override
    public Boolean deleteTeam(Long id) {
        try {
            teamRepository.deleteById(id);
            return true;
        } catch (DataAccessException exception) {
            return false;
        }
    }

    @Override
    public Optional<Team> addTeam(Team team) {
        return Optional.of(teamRepository.save(team));
    }

    @Override
    public Optional<Team> updateTeam(Long id, Team team) {
        Optional<Team> teamFromDB = teamRepository.findById(id);
        if (!teamFromDB.isPresent())
            return Optional.empty();

        team.setId(teamFromDB.get().getId());

        return Optional.of(teamRepository.save(team));
    }

    @Override
    public Optional<Team> updateTeamEmployees(Long teamId, List<Long> employeesIds) {
        Optional<Team> teamFromDB = teamRepository.findById(teamId);
        if (!teamFromDB.isPresent())
            return Optional.empty();

        List<Employee> employees = new ArrayList<>();
        AtomicBoolean badRequest = new AtomicBoolean(false);
        employeesIds.forEach(id -> {
            Optional<Employee> employee = employeeRepository.findById(id);
            if (!employee.isPresent())
                badRequest.set(true);
            else
                employees.add(employee.get());
        });

        if (badRequest.get())
            return Optional.empty();

        teamFromDB.get().setEmployees(employees);

        return Optional.of(teamRepository.save(teamFromDB.get()));
    }
}
