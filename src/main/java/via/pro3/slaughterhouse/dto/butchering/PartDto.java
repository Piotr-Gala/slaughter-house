package via.pro3.slaughterhouse.dto.butchering;

import via.pro3.slaughterhouse.domain.Part;

public class PartDto {
    public Long id;
    public double weight;
    public String type;
    public Long animalId;
    public Long trayId;

    public static PartDto from(Part part) {
        PartDto dto = new PartDto();
        dto.id = part.getId();
        dto.weight = part.getWeight();
        dto.type = part.getType();
        dto.animalId = part.getAnimal() != null ? part.getAnimal().getId() : null;
        dto.trayId = part.getTray() != null ? part.getTray().getId() : null;
        return dto;
    }
}
