package br.com.user.micro.service.imp;

import br.com.user.micro.domain.Role;
import br.com.user.micro.exceptions.RoleNotFoundException;
import br.com.user.micro.repository.IRoleRepository;
import br.com.user.micro.service.IRoleService;
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
