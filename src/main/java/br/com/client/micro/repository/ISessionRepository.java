package br.com.client.micro.repository;

import br.com.client.micro.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISessionRepository extends MongoRepository<Session, String> {
}
