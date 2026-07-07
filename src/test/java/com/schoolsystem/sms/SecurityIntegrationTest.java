package com.schoolsystem.sms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
    "DB_URL=jdbc:h2:mem:schoolsystem",
    "DB_DIALECT=org.hibernate.dialect.H2Dialect"
})
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthenticatedUserShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"SUPER_DOS"})
    public void adminCanAccessUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "teacher1", roles = {"TEACHER"})
    public void teacherCannotAccessUsersPage() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "secretary", roles = {"SECRETARY"})
    public void secretaryCannotAccessResultsPage() throws Exception {
        mockMvc.perform(get("/results"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "dos", roles = {"DOS"})
    public void dosCanAccessReportsPage() throws Exception {
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk());
    }
}
