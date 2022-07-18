package com.ivanov.kirill.EmployeeHandbook.controller;

import com.ivanov.kirill.EmployeeHandbook.dto.TeamDto;
import com.ivanov.kirill.EmployeeHandbook.email.EmailService;
import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.service.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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

    @Autowired
    private EmailService emailService;

    @GetMapping("/find")
    @ResponseBody
    public List<TeamDto> teams(
            @RequestParam(required = false) String title,
            @RequestParam(required = false, defaultValue = "0", name = "pgNum") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "100", name = "pgSize") Integer pageSize,
            @RequestParam(required = false, defaultValue = "title", name = "sort") String sortByField
    ) {
        ExampleMatcher teamMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnorePaths("id");
        Example<Team> query = Example.of(
                modelMapper.map(new TeamDto(null, title, null, null, null, null), Team.class),
                teamMatcher
        );
        Pageable rules = PageRequest.of(pageNumber, pageSize, Sort.by(sortByField));
        return teamService
                .getMatchingTeams(query, rules)
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
        if (teamService.deleteTeam(id)) {
            emailService.sendMailMessage(
                    "kirdmiv@gmail.com",
                    "Team deleted",
                    "Team with id=" + id + " has been deleted successfully."
            );
            return ResponseEntity.ok("Successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid id");
    }

    @PostMapping("/add")
    public ResponseEntity<TeamDto> addTeam(
            @RequestBody TeamDto teamRequest
    ) {
        Team team = modelMapper.map(teamRequest, Team.class);
        Optional<Team> addedTeam = teamService.addTeam(team);
        if (addedTeam.isPresent()) {
            TeamDto addedTeamDto = modelMapper.map(addedTeam.get(), TeamDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Team added",
                    addedTeamDto
            );
            return ResponseEntity.ok(addedTeamDto);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @PathVariable Long id,
            @RequestBody TeamDto teamRequest
    ) {
        Team team = modelMapper.map(teamRequest, Team.class);
        Optional<Team> updatedTeam = teamService.updateTeam(id, team);
        if (updatedTeam.isPresent()) {
            TeamDto updatedTeamDto = modelMapper.map(updatedTeam.get(), TeamDto.class);
            emailService.sendMailEntity(
                    "kirdmiv@gmail.com",
                    "Team updated",
                    updatedTeamDto
            );
            return ResponseEntity.ok(updatedTeamDto);
        }
        return ResponseEntity.badRequest().body(null);
    }
}
