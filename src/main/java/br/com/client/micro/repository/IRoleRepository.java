package br.com.client.micro.repository;

import br.com.client.micro.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends MongoRepository<Role, String> {
    public Optional<Role> findByName(String name);
}
