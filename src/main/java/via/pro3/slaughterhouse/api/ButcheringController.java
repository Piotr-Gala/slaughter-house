package via.pro3.slaughterhouse.api;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import via.pro3.slaughterhouse.application.ButcheringService;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Tray;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.TrayRepository;

import java.util.List;

@RestController
@RequestMapping("/api/butchering")
public class ButcheringController {
    private final TrayRepository trayRepo;
    private final PartRepository partRepo;
    private final ButcheringService butchering;

    public ButcheringController(TrayRepository t, PartRepository p, ButcheringService b) {
        this.trayRepo = t; this.partRepo = p; this.butchering = b;
    }

    // CREATE TRAY
    @PostMapping("/trays")
    public ResponseEntity<Tray> createTray(@RequestBody Tray t) { return ResponseEntity.ok(trayRepo.save(t)); }

    // CREATE PART (bez tacy)
    @PostMapping("/parts")
    public ResponseEntity<Part> createPart(@RequestBody Part p) { return ResponseEntity.ok(partRepo.save(p)); }

    // PUT PART ON TRAY (walidacje w serwisie)
    @PostMapping("/parts/{partId}/put-on-tray/{trayId}")
    public ResponseEntity<Part> putOnTray(@PathVariable Long partId, @PathVariable Long trayId) {
        return ResponseEntity.ok(butchering.putPartOnTray(partId, trayId));
    }

    // pomocnicze listy do kompletowania paczek
    @GetMapping("/parts/by-type/{type}")
    public List<Part> partsByType(@PathVariable String type) { return partRepo.findAll().stream()
            .filter(p -> p.getType().equalsIgnoreCase(type)).toList(); }
}