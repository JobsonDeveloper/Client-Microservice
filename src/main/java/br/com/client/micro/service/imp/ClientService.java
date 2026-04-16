package br.com.client.micro.service.imp;

import br.com.client.micro.domain.Client;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.repository.IClientRepository;
import br.com.client.micro.service.IClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ClientService implements IClientService {

    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(Client client) {
        clientRepository.findByCpf(client.getCpf()).orElseThrow(ClientAlreadyRegisteredException::new);
        clientRepository.findByEmail(client.getEmail()).orElseThrow(ClientAlreadyRegisteredException::new);

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
    public Page<Client> listClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(client -> new Client(
                        client.getId(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getCpf(),
                        client.getBirthday(),
                        client.getEmail(),
                        client.getPhone(),
                        client.getAddress(),
                        client.getPassword(),
                        client.getCreatedAt(),
                        client.getUpdatedAt()
                ));
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
                .password(savedClient.getPassword())
                .createdAt(savedClient.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return clientRepository.save(clientMounter);
    }
}
