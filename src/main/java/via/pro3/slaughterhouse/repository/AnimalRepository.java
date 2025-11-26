package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import via.pro3.slaughterhouse.domain.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findById(Long Id);
    List<Animal> findByArrivalDate(LocalDate arrivalDate);
    List<Animal> findByOrigin(String origin);
    boolean existsById(Long Id);
}
