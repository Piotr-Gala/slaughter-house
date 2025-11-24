package via.pro3.slaughterhouse.grpc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;
import via.pro3.slaughterhouse.repository.AnimalRepository;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.ProductRepository;
import via.pro3.slaughterhouse.repository.TraceRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static via.pro3.slaughterhouse.domain.ProductKind.HALF_ANIMAL;

@Disabled("Disabled for simplicity – rest of the system is tested elsewhere")
@SpringBootTest
@ActiveProfiles("test")
class TraceRepositoryTest {

    @Autowired
    TraceRepository traceRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    PartRepository partRepository;

    @Autowired
    ProductRepository productRepository;

    private Product product;
    private Animal animal1;
    private Animal animal2;

    @BeforeEach
    void setUp() {
        // czyścimy wszystko w poprawnej kolejności
        productRepository.deleteAll();
        partRepository.deleteAll();
        animalRepository.deleteAll();

        // tworzymy zwierzaki
        animal1 = new Animal();
        animal1.setRegistrationNumber("AN-001");
        animal1.setWeight(600);
        animal1 = animalRepository.save(animal1);

        animal2 = new Animal();
        animal2.setRegistrationNumber("AN-002");
        animal2.setWeight(580);
        animal2 = animalRepository.save(animal2);

        // części
        Part p1 = new Part();
        p1.setType("leg");
        p1.setWeight(10);
        p1.setAnimal(animal1);
        p1 = partRepository.save(p1);

        Part p2 = new Part();
        p2.setType("rib");
        p2.setWeight(8);
        p2.setAnimal(animal2);
        p2 = partRepository.save(p2);

        // produkt z obu części
        product = new Product();
        product.setKind(HALF_ANIMAL); // albo enum jak już przerobiłeś
        product.getParts().add(p1);
        product.getParts().add(p2);
        product = productRepository.save(product);
    }

    @Test
    void animalsByProduct_ok() {
        List<String> regs = traceRepository.findAnimalRegistrationNumbersByProductId(product.getId());
        assertThat(regs)
                .containsExactlyInAnyOrder("AN-001", "AN-002");
    }

    @Test
    void productsByAnimal_ok() {
        List<Long> productIds = traceRepository.findProductIdsByAnimalRegistrationNumber("AN-001");
        assertThat(productIds)
                .contains(product.getId());
    }
}
