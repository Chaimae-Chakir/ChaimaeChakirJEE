package ma.enset.test;

import ma.enset.test.entities.Client;
import ma.enset.test.entities.TypeAbonnement;
import ma.enset.test.services.AbonnementService;
import ma.enset.test.services.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(ClientService clientService, AbonnementService abonnementService){
        return args->{
            clientService.saveClient(new Client(null,"Chaimae chakir","chaimaechakir@gmail.com","Chakir"));
            clientService.saveClient(new Client(null,"Hiba rami","hibarami@gmail.com","Rami"));
            clientService.saveClient(new Client(null,"Hamza Louriz","hamzalouriz@gmail.com","Louriz"));

            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.valueOf(TypeAbonnement.TELEPHONE_FIXE.toString()), 1000.0f, 200.00f, clientService.findClientByUsername("Chakir"));
            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.valueOf(TypeAbonnement.TELEPHONE_FIXE.toString()), 120.0f, 280.00f, clientService.findClientByUsername("Louriz"));

            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.valueOf(TypeAbonnement.INTERNET.toString()), 80.0f, 250.00f, clientService.findClientByUsername("Rami"));
            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.valueOf(TypeAbonnement.GSM.toString()), 100.0f, 125.00f, clientService.findClientByUsername("Rami"));
            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.valueOf(TypeAbonnement.TELEPHONE_FIXE.toString()), 50.0f, 100.00f, clientService.findClientByUsername("Rami"));

            abonnementService.saveAbonnementToClient(new Date(), TypeAbonnement.INTERNET, 12.0f, 250.00f, clientService.findClientByUsername("Louriz"));
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
