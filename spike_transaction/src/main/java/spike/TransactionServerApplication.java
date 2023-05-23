package spike;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TransactionServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionServerApplication.class, args);
    }
}
