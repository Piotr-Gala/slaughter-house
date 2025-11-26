package via.pro3.slaughterhouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.dtos.animal.AnimalDto;
import via.pro3.slaughterhouse.dtos.animal.CreateAnimalDto;
import via.pro3.slaughterhouse.repository.AnimalRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

public class AnimalController {

    private final AnimalRepository repo;

    public AnimalController(AnimalRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Animal registerAnimal(@RequestBody Animal animal) {
        if (animal.getRegistrationNumber() == null || animal.getRegistrationNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registrationNumber is required");
        }

        if (repo.existsByRegistrationNumber(animal.getRegistrationNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Animal with this registrationNumber already exists");
        }

        return repo.save(animal);
    }


    // READ: by registration number
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<Animal> byRegistration(@PathVariable String registrationNumber) {
        return repo.findByRegistrationNumber(registrationNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Animal> getByDateOrOrigin(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String origin
    ) {
        if (date != null && origin != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provide either date or origin, not both"
            );
        }

        if (date != null) {
            final LocalDate arrivalDate;
            try {
                arrivalDate = LocalDate.parse(date);
            } catch (DateTimeParseException ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid date format, expected YYYY-MM-DD"
                );
            }
            return repo.findByArrivalDate(arrivalDate);
        }

        if (origin != null) {
            return repo.findByOrigin(origin);
        }

        return repo.findAll();
    }

    // READ: by date OR origin (jeden parametr naraz)
//    @GetMapping
//    public ResponseEntity<List<AnimalDto>> query(@RequestParam(required = false) String date,
//                                                 @RequestParam(required = false) String origin) {
//        if ((date == null && origin == null) || (date != null && origin != null)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "provide either date or origin");
//        }
//        if (date != null) {
//            LocalDate d = LocalDate.parse(date);
//            return ResponseEntity.ok(repo.findAllByArrivalDate(d).stream().map(AnimalDto::from).toList());
//        } else {
//            return ResponseEntity.ok(repo.findAllByOriginIgnoreCase(origin).stream().map(AnimalDto::from).toList());
//        }
//    }


    


}
