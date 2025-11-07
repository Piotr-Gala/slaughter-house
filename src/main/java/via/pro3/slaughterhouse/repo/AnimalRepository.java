package via.pro3.slaughterhouse.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import via.pro3.slaughterhouse.domain.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Optional<Animal> findByRegistrationNumber(String registrationNumber);
    List<Animal> findAllByArrivalDate(LocalDate date);
    List<Animal> findAllByOriginIgnoreCase(String origin);
}
