package com.ivanov.kirill.EmployeeHandbook.utils;

import com.ivanov.kirill.EmployeeHandbook.model.*;
import com.ivanov.kirill.EmployeeHandbook.service.DepartmentService;
import com.ivanov.kirill.EmployeeHandbook.service.EmployeeService;
import com.ivanov.kirill.EmployeeHandbook.service.OrganizationService;
import com.ivanov.kirill.EmployeeHandbook.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DatabaseInitializer implements ApplicationRunner {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private OrganizationService organizationService;

    private Organization currentOrganization = null;
    private Team currentTeam = null;
    private Department currentDepartment = null;
    private final List<Employee> teamEmployees = new ArrayList<>();
    private final List<Department> currentDepartments = new ArrayList<>();
    private final List<Team> currentTeams = new ArrayList<>();
    private final Stack<Unit> unitStack = new Stack<>();

    private final String ARG_NAME = "databaseInitializationFile";

    @Override
    public void run(ApplicationArguments args) {
        if (!args.containsOption(ARG_NAME) || args.getOptionValues(ARG_NAME).size() == 0)
            return;

        List<String> files = args.getOptionValues(ARG_NAME);
        files.forEach(file -> {
            try {
                parseFile(new File(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void parseFile(File file) throws FileNotFoundException {
        FileReader inputStream = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(inputStream);
        try {
            while (bufferedReader.ready())
                parseLine(bufferedReader.readLine());
            flushData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            close(bufferedReader);
            close(inputStream);
        }
    }

    private void parseLine(String line) {
        String[] values = line.split("(?<!\\\\)\\$");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].replace("\\$", "$");
        }
        switch (values[0]) {
            case "organization":
                setUpNewOrganization(values);
                break;
            case "department":
                setUpDepartment(values);
                break;
            case "team":
                setUpTeam(values);
                break;
            case "employee":
                setUpEmployee(values);
                break;
            default:
                throw new RuntimeException("Wrong file format");
        }
    }

    private void setUpEmployee(String[] values) {
        Employee currentEmployee = new Employee();
        currentEmployee.setName(values[1]);
        currentEmployee.setSurname(values[2]);
        currentEmployee.setEmail(values[3]);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            currentEmployee.setBirthday(format.parse(values[4]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        currentEmployee.setWorkplace(unitStack.peek());

        if (values.length > 5 && Objects.equals(values[5], "ADMIN"))
            currentEmployee.setRole(UserRole.ROLE_ADMIN);
        else if (values.length > 5 && Objects.equals(values[5], "MOD"))
            currentEmployee.setRole(UserRole.ROLE_MODERATOR);
        else
            currentEmployee.setRole(UserRole.ROLE_USER);

        Optional<Employee> addedEmployee = employeeService.addEmployee(currentEmployee, null);
        if (!addedEmployee.isPresent())
            throw new RuntimeException("Failed to add employee: " + currentEmployee);
        if (unitStack.peek().getHead() == null)
            unitStack.peek().setHead(addedEmployee.get());
        else
            teamEmployees.add(addedEmployee.get());
    }

    private void setUpTeam(String[] values) {
        flushTeam();
        currentTeam = new Team();
        setUpUnit(currentTeam, values);
        currentTeam.setDepartment((Department) unitStack.peek());
        currentTeam.setEmployees(teamEmployees);
        unitStack.add(currentTeam);

        Optional<Team> addedTeam = teamService.addTeam(currentTeam);
        if (!addedTeam.isPresent())
            throw new RuntimeException("Failed to add team: " + currentTeam.toString());

        currentTeam = addedTeam.get();
    }

    private void flushTeam() {
        if (currentTeam == null)
            return;

        currentTeam.setEmployees(teamEmployees);
        currentTeam = getFromOptional(
                teamService.updateTeam(currentTeam.getId(), currentTeam),
                "Failed to update team: " + currentTeam
        );
        currentTeams.add(currentTeam);

        Employee head = currentTeam.getHead();
        head.setWorkplace(currentTeam);
        employeeService.updateEmployee(head.getId(), head, null);

        currentTeam.getEmployees().forEach(employee -> {
            employee.setWorkplace(currentTeam);
            employeeService.updateEmployee(employee.getId(), employee, null);
        });

        teamEmployees.clear();
        unitStack.pop();
        currentTeam = null;
    }

    private void setUpDepartment(String[] values) {
        flushDepartment();
        currentDepartment = new Department();
        setUpUnit(currentDepartment, values);
        currentDepartment.setOrganization((Organization) unitStack.peek());
        currentDepartment.setTeams(null);
        unitStack.add(currentDepartment);

        Optional<Department> addedDepartment = departmentService.addDepartment(currentDepartment);
        if (!addedDepartment.isPresent())
            throw new RuntimeException("Failed to add department: " + currentDepartment.toString());

        currentDepartment = addedDepartment.get();
    }

    private void flushDepartment() {
        if (currentDepartment == null)
            return;

        currentDepartment.setTeams(currentTeams);
        currentDepartment = getFromOptional(
                departmentService.updateDepartment(currentDepartment.getId(), currentDepartment),
                "Failed to update department: " + currentDepartment
        );

        Employee head = currentDepartment.getHead();
        head.setWorkplace(currentDepartment);
        employeeService.updateEmployee(head.getId(), head, null);

        currentTeams.clear();
        unitStack.pop();
        currentDepartment = null;
    }

    private void setUpNewOrganization(String[] values) {
        flushOrganization();
        currentOrganization = new Organization();
        currentOrganization = new Organization();
        setUpUnit(currentOrganization, values);
        currentOrganization.setHeadQuarters(values[3]);
        currentOrganization.setDepartments(null);
        unitStack.add(currentOrganization);

        Optional<Organization> addedOrganization = organizationService.addOrganization(currentOrganization);
        if (!addedOrganization.isPresent())
            throw new RuntimeException("Failed to add organization: " + currentOrganization.toString());

        currentOrganization = addedOrganization.get();
    }

    private void flushOrganization() {
        if (currentOrganization == null)
            return;

        currentOrganization.setDepartments(currentDepartments);
        currentOrganization = getFromOptional(
                organizationService.updateOrganization(currentOrganization.getId(), currentOrganization),
                "Failed to update organization: " + currentOrganization
        );

        Employee head = currentOrganization.getHead();
        head.setWorkplace(currentOrganization);
        employeeService.updateEmployee(head.getId(), head, null);

        currentDepartments.clear();
        unitStack.pop();
        currentOrganization = null;
    }

    private void setUpUnit(Unit unit, String[] values) {
        unit.setTitle(values[1]);
        unit.setDescription(values[2]);
        unit.setHead(null);
    }

    private void flushData() {
        flushTeam();
        flushDepartment();
        flushOrganization();
    }

    private static void close(Closeable c) {
        if (c == null)
            return;

        try {
            c.close();
        } catch (IOException e) {
            //log the exception
        }
    }

    private static <T> T getFromOptional(Optional<T> value, String errorMessage) {
        if (!value.isPresent())
            throw new RuntimeException(errorMessage);
        return value.get();
    }
}
