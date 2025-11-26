package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import via.pro3.slaughterhouse.domain.Product;

import java.util.List;

public interface TraceRepository extends JpaRepository<Product, Long> {

    // 1) rejestracje zwierząt użytych w produkcie
    @Query("""
      select distinct a.id
      from Product p
      join p.parts prt
      join prt.animal a
      where p.id = :productId
      """)
    List<String> findAnimalIdByProductId(Long productId);

    // 2) produkty dla zwierzęcia (po id)
    @Query("""
      select distinct p.id
      from Product p
      join p.parts prt
      join prt.animal a
      where a.id = :animalId
      """)
    List<Long> findProductIdsByAnimalId(String animalId);

}
