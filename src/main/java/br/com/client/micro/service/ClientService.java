package br.com.client.micro.service;

import br.com.client.micro.domain.Client;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.ErrorCreatingClientException;
import br.com.client.micro.exceptions.ErrorDeletingClientException;
import br.com.client.micro.repository.IClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService {

    private final IClientRepository clientRepository;

    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client createClient(Client client) {
        Optional<Client> savedClient = clientRepository.findByCpf(client.getCpf());

        if (savedClient.isPresent()) {
            throw new ClientAlreadyRegisteredException();
        }

        Client newClient = clientRepository.save(client);

        if(newClient.getId().isBlank()) {
            throw new ErrorCreatingClientException();
        }

        return newClient;
    }

    @Override
    public void deleteClient(String id) {
        Optional<Client> registeredClient = clientRepository.findById(id);

        if(!registeredClient.isPresent()){
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

        if(!client.isPresent()) {
            throw new ClientNotFoundException();
        }

        return client.get();
    }

    @Override
    public List<Client> listClients() {
        return List.of();
    }

    @Override
    public Client updateClient(Client client) {
        Optional<Client> savedClient = clientRepository.findById(client.getId());

        if (!savedClient.isPresent()) {
            throw new ClientNotFoundException();
        }

        Client clientMounter = savedClient.get();
        clientMounter.setFirstName(client.getFirstName());
        clientMounter.setLastName(client.getLastName());
        clientMounter.setBirthday(client.getBirthday());
        clientMounter.setEmail(client.getEmail());
        clientMounter.setPhone(client.getPhone());
        clientMounter.setAddress(client.getAddress());

        Client modifiedClient = clientRepository.save(clientMounter);

        if(modifiedClient.getId().isBlank()) {
            throw new ErrorCreatingClientException();
        }

        return modifiedClient;
    }
}
