package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.TeamDto;
import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

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
        return teamService
                .getMatchingTeams(query)
                .stream()
                .map(team -> modelMapper.map(team, TeamDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> findTeamById(
            @PathVariable Long id
    ) {
        Optional<Team> team = teamService.getTeamById(id);
        return team.map(
                value -> ResponseEntity.ok(modelMapper.map(value, TeamDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteTeamById(
            @RequestParam Long id
    ) {
        if (teamService.deleteTeam(id))
            return ResponseEntity.ok("Successful");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<TeamDto> addTeam(
            @RequestBody TeamDto teamRequest
    ) {
        Team team = modelMapper.map(teamRequest, Team.class);
        Optional<Team> addedTeam = teamService.addTeam(team);
        return addedTeam.map(
                value -> ResponseEntity.ok(modelMapper.map(value, TeamDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @PathVariable Long id,
            @RequestBody TeamDto teamRequest
    ) {
        Team team = modelMapper.map(teamRequest, Team.class);
        Optional<Team> updatedTeam = teamService.updateTeam(id, team);
        return updatedTeam.map(
                value -> ResponseEntity.ok(modelMapper.map(value, TeamDto.class))
        ).orElseGet(() -> ResponseEntity.badRequest().body(null));
    }
}
