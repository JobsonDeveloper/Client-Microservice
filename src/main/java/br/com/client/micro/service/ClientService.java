package br.com.client.micro.service;

import br.com.client.micro.domain.Client;
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
        Client newClient = clientRepository.save(client);
        return newClient;
    }

    @Override
    public Boolean deleteClient(Long cpf) {
        return null;
    }

    @Override
    public Optional<Client> getClient(Long cpf) {
        Optional<Client> client = clientRepository.findByCpf(cpf);
        return client;
    }

    @Override
    public List<Client> listClients() {
        return List.of();
    }

    @Override
    public Client updateClient(Client client) {
        return null;
    }
}
