package br.com.user.micro.repository;

import br.com.user.micro.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends MongoRepository<Role, String> {
    public Optional<Role> findByName(String name);
    public Optional<Role> findById(Integer id);
}
