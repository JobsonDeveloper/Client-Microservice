package br.com.client.micro.service.imp;

import br.com.client.micro.domain.Role;
import br.com.client.micro.exceptions.RoleNotFoundException;
import br.com.client.micro.repository.IRoleRepository;
import br.com.client.micro.service.IRoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {
    private final IRoleRepository iRoleRepository;

    public RoleService(IRoleRepository iRoleRepository) {
        this.iRoleRepository = iRoleRepository;
    }

    @Override
    public Role findByName(String roleName) {
        return iRoleRepository.findByName(roleName).orElseThrow(RoleNotFoundException::new);
    }
}
