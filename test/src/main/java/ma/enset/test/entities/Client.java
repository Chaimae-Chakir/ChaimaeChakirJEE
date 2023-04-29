package ma.enset.test.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty @Size(min = 4,max = 40)
    private String nom;
    @NotEmpty
    @Column(nullable = false, unique = true, length = 45)
    private String email;
    @NotEmpty @Size(min = 4,max = 40)
    private String username;
    @OneToMany(mappedBy = "client",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Abonnement> abonnements;

    public Client(Long id, String nom, String email, String username) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.username = username;
    }
}
