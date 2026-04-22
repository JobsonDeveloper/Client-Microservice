package br.com.user.micro.service;

import br.com.user.micro.domain.User;
import br.com.user.micro.dto.response.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    public User create(User user);
    public void delete(String id);
    public User getById(String id);
    public Page<UserDto> list(Pageable pageable);
    public User update(User user);
    public User getByEmail(String email);
}
