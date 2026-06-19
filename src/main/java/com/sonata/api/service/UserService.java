package com.sonata.api.service;

import com.sonata.api.dto.UserDTO;
import com.sonata.api.exception.BusinessException;
import com.sonata.api.exception.NotFoundException;
import com.sonata.api.model.User;
import com.sonata.api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negocio relacionadas a usuarios. */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario " + id + " nao encontrado"));
    }

    /** Cria um novo usuario com senha em BCrypt. */
    public User create(UserDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessException("Email ja cadastrado: " + dto.email());
        }
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(passwordEncoder.encode(dto.password()))
                .preferredAudioQuality(dto.preferredAudioQuality())
                .build();
        return userRepository.save(user);
    }

    public User update(Long id, UserDTO dto) {
        User user = findById(id);
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPreferredAudioQuality(dto.preferredAudioQuality());
        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }
        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Usuario " + id + " nao encontrado");
        }
        userRepository.deleteById(id);
    }
}
