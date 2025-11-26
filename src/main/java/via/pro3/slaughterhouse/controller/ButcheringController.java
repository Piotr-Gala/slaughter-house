package via.pro3.slaughterhouse.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import via.pro3.slaughterhouse.repository.AnimalRepository;
import via.pro3.slaughterhouse.service.ButcheringService;
import via.pro3.slaughterhouse.dto.butchering.CreateTrayDto;
import via.pro3.slaughterhouse.dto.butchering.CreatePartDto;
import via.pro3.slaughterhouse.dto.butchering.TrayDto;
import via.pro3.slaughterhouse.dto.butchering.PartDto;
import via.pro3.slaughterhouse.domain.Tray;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.TrayRepository;

import java.util.List;

@RestController
@RequestMapping("/api/butchering")
public class ButcheringController {

    private final TrayRepository trayRepository;
    private final PartRepository partRepository;
    private final ButcheringService butcheringService;
    private final AnimalRepository animalRepository;

    public ButcheringController(TrayRepository t, PartRepository p, ButcheringService b, AnimalRepository a) {
        this.trayRepository = t; this.partRepository = p; this.butcheringService = b; this.animalRepository = a;
    }

    // CREATE TRAY
    @PostMapping("/trays")
    @ResponseStatus(HttpStatus.CREATED)
    public TrayDto createTray(@RequestBody CreateTrayDto dto) {
        Tray tray = new Tray();
        tray.setType(dto.type);
        tray.setMaxWeight(dto.maxWeight);
        Tray saved = trayRepository.save(tray);
        return TrayDto.from(saved);
    }


    // CREATE PART (bez tacy)
    @PostMapping("/parts")
    @ResponseStatus(HttpStatus.CREATED)
    public PartDto createPart(@RequestBody CreatePartDto dto) {
        Part part = new Part();
        part.setWeight(dto.weight);
        part.setType(dto.type);

        if (dto.animalId != null) {
            var animal = animalRepository.findById(dto.animalId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Animal not found"));
            part.setAnimal(animal);
        }

        Part saved = partRepository.save(part);
        return PartDto.from(saved);
    }


    // PUT PART ON TRAY (walidacje w serwisie)
    @PostMapping("/parts/{partId}/put-on-tray/{trayId}")
    public PartDto putPartOnTray(@PathVariable Long partId, @PathVariable Long trayId) {
        Part part = butcheringService.putPartOnTray(partId, trayId);
        return PartDto.from(part);
    }


    // pomocnicze listy do kompletowania paczek
    @GetMapping("/parts/by-type/{type}")
    public List<PartDto> getPartsByType(@PathVariable String type) {
        return partRepository.findByTypeIgnoreCase(type).stream()
                .map(PartDto::from)
                .toList();
    }

}