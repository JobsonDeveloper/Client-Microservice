package br.com.client.micro.repository;

import br.com.client.micro.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClientRepository extends MongoRepository<Client, String> {
    public boolean existsByCpf(String cpf);
    public boolean existsByEmail(String email);
    Page<Client> findByRole_Name(String roleName, Pageable pageable);
    public Optional<Client> findByEmail(String email);

}
