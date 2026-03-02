package br.com.client.micro.service;

import br.com.client.micro.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IClientService {
    public Client createClient(Client client);
    public void deleteClient(String id);
    public Client getClient(String id);
    public Page<Client> listClients(Pageable pageable);
    public Client updateClient(Client client);
}
