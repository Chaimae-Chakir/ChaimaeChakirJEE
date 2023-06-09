package ma.enset.customerservice;

import ma.enset.customerservice.entities.Customer;
import ma.enset.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.List;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, RepositoryRestConfiguration repositoryRestConfiguration) {
        repositoryRestConfiguration.exposeIdsFor(Customer.class);
        return args -> {
            customerRepository.saveAll(List.of(
                    Customer.builder().name("Mohammed").email("mohamed@gmail.com").build(),
                    Customer.builder().name("chaimae").email("chaimae@gmail.com").build(),
                    Customer.builder().name("IMane").email("imane@gmail.com").build()
            ));
            customerRepository.findAll().forEach(System.out::println);
        };
    }
}
