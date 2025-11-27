package br.com.client.micro.service;

import br.com.client.micro.controller.dto.ReturnAllClientsDto;
import br.com.client.micro.domain.Client;
import br.com.client.micro.repository.IClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    public Client createClient(Client client);
    public void deleteClient(String id);
    public Client getClient(String id);
    public Page<ReturnAllClientsDto> listClients(Pageable pageable);
    public Client updateClient(Client client);
}
