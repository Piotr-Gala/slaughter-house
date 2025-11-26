package via.pro3.slaughterhouse.dto.animal;

import via.pro3.slaughterhouse.domain.Animal;

public class AnimalDto {
    public String id;
    public double weight;
    public String arrivalDate;
    public String origin;

    public static AnimalDto from(Animal a) {
        AnimalDto dto = new AnimalDto();
        dto.id = a.getId() == null ? null : a.getId().toString();
        dto.weight = a.getWeight();
        dto.arrivalDate = a.getArrivalDate() == null ? null : a.getArrivalDate().toString();
        dto.origin = a.getOrigin();
        return dto;
    }
}