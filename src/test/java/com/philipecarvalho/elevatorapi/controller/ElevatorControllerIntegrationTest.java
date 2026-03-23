package com.philipecarvalho.elevatorapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipecarvalho.elevatorapi.domain.Elevator;
import com.philipecarvalho.elevatorapi.dto.CreateElevatorRequest;
import com.philipecarvalho.elevatorapi.repository.ElevatorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ElevatorControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ElevatorRepository repository;

    //Limpa o repositório antes de cada teste
    @BeforeEach
    void setUp()  {
        repository.findAll().
                forEach(elevator -> repository.deleteById(elevator.getId()));
    }

    @Test
    @DisplayName("Deve criar um elevador com sucesso")
    void shouldCreateElevatorSuccessfully() throws Exception {
        CreateElevatorRequest request = new CreateElevatorRequest(1, 1);
        mockMvc.perform(post("/elevators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.maxCapacity").value(1))
                .andExpect(jsonPath("$.maxFloor").value(1));
    }

    @Test
    @DisplayName("Deve retornar erro para campo maxFloor invalido")
    void shouldReturnBadRequestWhenMaxFloorIsInvalid() throws Exception {
        String invalidRequest = """
                {
                    "maxCapacity": 1,
                    "maxFloor": -1
                }
                """;
        mockMvc.perform(post("/elevators")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        containsString("maxFloor must be greater than zero")
                ))
                .andExpect(jsonPath("$.path").value("/elevators"));
    }

    @Test
    @DisplayName("Deve retornar erro para múltiplos campos inválidos")
    void shouldReturnBadRequestToMultiplesErrors() throws Exception {
        String invalidRequest = """
                {
                    "maxCapacity": -1,
                    "maxFloor": -1
                }
                """;
        mockMvc.perform(post("/elevators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                        allOf(
                                containsString("maxCapacity must be greater than zero"),
                                containsString("maxFloor must be greater than zero")
                        )
                ))
                .andExpect(jsonPath("$.path").value("/elevators"));
    }

    @Test
    @DisplayName("Deve retornar erro para JSON mal formatado")
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        String invalidRequest = """
                {
                    "maxCapacity": xyz,
                    "maxCapacity": 1,
        """;
        mockMvc.perform(post("/elevators")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(
                                containsString("Malformed JSON request")))
                .andExpect(jsonPath("$.path").value("/elevators"));
    }

    @Test
    @DisplayName("Deve buscar um elevador com base no id")
    void shouldGetElevatorByIdSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(get("/elevators/{id}", savedElevator.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedElevator.getId()))
                .andExpect(jsonPath("$.maxCapacity").value(1))
                .andExpect(jsonPath("$.maxFloor").value(1));
    }

    @Test
    @DisplayName("Retornar erro ao tentar buscar um elevador com id que nao existe")
    void shouldReturnNotFoundWhenElevatorsIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/elevators/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Retornar uma lista de todos os elevadores salvos")
    void shouldReturnListOfElevatorsSuccessfully() throws Exception {
        Elevator elevator1  = new Elevator(1, 1);
        Elevator elevator2  = new Elevator(2, 2);
        repository.save(elevator1);
        repository.save(elevator2);
        mockMvc.perform(get("/elevators"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].maxCapacity").value(1))
                .andExpect(jsonPath("$[0].maxFloor").value(1))
                .andExpect(jsonPath("$[1].maxCapacity").value(2))
                .andExpect(jsonPath("$[1].maxFloor").value(2));
    }

    @Test
    @DisplayName("Deve excluir um elevador com base no id")
    void shouldExcludeElevatorByIdSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(delete("/elevators/{id}", savedElevator.getId()))
                .andExpect(status().isNoContent());

        //Validação
        Optional<Elevator> deleted = repository.findById(savedElevator.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @DisplayName("Deve retornar erro quando tentar deletar um elevador com id inexistente")
    void shouldReturnNotFoundWhenDeletingNonExistingElevator() throws Exception {
        mockMvc.perform(delete("/elevators/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve adicionar passageiro com sucesso")
    void shouldAddPassengerSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/add", savedElevator.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPassengers").value(1));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar adicionar passageiro com o elevador cheio")
    void shouldReturnErrorWhenAddingPassengerToFullElevator() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        elevator.addPassenger();
        Elevator savedElevator = repository.save(elevator);

        //Forcar o erro
        mockMvc.perform(patch("/elevators/{id}/add", savedElevator.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        containsString(String.valueOf("Invalid operation for elevator "
                                + savedElevator.getId() + ", because it's already full"
                        ))));
    }

    @Test
    @DisplayName("Deve remover passageiro com sucesso")
    void shouldRemovePassengerSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        elevator.addPassenger();
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/remove", savedElevator.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPassengers").value(0));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar remover passageiro com o elevador vazio")
    void shouldReturnErrorWhenRemovingPassengerFromEmptyElevator() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/remove", savedElevator.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                containsString(String.valueOf("Invalid operation for elevator "
                        + savedElevator.getId() + ", because there's no one inside"
                ))));
    }

    @Test
    @DisplayName("Deve subir um andar com sucesso")
    void shouldMoveElevatorUpSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/up", savedElevator.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentFloor").value(1));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar subir além do último andar")
    void shouldReturnErrorWhenMovingElevatorAboveMaxFloor() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        elevator.moveElevatorUp();
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/up", savedElevator.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        containsString(String.valueOf("Invalid operation for elevator "
                                + savedElevator.getId() + ", because it's already at the top floor"
                        ))));
    }

    @Test
    @DisplayName("Deve descer um andar com sucesso")
    void shouldMoveElevatorDownSuccessfully() throws Exception {
        Elevator elevator = new Elevator(1, 1);
        elevator.moveElevatorUp();
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/down", savedElevator.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentFloor").value(0));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar descer alem do térreo")
    void shouldReturnErrorWhenMovingBelowGroundFloor() throws Exception {
        Elevator elevator =  new Elevator(1, 1);
        Elevator savedElevator = repository.save(elevator);
        mockMvc.perform(patch("/elevators/{id}/down", savedElevator.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        containsString(String.valueOf("Invalid operation for elevator "
                                + savedElevator.getId() + ", because it's already at the ground floor"
                        ))));
    }
}
