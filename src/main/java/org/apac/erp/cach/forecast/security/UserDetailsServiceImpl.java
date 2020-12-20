package org.apac.erp.cach.forecast.security;

import org.apac.erp.cach.forecast.persistence.entities.Role;
import org.apac.erp.cach.forecast.persistence.entities.User;
import org.apac.erp.cach.forecast.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;



    @Override
    @Transactional
    public UserDetails loadUserByUsername(String loginUserUSer) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(loginUserUSer)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));

        return UserDetailsImpl.build(user);
    }

    @Transactional
    public Set<Role> getRolesUser(String loginUserUSer) {
        User user = userRepository.findByUsername(loginUserUSer)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + loginUserUSer));
        return user.getRoles();
    }
}