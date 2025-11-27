package br.com.client.micro.controller;

import br.com.client.micro.controller.dto.*;
import br.com.client.micro.domain.Client;
import br.com.client.micro.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import br.com.client.micro.exceptions.ClientAlreadyRegisteredException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "It was not possible to create the client",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"INTERNAL_SERVER_ERROR\", \"message\": \"It was not possible to create the client!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ClientCreatedSuccessfullyDto> createClient(@Valid @RequestBody CreateClientDto clientDto) throws ClientAlreadyRegisteredException {
        String firstName = clientDto.firstName();
        String lastName = clientDto.lastName();
        Long cpf = Long.parseLong(clientDto.cpf());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthday = clientDto.birthday();
        String email = clientDto.email();
        String phone = clientDto.phone();
        String address = clientDto.address();
        LocalDateTime createdAt = LocalDateTime.now();

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
                            responseCode = "404",
                            description = "Client not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"NOT_FOUND\", \"message\": \"Client not found!\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "We were unable to delete the client",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"INTERNAL_SERVER_ERROR\", \"message\": \"We were unable to delete the client!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ClientDeletedSuccessfullyDto> deleteClient(@Valid @RequestBody DeleteClientDto clientDto) {
        String clientId = clientDto.id();

        clientService.deleteClient(clientId);

        return ResponseEntity.status(HttpStatus.OK).body(new ClientDeletedSuccessfullyDto("Client deleted successfully!"));
    }

    @PutMapping("/api/client/update")
    @Operation(
            summary = "Update a user",
            description = "Update the data from a user of system",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client data modified successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ModifiedClientDataDto.class)
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
                            responseCode = "404",
                            description = "Client not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"NOT_FOUND\", \"message\": \"Client not found!\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "It was not possible to modify the client data!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"INTERNAL_SERVER_ERROR\", \"message\": \"It was not possible to modify the client data!\" }"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<ModifiedClientDataDto> changeClient(@Valid @RequestBody ChangeClientDto clientDto) {
        String id = clientDto.id();
        String firstName = clientDto.firstName();
        String lastName = clientDto.lastName();
        LocalDate birthday = clientDto.birthday();
        String email = clientDto.email();
        String phone = clientDto.phone();
        String address = clientDto.address();

        Client currentClient = Client.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .email(email)
                .phone(phone)
                .address(address)
                .build();

        Client client = clientService.updateClient(currentClient);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ModifiedClientDataDto("Client data modified successfully!", client));
    }

    @Operation(
            summary = "Get client information",
            description = "Return all information of a client",
            tags = {"Client"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Client information returned successfully!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReturnClientInformationDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Client not found!",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{ \"status\": \"NOT_FOUND\", \"message\": \"Client not found!\" }"
                                    )
                            )
                    )
            }
    )
    @GetMapping("/api/client/{id}/info")
    public ResponseEntity<ReturnClientInformationDto> getClientInfo(
            @Parameter(description = "Client id", required = true)
            @PathVariable String id
    ) {
        Client client = clientService.getClient(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ReturnClientInformationDto("Client information returned successfully!", client));
    }
}
