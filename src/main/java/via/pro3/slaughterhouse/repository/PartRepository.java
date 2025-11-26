package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.pro3.slaughterhouse.domain.Part;

import java.util.List;


public interface PartRepository extends JpaRepository<Part, Long> {
    List<Part> findByTypeIgnoreCase(String type);

}
