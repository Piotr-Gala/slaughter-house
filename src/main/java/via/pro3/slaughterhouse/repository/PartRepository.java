package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.pro3.slaughterhouse.domain.Part;


public interface PartRepository extends JpaRepository<Part, Long> {
}
