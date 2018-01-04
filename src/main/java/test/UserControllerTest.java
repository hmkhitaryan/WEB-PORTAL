package test;


import com.egs.account.config.hibernate.HibernateConfiguration;
import com.egs.account.controller.UserController;
import com.egs.account.exception.UserNotFoundException;
import com.egs.account.model.User;
import com.egs.account.service.security.SecurityService;
import com.egs.account.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@ContextConfiguration(locations = {
        "classpath:/appconfig-security.xml"},
        classes = {HibernateConfiguration.class})
@WebAppConfiguration
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @Mock
    private UserService userServiceMock;

    @Mock
    private SecurityService securityServiceMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = standaloneSetup(controller).setSingleView(new InternalResourceView("/WEB-INF/views/welcomeUser.jsp")).build();;
    }

    @Test(expected = AuthenticationException.class)
    public void testLoginFailure() throws AuthenticationException {
        securityServiceMock.findLoggedInUsername();
    }

    @Test
    public void findByUserName() throws Exception {
	    final User expectedUser = createUser(1L);
	    Mockito.when(userServiceMock.findByUsername("hmkhiaryan")).thenReturn(expectedUser);
    }

    @Test
    public void findAll_ShouldAddUserEntriesToList() throws Exception {
	    final User userFirst = new User();
	    userFirst.setUsername("hmkhiaryan");
        userFirst.setEmail("hayk_84@mail.ru");
        userFirst.setFirstName("Hayk");
        userFirst.setLastName("Mkhitaryan");
        userFirst.setId(1L);

	    final User userSecond = new User();
	    userSecond.setUsername("amkhiaryan");
        userSecond.setEmail("aelita_88@mail.ru");
        userSecond.setFirstName("Aelita");
        userSecond.setLastName("Ghazaryan");
        userSecond.setId(2L);

        when(userServiceMock.findAllUsers()).thenReturn(Arrays.asList(userFirst, userSecond));
    }

    @Test
    public void findById_UserEntryNotFound_ShouldRender404View() throws Exception {
        when(userServiceMock.findById(1L)).thenThrow(new UserNotFoundException(""));
    }

    @Test
    public void shouldShowRecentUsers() throws Exception {
	    final User expectedUser = createUser(1L);
	    userServiceMock = mock(UserService.class);
        when(userServiceMock.findById(1L)).thenReturn(expectedUser);
    }

    private User createUser(Long userId) {

	    final User user = new User();
	    user.setId(userId);
        user.setUsername("hmkhiaryan");
        user.setEmail("hayk_84@mail.ru");
        user.setFirstName("Hayk");
        user.setLastName("Mkhitaryan");

        return user;
    }


}