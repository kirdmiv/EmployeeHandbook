package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.TeamDto;
import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/find")
    @ResponseBody
    public List<TeamDto> teams(
            @RequestParam(required = false) String title
    ) {
        ExampleMatcher teamMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Team> query = Example.of(
                modelMapper.map(new TeamDto(null, title, null, null, null, null), Team.class),
                teamMatcher
        );
        return teamRepository
                .findAll(query)
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/findById")
    @ResponseBody
    public Optional<TeamDto> findTeamById(
            @RequestParam Long id
    ) {
        return teamRepository
                .findById(id)
                .map(team -> modelMapper.map(team, TeamDto.class));
    }
}
