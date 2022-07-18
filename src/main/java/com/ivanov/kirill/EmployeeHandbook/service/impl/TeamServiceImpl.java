package com.ivanov.kirill.EmployeeHandbook.service.impl;

import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.repository.TeamRepository;
import com.ivanov.kirill.EmployeeHandbook.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public List<Team> getMatchingTeams(Example<Team> teamExample) {
        return teamRepository.findAll(teamExample);
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
}
