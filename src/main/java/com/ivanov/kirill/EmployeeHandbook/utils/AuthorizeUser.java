package com.ivanov.kirill.EmployeeHandbook.utils;

import com.ivanov.kirill.EmployeeHandbook.model.Department;
import com.ivanov.kirill.EmployeeHandbook.model.Organization;
import com.ivanov.kirill.EmployeeHandbook.model.Team;
import com.ivanov.kirill.EmployeeHandbook.model.UserRole;
import com.ivanov.kirill.EmployeeHandbook.security.UserPrincipal;

public final class AuthorizeUser {
    private static Boolean checkAdmin(UserPrincipal principal) {
        return principal.getRole() == UserRole.ROLE_ADMIN;
    }

    public static Boolean checkOrganizationAccess(Organization organization, Object principal) {
        if (!(principal instanceof UserPrincipal))
            throw new RuntimeException("Given object should be an instance of UserPrinciple");
        return organization.getHead().getId() == ((UserPrincipal) principal).getId() || checkAdmin((UserPrincipal) principal);
    }

    public static Boolean checkDepartmentAccess(Department department, Object principal) {
        if (!(principal instanceof UserPrincipal))
            throw new RuntimeException("Given object should be an instance of UserPrinciple");
        return department.getHead().getId() == ((UserPrincipal) principal).getId() || checkOrganizationAccess(department.getOrganization(), principal);
    }

    public static Boolean checkTeamAccess(Team team, Object principal) {
        if (!(principal instanceof UserPrincipal))
            throw new RuntimeException("Given object should be an instance of UserPrinciple");
        return team.getHead().getId() == ((UserPrincipal) principal).getId() || checkDepartmentAccess(team.getDepartment(), principal);
    }
}
