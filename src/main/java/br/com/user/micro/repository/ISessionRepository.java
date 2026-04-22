package br.com.user.micro.repository;

import br.com.user.micro.domain.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISessionRepository extends MongoRepository<Session, String> {
}
