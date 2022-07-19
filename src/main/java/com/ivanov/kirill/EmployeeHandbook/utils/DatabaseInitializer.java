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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

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
    private Employee currentEmployee = null;
    private final List<Employee> teamEmployees = new ArrayList<>();
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
        String[] values = line.split(" ");
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
        currentEmployee = new Employee();
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

        //Optional<Employee> addedEmployee = employeeService.addEmployee(currentEmployee, null);
        Optional<Employee> addedEmployee = Optional.of(currentEmployee);
        if (!addedEmployee.isPresent())
            throw new RuntimeException("Failed to add employee: " + currentEmployee.toString());
        if (unitStack.peek().getHead() == null)
            unitStack.peek().setHead(addedEmployee.get());
        else
            teamEmployees.add(addedEmployee.get());

        currentEmployee = null;
    }

    private void setUpTeam(String[] values) {
        //flushTeam();
        currentTeam = new Team();
        setUpUnit(currentTeam, values);
        currentTeam.setDepartment((Department) unitStack.peek());
        currentTeam.setEmployees(teamEmployees);
        unitStack.add(currentTeam);
    }

    private void flushTeam() {
        if (currentTeam == null)
            return;

        currentTeam.setEmployees(teamEmployees);
        Optional<Team> addedTeam = teamService.addTeam(currentTeam);
        if (!addedTeam.isPresent())
            throw new RuntimeException("Failed to add team: " + currentTeam.toString());

        Employee head = addedTeam.get().getHead();
        head.setWorkplace(addedTeam.get());
        employeeService.updateEmployee(head.getId(), head, null);

        addedTeam.get().getEmployees().forEach(employee -> {
            employee.setWorkplace(addedTeam.get());
            employeeService.updateEmployee(employee.getId(), employee, null);
        });

        teamEmployees.clear();
        unitStack.pop();
        currentTeam = null;
    }

    private void setUpDepartment(String[] values) {
        //flushDepartment();
        currentDepartment = new Department();
        setUpUnit(currentDepartment, values);
        currentDepartment.setOrganization((Organization) unitStack.peek());
        currentDepartment.setTeams();
        unitStack.add(currentDepartment);
    }

    private void flushDepartment() {
        if (currentDepartment == null)
            return;

        Optional<Department> addedDepartment = departmentService.addDepartment(currentDepartment);
        if (!addedDepartment.isPresent())
            throw new RuntimeException("Failed to add department: " + currentDepartment.toString());

        Employee head = addedDepartment.get().getHead();
        head.setWorkplace(addedDepartment.get());
        employeeService.updateEmployee(head.getId(), head, null);

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
    }

    private void flushOrganization() {
        if (currentOrganization == null)
            return;

        Optional<Organization> addedOrganization = organizationService.addOrganization(currentOrganization);
        if (!addedOrganization.isPresent())
            throw new RuntimeException("Failed to add organization: " + currentOrganization.toString());

        Employee head = addedOrganization.get().getHead();
        head.setWorkplace(addedOrganization.get());
        employeeService.updateEmployee(head.getId(), head, null);

        unitStack.pop();
        currentOrganization = null;
    }

    private void setUpUnit(Unit unit, String[] values) {
        unit.setTitle(values[1]);
        unit.setDescription(values[2]);
        unit.setHead(null);
    }

    private void flushData() {
        //flushTeam();
        //flushDepartment();
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
}
