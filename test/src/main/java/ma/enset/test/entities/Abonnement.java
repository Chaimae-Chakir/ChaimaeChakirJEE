package ma.enset.test.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.DATE)
    private Date dateAbonnement;
    private TypeAbonnement typeAbonnement;
    private double sold;
    private double montantMensuel;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}
