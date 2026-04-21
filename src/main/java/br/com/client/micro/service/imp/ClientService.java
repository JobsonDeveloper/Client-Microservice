package br.com.client.micro.service.imp;

import br.com.client.micro.domain.Client;
import br.com.client.micro.domain.Role;
import br.com.client.micro.dto.response.ClientsDto;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.RoleNotFoundException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.repository.IRoleRepository;
import br.com.client.micro.service.IClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClientService implements IClientService {

    private final IClientRepository clientRepository;
    private final IRoleRepository iRoleRepository;

    public ClientService(
            IClientRepository clientRepository,
            IRoleRepository iRoleRepository
    ) {
        this.clientRepository = clientRepository;
        this.iRoleRepository = iRoleRepository;
    }

    @Override
    public Client createClient(Client client) {
        boolean clientByCpf = clientRepository.existsByCpf(client.getCpf());
        if (clientByCpf) throw new ClientAlreadyRegisteredException();

        boolean clientByEmail = clientRepository.existsByEmail(client.getEmail());
        if (clientByEmail) throw new ClientAlreadyRegisteredException();

        Role role = iRoleRepository.findByName("BASIC").orElseThrow(RoleNotFoundException::new);
        client.setRole(role);
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(String id) {
        clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
        clientRepository.deleteById(id);
    }

    @Override
    public Client getClient(String id) {
        return clientRepository.findById(id).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public Page<ClientsDto> listClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(ClientsDto::new);
    }

    @Override
    public Client updateClient(Client client) {
        Client savedClient = clientRepository.findById(client.getId()).orElseThrow(ClientNotFoundException::new);

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

        return clientRepository.save(clientMounter);
    }
}