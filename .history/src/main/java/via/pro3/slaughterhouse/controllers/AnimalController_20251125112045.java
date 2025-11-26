package via.pro3.slaughterhouse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.dto.animal.*;
import via.pro3.slaughterhouse.service.*;
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

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnimalDto registerAnimal(@RequestBody CreateAnimalDto dto) {
        Animal saved = animalService.registerAnimal(dto);
        return AnimalDto.from(saved);
    }



    // READ: by registration number
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<AnimalDto> byRegistration(@PathVariable String registrationNumber) {
        return animalService.findByRegistrationNumber(registrationNumber)
                .map(AnimalDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping
    public List<AnimalDto> getByDateOrOrigin(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String origin
    ) {
        List<Animal> animals = animalService.findByDateOrOrigin(date, origin);
        return animals.stream()
                .map(AnimalDto::from)
                .toList();
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
