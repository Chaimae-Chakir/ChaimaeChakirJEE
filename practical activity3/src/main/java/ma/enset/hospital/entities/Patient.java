package ma.enset.hospital.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Patient  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    private boolean malade;
    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    private Collection<RendezVous> rendezVous;
}
