package br.com.user.micro.repository;

import br.com.user.micro.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends MongoRepository<User, String> {
    public boolean existsByCpf(String cpf);
    public boolean existsByEmail(String email);
    Page<User> findByRole_Name(String roleName, Pageable pageable);
    public Optional<User> findByEmail(String email);
}
