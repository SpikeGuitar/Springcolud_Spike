package spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpikeNatServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpikeNatServerApplication.class,args);
    }
}
