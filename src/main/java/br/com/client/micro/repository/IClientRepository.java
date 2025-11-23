package br.com.client.micro.repository;

import br.com.client.micro.domain.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClientRepository extends MongoRepository<Client, String> {
    Optional<Client> findByCpf(Long cpf);
}
