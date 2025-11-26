package via.pro3.slaughterhouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.dto.animal.*;
import via.pro3.slaughterhouse.service.*;

import java.util.List;

/** Part 3: Animal Registration API
 * REST controller for managing animal registrations in the slaughterhouse system.
    Allows:
    - Creating new animal registrations
    - Retrieving animal details by registration number
    - Querying animals by arrival date or origin
 */
@RestController
@RequestMapping("/api/animals")

public class AnimalRegistrationController {

    private final AnimalRegistrationService animalRegistrationService;

    public AnimalRegistrationController(AnimalRegistrationService animalRegistrationService) {
        this.animalRegistrationService = animalRegistrationService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto registerAnimal(@RequestBody CreateAnimalDto dto) {
        Animal saved = animalRegistrationService.registerAnimal(dto);
        return AnimalDto.from(saved);
    }



    // READ: by id
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> byId(@PathVariable Long id) {
        return animalRegistrationService.findById(id)
                .map(AnimalDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<AnimalDto> getByDateOrOrigin(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String origin
    ) {
        List<Animal> animals = animalRegistrationService.findByDateOrOrigin(date, origin);
        return animals.stream()
                .map(AnimalDto::from)
                .toList();
    }

}
