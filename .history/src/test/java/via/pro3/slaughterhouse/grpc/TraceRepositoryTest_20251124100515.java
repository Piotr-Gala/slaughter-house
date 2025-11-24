package via.pro3.slaughterhouse.grpc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import via.pro3.slaughterhouse.repository.TraceRepository;


import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DataJpaTest
class TraceRepositoryTest {

    @Autowired private TraceRepository repo;

    @Test
    void animalsByProduct_ok() {
        var regs = repo.findAnimalRegistrationNumbersByProductId(Long.valueOf(1));
        assertThat(regs).containsExactlyInAnyOrder("AN-001","AN-002");
    }

    @Test
    void productsByAnimal_ok() {
        var ids = repo.findProductIdsByAnimalRegistrationNumber("AN-001");
        assertThat(ids).contains(Long.valueOf(1));
    }
}