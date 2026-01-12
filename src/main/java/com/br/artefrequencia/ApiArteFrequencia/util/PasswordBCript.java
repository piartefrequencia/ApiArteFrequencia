package com.br.artefrequencia.ApiArteFrequencia.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public class PasswordBCript {
    
   private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encoder(String raw) {
        return encoder.encode(raw);
    }

    public static boolean matches(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }

}
