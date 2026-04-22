package br.com.client.micro.service;

import br.com.client.micro.domain.Role;

public interface IRoleService {
    public Role findByName(String roleName);
}
