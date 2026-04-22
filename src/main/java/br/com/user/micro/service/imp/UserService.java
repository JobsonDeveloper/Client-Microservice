package br.com.user.micro.service.imp;

import br.com.user.micro.domain.User;
import br.com.user.micro.dto.response.UserDto;
import br.com.user.micro.exceptions.UserAlreadyRegisteredException;
import br.com.user.micro.exceptions.UserNotFoundException;
import br.com.user.micro.repository.IUserRepository;
import br.com.user.micro.repository.IRoleRepository;
import br.com.user.micro.service.IUserService;
import br.com.user.micro.service.ISessionService;
import br.com.user.micro.util.TokenGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements IUserService {

    private final IUserRepository iUserRepository;

    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public User create(User user) {
        boolean userByCpf = iUserRepository.existsByCpf(user.getCpf());
        if (userByCpf) throw new UserAlreadyRegisteredException();

        boolean userByEmail = iUserRepository.existsByEmail(user.getEmail());
        if (userByEmail) throw new UserAlreadyRegisteredException();

        return iUserRepository.save(user);
    }

    @Override
    public void delete(String id) {
        iUserRepository.findById(id).orElseThrow(UserNotFoundException::new);
        iUserRepository.deleteById(id);
    }

    @Override
    public User getById(String id) {
        return iUserRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Page<UserDto> list(Pageable pageable) {
        return iUserRepository.findByRole_Name("BASIC", pageable).map(UserDto::new);
    }

    @Override
    public User update(User user) {
        User savedUser = iUserRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);

        User userMounter = User.builder()
                .id(savedUser.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .cpf(savedUser.getCpf())
                .birthday(user.getBirthday())
                .email(savedUser.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(savedUser.getRole())
                .password(savedUser.getPassword())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return iUserRepository.save(userMounter);
    }

    @Override
    public User getByEmail(String email) {
        return iUserRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
}