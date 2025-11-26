package via.pro3.slaughterhouse.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.repository.AnimalRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AnimalRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AnimalRepository animalRepository;

    @BeforeEach
    void setUp() {
        animalRepository.deleteAll();
    }

    @Test
    void createAnimalAndGetByRegistrationNumber() throws Exception {
        String body = """
                {
                  "registrationNumber": "AN-999",
                  "weight": 600.5,
                  "arrivalDate": "2025-01-01",
                  "origin": "FarmX"
                }
                """;

        // create
        mockMvc.perform(post("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        // get by registrationNumber
        mockMvc.perform(get("/api/animals/AN-999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registrationNumber").value("AN-999"))
                .andExpect(jsonPath("$.origin").value("FarmX"));
    }

    @Test
    void getAnimalsByDate() throws Exception {
        // przygotuj dane w repo (bez REST, szybciej)
        Animal a = new Animal();
        a.setRegistrationNumber("AN-100");
        a.setWeight(500.0);
        a.setArrivalDate(java.time.LocalDate.of(2025, 1, 1));
        a.setOrigin("FarmY");
        animalRepository.save(a);

        mockMvc.perform(get("/api/animals")
                        .param("date", "2025-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].registrationNumber").value("AN-100"));
    }

    @Test
    void invalidDateFormatReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/animals")
                        .param("date", "01-01-2025"))  // zły format
                .andExpect(status().isBadRequest());
    }
}
