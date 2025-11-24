package via.pro3.slaughterhouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import via.pro3.slaughterhouse.domain.Product;

import java.util.List;
import java.util.UUID;

public interface TraceRepository extends JpaRepository<Product, UUID> {

    // 1) rejestracje zwierząt użytych w produkcie
    @Query("""
      select distinct a.registrationNumber
      from Product p
      join p.parts prt
      join prt.animal a
      where p.id = :productId
      """)
    List<String> findAnimalRegistrationNumbersByProductId(UUID productId);

    // 2) produkty dla zwierzęcia (po registrationNumber)
    @Query("""
      select distinct p.id
      from Product p
      join p.parts prt
      join prt.animal a
      where a.registrationNumber = :reg
      """)
    List<UUID> findProductIdsByAnimalRegistrationNumber(String reg);
}
