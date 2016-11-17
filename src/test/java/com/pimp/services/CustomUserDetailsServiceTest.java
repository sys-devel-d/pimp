package com.pimp.services;

import com.pimp.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    private CustomUserDetailsService userDetailsService;

    @Before
    public void setUp() throws Exception {
        userDetailsService = new CustomUserDetailsService(userService);
    }


    @Test
    public void testLoadUserByUsername() throws Exception {
        when(userService.findByUserName("PatrickBateman")).thenReturn(new User().setUserName("PatrickBateman"));

        UserDetails userDetails = userDetailsService.loadUserByUsername("PatrickBateman");

        assertThat(userDetails instanceof UserDetails);
        assertThat(userDetails.getUsername()).isEqualTo("PatrickBateman");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserThrowsUserNotFoundException() throws Exception {
        when(userService.findByUserName(any())).thenThrow(new UsernameNotFoundException("not found"));

        userDetailsService.loadUserByUsername("TylerDurden");
    }
}