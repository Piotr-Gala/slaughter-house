package via.pro3.slaughterhouse.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Tray;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.TrayRepository;

import java.util.UUID;

@Service
public class ButcheringService {

    private final PartRepository partRepo;
    private final TrayRepository trayRepo;

    public ButcheringService(PartRepository partRepo, TrayRepository trayRepo) {
        this.partRepo = partRepo;
        this.trayRepo = trayRepo;
    }

    @Transactional
    public Part putPartOnTray(UUID partId, UUID trayId) {
        Part part = partRepo.findById(partId).orElseThrow();
        Tray tray = trayRepo.findById(trayId).orElseThrow();

        // 1) typ części musi pasować do typu tacy
        if (!part.getType().equalsIgnoreCase(tray.getType())) {
            throw new IllegalArgumentException(
                    "Tray type mismatch: part=" + part.getType() + ", tray=" + tray.getType());
        }

        // 2) waga na tacy nie może przekroczyć max_weight
        double current = tray.getParts().stream().mapToDouble(Part::getWeight).sum();
        if (current + part.getWeight() > tray.getMaxWeight()) {
            throw new IllegalStateException(
                    "Max tray weight exceeded: current=" + current + ", part=" + part.getWeight());
        }

        part.setTray(tray);
        return partRepo.save(part);
    }
}
