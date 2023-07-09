package com.example.myshop;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.*;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String rawPassword="123123123";
        String encodePassword=bCryptPasswordEncoder.encode(rawPassword);
        System.out.println(encodePassword);

        boolean matches=bCryptPasswordEncoder.matches(rawPassword,encodePassword);
        assertThat(matches).isTrue();
    }
}
