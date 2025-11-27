package br.com.client.micro.service;

import br.com.client.micro.domain.Client;
import br.com.client.micro.repository.IClientRepository;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    public Client createClient(Client client);
    public void deleteClient(String id);
    public Client getClient(String id);
    public List<Client> listClients();
    public Client updateClient(Client client);
}
