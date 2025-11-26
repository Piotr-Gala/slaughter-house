package via.pro3.slaughterhouse.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Tray;
import via.pro3.slaughterhouse.repository.AnimalRepository;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.TrayRepository;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ButcheringServiceTest {

    @Autowired
    PartRepository partRepo;

    @Autowired
    TrayRepository trayRepo;

    @Autowired
    AnimalRepository animalRepo;

    ButcheringService service;

    private void init() {
        service = new ButcheringService(partRepo, trayRepo);
    }

    @Test
    void putPartOnTray_happyPath() {
        init();

        Animal a = new Animal();
        a.setWeight(500);
        a = animalRepo.save(a);

        Part p = new Part();
        p.setType("leg");
        p.setWeight(10);
        p.setAnimal(a);
        p = partRepo.save(p);

        Tray t = new Tray();
        t.setType("leg");
        t.setMaxWeight(50);
        t = trayRepo.save(t);

        Part result = service.putPartOnTray(p.getId(), t.getId());

        assertThat(result.getTray().getId()).isEqualTo(t.getId());
    }

    @Test
    void putPartOnTray_wrongType_throws() {
        init();

        Animal a = new Animal();
        a.setWeight(400);
        a = animalRepo.save(a);

        Part p = new Part();
        p.setType("rib");
        p.setWeight(5);
        p.setAnimal(a);

        Part savedP = partRepo.save(p);

        Tray t = new Tray();
        t.setType("shoulder");
        t.setMaxWeight(100);

        Tray savedT = trayRepo.save(t);

        assertThatThrownBy(() -> service.putPartOnTray(savedP.getId(), savedT.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void putPartOnTray_exceedsWeight_throws() {
        init();

        Animal a = animalRepo.save(new Animal());

        Part p = new Part();
        p.setType("rib");
        p.setWeight(40);
        p.setAnimal(a);

        Part savedP = partRepo.save(p);

        Tray t = new Tray();
        t.setType("rib");
        t.setMaxWeight(30); // less than part

        Tray savedT = trayRepo.save(t);

        assertThatThrownBy(() -> service.putPartOnTray(savedP.getId(), savedT.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
