package com.ivanov.kirill.EmployeeHandbook.service;

import com.ivanov.kirill.EmployeeHandbook.model.Team;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public interface TeamService {
    List<Team> getMatchingTeams(Example<Team> teamExample);

    Optional<Team> getTeamById(Long id);

    Boolean deleteTeam(Long id);

    Optional<Team> addTeam(Team team);

    Optional<Team> updateTeam(Long id, Team team);
}
