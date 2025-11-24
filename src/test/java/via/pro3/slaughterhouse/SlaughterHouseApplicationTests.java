package via.pro3.slaughterhouse;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled("Context load smoke test disabled for this project")

class SlaughterHouseApplicationTests {

    @Test
    void contextLoads() {
    }

}
