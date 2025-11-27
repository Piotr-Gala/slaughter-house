package via.pro3.slaughterhouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import via.pro3.slaughterhouse.domain.Animal;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;
import via.pro3.slaughterhouse.domain.Tray;
import via.pro3.slaughterhouse.repository.*;
import via.pro3.slaughterhouse.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GuiController {

    private final AnimalRegistrationService animalService;
    private final ButcheringService butcheringService;
    private final PackagingService packagingService;
    private final TraceRepository traceRepository;

    private final AnimalRepository animalRepo;
    private final PartRepository partRepo;
    private final TrayRepository trayRepo;
    private final ProductRepository productRepo;

    public GuiController(AnimalRegistrationService animalService,
                         ButcheringService butcheringService,
                         PackagingService packagingService,
                         TraceRepository traceRepository,
                         AnimalRepository animalRepo,
                         PartRepository partRepo,
                         TrayRepository trayRepo,
                         ProductRepository productRepo) {

        this.animalService = animalService;
        this.butcheringService = butcheringService;
        this.packagingService = packagingService;
        this.traceRepository = traceRepository;
        this.animalRepo = animalRepo;
        this.partRepo = partRepo;
        this.trayRepo = trayRepo;
        this.productRepo = productRepo;
    }


    // --------------------------
    // MAIN GUI PAGE
    // --------------------------
    @GetMapping("/gui")
    public String gui(Model model) {

        model.addAttribute("animals", animalRepo.findAll());
        model.addAttribute("parts", partRepo.findAll());
        model.addAttribute("trays", trayRepo.findAll());
        model.addAttribute("products", productRepo.findAll());

        return "gui";
    }


    // --------------------------
    // STATION 1: ANIMAL REGISTRATION
    // --------------------------
    @PostMapping("/gui/animals/add")
    public String addAnimal(
            @RequestParam double weight,
            @RequestParam String arrivalDate,
            @RequestParam(required = false) String origin
    ) {

        var dto = new via.pro3.slaughterhouse.dto.animal.CreateAnimalDto();
        dto.weight = weight;
        dto.arrivalDate = String.valueOf(LocalDate.parse(arrivalDate));
        dto.origin = origin;

        animalService.registerAnimal(dto);
        return "redirect:/gui";
    }


    // --------------------------
    // STATION 2: BUTCHERING
    // --------------------------

    @PostMapping("/gui/parts/add")
    public String addPart(
            @RequestParam String type,
            @RequestParam double weight,
            @RequestParam(required = false) Long animalId
    ) {
        Part p = new Part();
        p.setType(type);
        p.setWeight(weight);

        if (animalId != null)
            p.setAnimal(animalRepo.findById(animalId).orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST)));

        partRepo.save(p);
        return "redirect:/gui";
    }

    @PostMapping("/gui/trays/add")
    public String addTray(
            @RequestParam String type,
            @RequestParam double maxWeight
    ) {
        Tray t = new Tray();
        t.setType(type);
        t.setMaxWeight(maxWeight);
        trayRepo.save(t);

        return "redirect:/gui";
    }

    @PostMapping("/gui/parts/assign")
    public String assignPartToTray(
            @RequestParam Long partId,
            @RequestParam Long trayId
    ) {
        butcheringService.putPartOnTray(partId, trayId);
        return "redirect:/gui";
    }


    // --------------------------
    // STATION 3: PACKAGING
    // --------------------------

    @PostMapping("/gui/packaging/same")
    public String createSameTypeProduct(@RequestParam String partIds) {

        List<Long> ids = Arrays.stream(partIds.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        packagingService.createSameTypeProduct(ids);
        return "redirect:/gui";
    }

    @PostMapping("/gui/packaging/half")
    public String createHalfAnimalProduct(@RequestParam String partIds) {

        List<Long> ids = Arrays.stream(partIds.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        packagingService.createHalfAnimalProduct(ids);
        return "redirect:/gui";
    }



    // --------------------------
    // TRACEABILITY
    // --------------------------

    @GetMapping("/gui/trace/product")
    public String traceByProduct(@RequestParam Long id, Model model) {

        List<String> animals = traceRepository.findAnimalIdByProductId(id);
        model.addAttribute("traceAnimals", animals);

        return gui(model);
    }

    @GetMapping("/gui/trace/animal")
    public String traceByAnimal(@RequestParam Long id, Model model) {

        List<Long> products = traceRepository.findProductIdsByAnimalId(String.valueOf(id));
        model.addAttribute("traceProducts", products);

        return gui(model);
    }

}
