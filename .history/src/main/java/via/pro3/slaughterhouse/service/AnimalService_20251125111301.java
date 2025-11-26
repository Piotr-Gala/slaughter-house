package via.pro3.slaughterhouse.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.dto.animal.CreateAnimalDto;
import via.pro3.slaughterhouse.repository.AnimalRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository repo;

    public AnimalService(AnimalRepository repo) {
        this.repo = repo;
    }

    public Animal registerAnimal(CreateAnimalDto dto) {
        if (dto.registrationNumber == null || dto.registrationNumber.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registrationNumber is required");
        }

        if (repo.existsByRegistrationNumber(dto.registrationNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Animal with this registrationNumber already exists");
        }

        Animal animal = new Animal();
        animal.setRegistrationNumber(dto.registrationNumber);
        animal.setWeight(dto.weight);
        animal.setOrigin(dto.origin);

        if (dto.arrivalDate != null) {
            try {
                animal.setArrivalDate(LocalDate.parse(dto.arrivalDate));
            } catch (DateTimeParseException ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid date format for arrivalDate, expected yyyy-MM-dd"
                );
            }
        }

        return repo.save(animal);
    }

    public Optional<Animal> findByRegistrationNumber(String registrationNumber) {
        return repo.findByRegistrationNumber(registrationNumber);
    }

    public List<Animal> findByDateOrOrigin(String date, String origin) {
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
                        "Invalid date format for date. Expected yyyy-MM-dd"
                );
            }
            return repo.findByArrivalDate(arrivalDate);
        }

        if (origin != null) {
            return repo.findByOrigin(origin);
        }

        // bez filtra – wszystkie
        return repo.findAll();
    }
}
