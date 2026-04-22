package br.com.client.micro.service.imp;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Role;
import br.com.client.micro.domain.Session;
import br.com.client.micro.dto.response.ClientAuthDto;
import br.com.client.micro.dto.response.ClientDto;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.PermissionDeniedException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.repository.IRoleRepository;
import br.com.client.micro.service.IClientService;
import br.com.client.micro.service.ISessionService;
import br.com.client.micro.util.TokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService implements IClientService {

    private final IClientRepository iClientRepository;
    private final ISessionService iSessionService;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientService(
            IClientRepository iClientRepository,
            IRoleRepository iRoleRepository,
            ISessionService iSessionService,
            TokenGenerator tokenGenerator,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.iClientRepository = iClientRepository;
        this.iSessionService = iSessionService;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client createClient(Client client) {
        boolean clientByCpf = iClientRepository.existsByCpf(client.getCpf());
        if (clientByCpf) throw new ClientAlreadyRegisteredException();

        boolean clientByEmail = iClientRepository.existsByEmail(client.getEmail());
        if (clientByEmail) throw new ClientAlreadyRegisteredException();

        return iClientRepository.save(client);
    }

    @Override
    public void deleteClient(String id) {
        iClientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        iClientRepository.deleteById(id);
    }

    @Override
    public Client getClient(String id) {
        return iClientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public Page<ClientDto> listClients(Pageable pageable) {
        return iClientRepository.findByRole_Name("BASIC", pageable).map(ClientDto::new);
    }

    @Override
    public Client updateClient(Client client) {
        Client savedClient = iClientRepository.findById(client.getId()).orElseThrow(ClientNotFoundException::new);

        Client clientMounter = Client.builder()
                .id(savedClient.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .cpf(savedClient.getCpf())
                .birthday(client.getBirthday())
                .email(savedClient.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .role(savedClient.getRole())
                .password(savedClient.getPassword())
                .createdAt(savedClient.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return iClientRepository.save(clientMounter);
    }

    @Override
    public Client getClientByEmail(String email) {
        return iClientRepository.findByEmail(email).orElseThrow(ClientNotFoundException::new);
    }
}