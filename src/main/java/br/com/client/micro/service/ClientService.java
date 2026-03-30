package br.com.client.micro.service;

import br.com.client.micro.domain.Client;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.ErrorCreatingClientException;
import br.com.client.micro.exceptions.ErrorDeletingClientException;
import br.com.client.micro.repository.IClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ClientService implements IClientService {

    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(Client client) {
        Optional<Client> clientByCpf = clientRepository.findByCpf(client.getCpf());
        Optional<Client> clientByEmail = clientRepository.findByEmail(client.getEmail());

        if (clientByCpf.isPresent() || clientByEmail.isPresent()) {
            throw new ClientAlreadyRegisteredException();
        }

        Client newClient = clientRepository.save(client);

        if (newClient.getId().isBlank()) {
            throw new ErrorCreatingClientException();
        }

        return newClient;
    }

    @Override
    public void deleteClient(String id) {
        Optional<Client> registeredClient = clientRepository.findById(id);

        if (!registeredClient.isPresent()) {
            throw new ClientNotFoundException();
        }

        clientRepository.deleteById(id);
        Optional<Client> client = clientRepository.findById(id);

        if (client.isPresent()) {
            throw new ErrorDeletingClientException();
        }
    }

    @Override
    public Client getClient(String id) {
        Optional<Client> client = clientRepository.findById(id);

        if (!client.isPresent()) {
            throw new ClientNotFoundException();
        }

        return client.get();
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
        Optional<Client> savedClient = clientRepository.findById(client.getId());

        if (!savedClient.isPresent()) {
            throw new ClientNotFoundException();
        }

        Client clientMounter = Client.builder()
                .id(savedClient.get().getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .cpf(savedClient.get().getCpf())
                .birthday(client.getBirthday())
                .email(savedClient.get().getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .password(savedClient.get().getPassword())
                .createdAt(savedClient.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        Client modifiedClient = clientRepository.save(clientMounter);

        if (modifiedClient.getId().isBlank()) {
            throw new ErrorCreatingClientException();
        }

        return modifiedClient;
    }
}
