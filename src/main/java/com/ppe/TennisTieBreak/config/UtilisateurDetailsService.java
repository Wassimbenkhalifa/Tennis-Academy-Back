package com.ppe.TennisTieBreak.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UtilisateurDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
