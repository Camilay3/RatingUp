package com.quadcore.Ratingup.model.profile;

import com.quadcore.Ratingup.dto.profile.ProfileRequestDTO;
import com.quadcore.Ratingup.dto.profile.ProfileUpdateRequestDTO;
import com.quadcore.Ratingup.enums.Roles;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {

    @Test
    void testUserBooleans() {
        User user = new User();
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
        user.setRole(Roles.USER);
        assertEquals(1, user.getAuthorities().size());
        user.setEmail("test@test.com");
        assertEquals("test@test.com", user.getUsername());
    }

    @Test
    void testProfileRequestDTO() {
        ProfileRequestDTO dto = new ProfileRequestDTO("Name", "Nick", "test@test.com", "11111111", "pw");
        assertEquals("Name", dto.name());
        assertEquals("Nick", dto.nickname());
        assertEquals("test@test.com", dto.email());
        
        User user = new User();
        user.setName("N");
        user.setNickname("Ni");
        user.setEmail("t@t.com");
        user.setTelefone("111");
        user.setPassword("pw");
        ProfileRequestDTO dto2 = new ProfileRequestDTO(user);
        assertEquals("N", dto2.name());
    }

    @Test
    void testProfileUpdateRequestDTO() {
        ProfileUpdateRequestDTO dto = new ProfileUpdateRequestDTO("N", "Ni", "e@e.com", "111");
        assertEquals("N", dto.name());
    }
}
