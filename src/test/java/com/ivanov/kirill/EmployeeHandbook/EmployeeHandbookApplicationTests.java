package com.ivanov.kirill.EmployeeHandbook;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(args = {
		"--databaseInitializationFile=init.txt",
		"--spring.mail.password=",
		"--spring.mail.username=kirdmiv@gmail.com",
		"--spring.datasource.username=employee_handbook",
		"--spring.datasource.password=ggEmployee"
})
@AutoConfigureMockMvc
class EmployeeHandbookApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithUserDetails(value = "Hill_8", userDetailsServiceBeanName = "userDetailService") // ADMIN
	void testEmployeesSearch() throws Exception {
		mockMvc.perform(get("/api/employees/find"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":4,\"name\":\"Curran\",\"surname\":\"Beck\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"1999-04-05\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"},{\"id\":2,\"name\":\"Kirill\",\"surname\":\"Ivanov\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2003-05-25\"},{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"}]"));
		mockMvc.perform(get("/api/employees/employee/2"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":2,\"name\":\"Kirill\",\"surname\":\"Ivanov\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2003-05-25\"}"));
	}

	@Test
	@WithUserDetails(value = "Hill_8", userDetailsServiceBeanName = "userDetailService") // ADMIN
	void testTeamsSearch() throws Exception {
		mockMvc.perform(get("/api/teams/find"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}]"));
		mockMvc.perform(get("/api/teams/team/5"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}"));
	}

	@Test
	@WithUserDetails(value = "Hill_8", userDetailsServiceBeanName = "userDetailService") // ADMIN
	void testDepartmentsSearch() throws Exception {
		mockMvc.perform(get("/api/departments/find"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":3,\"title\":\"Test department\",\"description\":\"Department description\",\"head\":{\"id\":4,\"name\":\"Curran\",\"surname\":\"Beck\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"1999-04-05\"},\"teams\":[{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}]}]"));
		mockMvc.perform(get("/api/departments/department/3"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":3,\"title\":\"Test department\",\"description\":\"Department description\",\"head\":{\"id\":4,\"name\":\"Curran\",\"surname\":\"Beck\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"1999-04-05\"},\"teams\":[{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}]}"));
	}

	@Test
	@WithUserDetails(value = "Hill_8", userDetailsServiceBeanName = "userDetailService") // ADMIN
	void testOrganizationsSearch() throws Exception {
		mockMvc.perform(get("/api/organizations/find"))
				.andExpect(status().isOk())
				.andExpect(content().json("[{\"id\":1,\"title\":\"Test$ org\",\"description\":\"Org description\",\"head\":{\"id\":2,\"name\":\"Kirill\",\"surname\":\"Ivanov\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2003-05-25\"},\"headQuarters\":\"Mountain View, California, United States\",\"departments\":[{\"id\":3,\"title\":\"Test department\",\"description\":\"Department description\",\"head\":{\"id\":4,\"name\":\"Curran\",\"surname\":\"Beck\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"1999-04-05\"},\"teams\":[{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}]}]}]"));
		mockMvc.perform(get("/api/organizations/organization/1"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"id\":1,\"title\":\"Test$ org\",\"description\":\"Org description\",\"head\":{\"id\":2,\"name\":\"Kirill\",\"surname\":\"Ivanov\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2003-05-25\"},\"headQuarters\":\"Mountain View, California, United States\",\"departments\":[{\"id\":3,\"title\":\"Test department\",\"description\":\"Department description\",\"head\":{\"id\":4,\"name\":\"Curran\",\"surname\":\"Beck\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"1999-04-05\"},\"teams\":[{\"id\":5,\"title\":\"Test team2\",\"description\":\"Team description\",\"head\":{\"id\":6,\"name\":\"Team\",\"surname\":\"Head\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2005-06-01\"},\"employees\":[{\"id\":7,\"name\":\"Tennyson\",\"surname\":\"Garnett\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2012-12-12\"},{\"id\":8,\"name\":\"Delano\",\"surname\":\"Hill\",\"email\":\"kirdmiv@gmail.com\",\"birthday\":\"2002-06-15\"}]}]}]}"));
	}

	@Test
	@WithUserDetails(value = "Garnett_7", userDetailsServiceBeanName = "userDetailService") // USER
	void testUserAccess() throws Exception {
		// employees
		mockMvc.perform(get("/api/employees/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/employees/employee/2"))
				.andExpect(status().isOk());

		// teams
		mockMvc.perform(get("/api/teams/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/teams/team/5"))
				.andExpect(status().isOk());

		// departments
		mockMvc.perform(get("/api/departments/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/departments/department/3"))
				.andExpect(status().isOk());

		// organizations
		mockMvc.perform(get("/api/organizations/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/organizations/organization/1"))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Ivanov_2", userDetailsServiceBeanName = "userDetailService") // MOD
	void testModeratorAccess() throws Exception {
		// employees
		mockMvc.perform(get("/api/employees/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/employees/employee/2"))
				.andExpect(status().isOk());

		// teams
		mockMvc.perform(get("/api/teams/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/teams/team/5"))
				.andExpect(status().isOk());

		// departments
		mockMvc.perform(get("/api/departments/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/departments/department/3"))
				.andExpect(status().isOk());

		// organizations
		mockMvc.perform(get("/api/organizations/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/organizations/organization/1"))
				.andExpect(status().isOk());
	}

	@Test
	@WithUserDetails(value = "Hill_8", userDetailsServiceBeanName = "userDetailService") // ADMIN
	void testAdminAccess() throws Exception {
		// employees
		mockMvc.perform(get("/api/employees/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/employees/employee/2"))
				.andExpect(status().isOk());

		// teams
		mockMvc.perform(get("/api/teams/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/teams/team/5"))
				.andExpect(status().isOk());

		// departments
		mockMvc.perform(get("/api/departments/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/departments/department/3"))
				.andExpect(status().isOk());

		// organizations
		mockMvc.perform(get("/api/organizations/find"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/api/organizations/organization/1"))
				.andExpect(status().isOk());
	}
}
