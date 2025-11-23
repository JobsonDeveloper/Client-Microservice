package br.com.client.micro.controller;

import br.com.client.micro.controller.dto.ClientDeletedSuccessfullyDto;
import br.com.client.micro.controller.dto.ClientCreatedSuccessfullyDto;
import br.com.client.micro.controller.dto.CreateClientDto;
import br.com.client.micro.controller.dto.DeleteClientDto;
import br.com.client.micro.domain.Client;
import br.com.client.micro.exceptions.ClientNotFoundException;
import br.com.client.micro.exceptions.ErrorDeletingClientException;
import br.com.client.micro.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@Tag(name = "Client", description = "Client operations")
public class ClientController {
    private final ClientService clientService;
    private Client newClient;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/api/client/create")
    @Transactional
    @Operation(
            summary = "Create a client",
            description = "Create a new client in the system",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client created successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientCreatedSuccessfullyDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"error\": \"Validation failed\", \"errors\": \"[...]\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Client already registered",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"CONFLICT\", \"message\": \"Client already registered!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ClientCreatedSuccessfullyDto> createUser(@Valid @RequestBody CreateClientDto clientDto) throws ClientAlreadyRegisteredException {
        String firstName = clientDto.firstName();
        String lastName = clientDto.lastName();
        Long cpf = Long.parseLong(clientDto.cpf());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthday = clientDto.birthday();
        String email = clientDto.email();
        String phone = clientDto.phone();
        String address = clientDto.address();
        LocalDateTime createdAt = LocalDateTime.now();

        Optional<Client> savedClient = clientService.getClient(cpf);

        if (savedClient.isPresent()) {
            throw new ClientAlreadyRegisteredException();
        }

        Client client = Client.builder()
                .firstName(firstName)
                .lastName(lastName)
                .cpf(cpf)
                .birthday(birthday)
                .email(email)
                .phone(phone)
                .address(address)
                .createdAt(createdAt)
                .build();
        Client newClient = clientService.createClient(client);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ClientCreatedSuccessfullyDto(
                        "Client created successfully!",
                        newClient
                )
        );
    }

    @DeleteMapping("/api/client/delete")
    @Operation(
            summary = "Delete a client",
            description = "Removes a client from the system",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client deleted successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ClientDeletedSuccessfullyDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"error\": \"Validation failed\", \"errors\": \"[...]\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "We were unable to delete the client",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"message\": \"We were unable to delete the client!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ClientDeletedSuccessfullyDto> deleteClient(@Valid @RequestBody DeleteClientDto clientDto) {
        String clientId = clientDto.id();
        Long clientCpf = Long.parseLong(clientDto.cpf());

        Optional<Client> client = clientService.getClient(clientCpf);

        if(!client.isPresent()){
            throw new ClientNotFoundException();
        }

        Boolean deleteResponse = clientService.deleteClient(clientId);

        if(!deleteResponse){
            throw new ErrorDeletingClientException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ClientDeletedSuccessfullyDto("Client deleted successfully!"));
    }
}
