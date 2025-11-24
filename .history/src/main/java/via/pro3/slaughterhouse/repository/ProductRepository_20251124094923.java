package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import via.pro3.slaughterhouse.domain.*;

    
public interface ProductRepository extends JpaRepository<Product, Long> {

}
