package br.com.client.micro.service.imp;

import br.com.client.micro.exceptions.SessionNotFoundException;
import br.com.client.micro.repository.ISessionRepository;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;
import br.com.client.micro.domain.Session;
import br.com.client.micro.service.ISessionService;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

@Service
public class SessionService implements ISessionService {

    private final ISessionRepository iSessionRepository;

    Parser parser = new Parser();

    public SessionService(ISessionRepository iSessionRepository) {
        this.iSessionRepository = iSessionRepository;
    }

    @Override
    public Session startSession(String userId, HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        String ipAddress = request.getHeader("X-Forwarded-For");
        Client device = parser.parse(agent);

        Session session = Session.builder()
                .userId(userId)
                .ipAddress(ipAddress)
                .userAgent(agent)
                .userDevice(device.os.family)
                .loginOn(LocalDateTime.now())
                .build();

        return iSessionRepository.save(session);
    }

    @Override
    public void endSession(String sessionId) {
        Session session = iSessionRepository.findById(sessionId).orElseThrow(SessionNotFoundException::new);
        session.setLogoutOn(LocalDateTime.now());
        iSessionRepository.save(session);
    }
}
