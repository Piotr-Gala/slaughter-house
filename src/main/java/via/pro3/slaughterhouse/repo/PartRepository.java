package via.pro3.slaughterhouse.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import via.pro3.slaughterhouse.domain.Part;

import java.util.UUID;

public interface PartRepository extends JpaRepository<Part, UUID> {
}
