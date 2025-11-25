package com.project.ProjectManagement.userdetailsimpl;

import com.project.ProjectManagement.model.User;
import com.project.ProjectManagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        User user = userRepo.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + emailAddress));

        return new UserDetailsImpl(user);
    }
}