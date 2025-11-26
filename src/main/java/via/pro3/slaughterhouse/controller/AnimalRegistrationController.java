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



    // READ: by registration number
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<AnimalDto> byRegistration(@PathVariable String registrationNumber) {
        return animalRegistrationService.findByRegistrationNumber(registrationNumber)
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
