package via.pro3.slaughterhouse.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.repo.AnimalRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalRegistrationController {

    private final AnimalRepository repo;

    public AnimalRegistrationController(AnimalRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AnimalDto> register(@RequestBody CreateAnimalDto body) {
        if (body == null || body.registrationNumber == null || body.registrationNumber.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registrationNumber required");
        }
        repo.findByRegistrationNumber(body.registrationNumber)
                .ifPresent(a -> { throw new ResponseStatusException(HttpStatus.CONFLICT, "registrationNumber exists"); });

        Animal a = new Animal();
        a.setRegistrationNumber(body.registrationNumber);
        a.setWeight(body.weight);
        a.setOrigin(body.origin);
        a.setArrivalDate(body.arrivalDate == null || body.arrivalDate.isBlank()
                ? null
                : LocalDate.parse(body.arrivalDate)); // oczekuje "yyyy-MM-dd"

        a = repo.save(a);
        return ResponseEntity.ok(AnimalDto.from(a));
    }

    // READ: by registration number
    @GetMapping("/{registrationNumber}")
    public ResponseEntity<AnimalDto> byRegistration(@PathVariable String registrationNumber) {
        return repo.findByRegistrationNumber(registrationNumber)
                .map(AnimalDto::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ: by date OR origin (jeden parametr naraz)
    @GetMapping
    public ResponseEntity<List<AnimalDto>> query(@RequestParam(required = false) String date,
                                                 @RequestParam(required = false) String origin) {
        if ((date == null && origin == null) || (date != null && origin != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "provide either date or origin");
        }
        if (date != null) {
            LocalDate d = LocalDate.parse(date);
            return ResponseEntity.ok(repo.findAllByArrivalDate(d).stream().map(AnimalDto::from).toList());
        } else {
            return ResponseEntity.ok(repo.findAllByOriginIgnoreCase(origin).stream().map(AnimalDto::from).toList());
        }
    }

    // PROSTE DTO (w stylu demo: public fields, bez @Valid)
    public static class CreateAnimalDto {
        public String registrationNumber;
        public double weight;
        public String arrivalDate; // ISO "yyyy-MM-dd"
        public String origin;
    }

    public static class AnimalDto {
        public String id;
        public String registrationNumber;
        public double weight;
        public String arrivalDate;
        public String origin;

        static AnimalDto from(Animal a) {
            AnimalDto dto = new AnimalDto();
            dto.id = a.getId().toString();
            dto.registrationNumber = a.getRegistrationNumber();
            dto.weight = a.getWeight();
            dto.arrivalDate = a.getArrivalDate() == null ? null : a.getArrivalDate().toString();
            dto.origin = a.getOrigin();
            return dto;
        }
    }
}
