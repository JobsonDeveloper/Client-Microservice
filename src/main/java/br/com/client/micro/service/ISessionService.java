package br.com.client.micro.service;

import br.com.client.micro.domain.Session;
import jakarta.servlet.http.HttpServletRequest;

public interface ISessionService {
    public Session startSession(String userId, HttpServletRequest request);
    public void endSession(String sessionId);
}
