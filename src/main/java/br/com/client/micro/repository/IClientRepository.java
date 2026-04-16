package br.com.client.micro.repository;

import br.com.client.micro.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IClientRepository extends MongoRepository<Client, String> {
    public boolean existsByCpf(String cpf);
    public boolean existsByEmail(String email);
}
