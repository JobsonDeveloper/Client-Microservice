package br.com.user.micro.service;

import br.com.user.micro.domain.Session;
import jakarta.servlet.http.HttpServletRequest;

public interface ISessionService {
    public Session startSession(String userId, HttpServletRequest request);
    public void endSession(String sessionId);
}
