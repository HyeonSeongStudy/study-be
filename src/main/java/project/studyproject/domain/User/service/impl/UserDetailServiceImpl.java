package project.studyproject.domain.User.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.studyproject.domain.User.dto.UserDetailsDto;
import project.studyproject.domain.User.repository.UserRepository;
import project.studyproject.domain.User.service.UserDetailService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername : " + username);

        return new UserDetailsDto(userRepository.getByUid(username));
    }
}
