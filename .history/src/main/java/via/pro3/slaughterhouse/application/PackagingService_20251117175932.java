package via.pro3.slaughterhouse.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import via.pro3.slaughterhouse.domain.Part;
import via.pro3.slaughterhouse.domain.Product;
import via.pro3.slaughterhouse.domain.ProductKind;
import via.pro3.slaughterhouse.repository.PartRepository;
import via.pro3.slaughterhouse.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class PackagingService {

    private final ProductRepository productRepo;
    private final PartRepository partRepo;

    // Możesz nadpisać w application.properties, patrz niżej.
    private final Set<String> halfAnimalRequired;

    public PackagingService(
            ProductRepository productRepo,
            PartRepository partRepo,
            @Value("${packaging.halfAnimal.requiredTypes:leg,rib,loin,shoulder,belly}")
            String requiredCsv
    ) {
        this.productRepo = productRepo;
        this.partRepo = partRepo;
        this.halfAnimalRequired = Arrays.stream(requiredCsv.split(","))
                .map(s -> s.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getHalfAnimalRequired() {
        return Collections.unmodifiableSet(halfAnimalRequired);
    }

    @Transactional
    public Product createSameTypeProduct(Collection<UUID> partIds) {
        if (partIds == null || partIds.isEmpty()) {
            throw new IllegalArgumentException("No parts provided");
        }

        // Pobierz części i sprawdź, czy wszystkie istnieją
        List<Part> parts = partRepo.findAllById(partIds);
        if (parts.size() != new HashSet<>(partIds).size()) {
            throw new IllegalArgumentException("Some part IDs do not exist");
        }

        String baseType = parts.get(0).getType();
        boolean allSame = parts.stream()
                .allMatch(p -> p.getType() != null &&
                        p.getType().equalsIgnoreCase(baseType));
        if (!allSame) {
            throw new IllegalStateException(
                    "SAME_TYPE product must contain only one part type");
        }

        Product product = new Product();
        product.setKind(ProductKind.SAME_TYPE);
        product.setParts(new HashSet<>(parts));
        return productRepo.save(product);
    }

    @Transactional
    public Product createHalfAnimalProduct(Collection<UUID> partIds) {
        if (partIds == null || partIds.isEmpty()) {
            throw new IllegalArgumentException("No parts provided");
        }

        List<Part> parts = partRepo.findAllById(partIds);
        if (parts.size() != new HashSet<>(partIds).size()) {
            throw new IllegalArgumentException("Some part IDs do not exist");
        }

        Set<String> present = parts.stream()
                .map(Part::getType)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        for (String req : halfAnimalRequired) {
            if (!present.contains(req)) {
                throw new IllegalStateException(
                        "HALF_ANIMAL missing required type: " + req);
            }
        }

        Product product = new Product();
        product.setKind(ProductKind.HALF_ANIMAL);
        product.setParts(new HashSet<>(parts));
        return productRepo.save(product);
    }
}
