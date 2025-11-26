package via.pro3.slaughterhouse.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.repository.AnimalRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnimalRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AnimalRepository animalRepository;

    @BeforeEach
    void setUp() {
        animalRepository.deleteAll(); // czyścimy bo teraz polegamy na ID
    }

    @Test
    void createAnimalAndGetById() throws Exception {
        String body = """
                {
                  "weight": 600.5,
                  "arrivalDate": "2025-01-01",
                  "origin": "FarmX"
                }
                """;

        // CREATE
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        // Pobierz ID z repo
        Long id = animalRepository.findAll().get(0).getId();

        // GET by id
        mockMvc.perform(get("/api/animals/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.weight").value(600.5))
                .andExpect(jsonPath("$.origin").value("FarmX"))
                .andExpect(jsonPath("$.arrivalDate").value("2025-01-01"));
    }

    @Test
    void getAnimalsByDate() throws Exception {
        Animal a = new Animal();
        a.setWeight(500.0);
        a.setArrivalDate(java.time.LocalDate.of(2025, 1, 1));
        a.setOrigin("FarmY");
        animalRepository.save(a);

        mockMvc.perform(get("/api/animals")
                        .param("date", "2025-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].origin").value("FarmY"))
                .andExpect(jsonPath("$[0].arrivalDate").value("2025-01-01"));
    }

    @Test
    void invalidDateFormatReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/animals")
                        .param("date", "01-01-2025"))
                .andExpect(status().isBadRequest());
    }
}
