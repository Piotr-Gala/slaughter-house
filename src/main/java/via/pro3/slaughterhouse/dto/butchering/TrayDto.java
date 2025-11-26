package via.pro3.slaughterhouse.dto.butchering;

import via.pro3.slaughterhouse.domain.Tray;

public class TrayDto {
    public Long id;
    public String type;
    public double maxWeight;

    public static TrayDto from(Tray tray) {
        TrayDto dto = new TrayDto();
        dto.id = tray.getId();
        dto.type = tray.getType();
        dto.maxWeight = tray.getMaxWeight();
        return dto;
    }
}
