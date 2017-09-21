package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.config.TestConfig;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
@Transactional
@Sql({"/schema.sql", "/data.sql"})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void shouldGetUserByIdTest() throws Exception {
        val user = userService.getById(1);
        assertEquals(1, user.getId());
        assertEquals("Homer", user.getName());
        assertEquals(39, user.getAge());
        assertTrue(user.isEnabled());
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotGetUserByIdTest() throws Exception {
        userService.getById(10);
    }

    @Test
    public void shouldGetActiveUserTest() throws Exception {
        val user = userService.getActiveUser(1);
        assertEquals(1, user.getId());
        assertEquals("Homer", user.getName());
        assertEquals(39, user.getAge());
        assertTrue(user.isEnabled());
    }

    @Test(expected = InactiveUserException.class)
    public void shouldNotGetActiveUserTest() throws Exception {
        userService.getActiveUser(2);
    }

    @Test
    public void shouldUpdateNameTest() throws Exception {
        userService.updateName(1, "Maggie");

        val user = userService.getById(1);
        assertEquals(1, user.getId());
        assertEquals("Maggie", user.getName());
        assertEquals(39, user.getAge());
        assertTrue(user.isEnabled());
    }

    @Test(expected = InactiveUserException.class)
    public void shouldNotUpdateNameTest() throws Exception {
        userService.updateName(2, "Maggie");
    }

    @Test
    public void shouldUpdateAgeTest() throws Exception {
        userService.updateAge(1, 35);

        val user = userService.getById(1);
        assertEquals(1, user.getId());
        assertEquals("Homer", user.getName());
        assertEquals(35, user.getAge());
        assertTrue(user.isEnabled());
    }

    @Test(expected = InactiveUserException.class)
    public void shouldNotUpdateAgeTest() throws Exception {
        userService.updateAge(2, 18);
    }

    @Test
    public void shouldDeactivateUserTest() throws Exception {
        userService.deactivate(1);

        val user = userService.getById(1);
        assertEquals(1, user.getId());
        assertEquals("Homer", user.getName());
        assertEquals(39, user.getAge());
        assertFalse(user.isEnabled());
    }

    @Test(expected = InactiveUserException.class)
    public void shouldNotDeactivateUserTest() throws Exception {
        userService.deactivate(2);
    }

    @Test (expected = UserNotFoundException.class)
    public void shouldDeleteUserTest() throws Exception {
        userService.delete(1);

        userService.getById(1);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void shouldNotDeleteUserTest() throws Exception {
        userService.delete(10);
    }

    @Test
    public void shouldCreateUserTest() throws Exception {
        val user = userService.create("Marge", 37);
        assertEquals(3, user.getId());
        assertEquals("Marge", user.getName());
        assertEquals(37, user.getAge());
        assertTrue(user.isEnabled());
    }
}
