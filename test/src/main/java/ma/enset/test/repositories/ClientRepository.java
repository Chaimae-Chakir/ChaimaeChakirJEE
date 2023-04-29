package ma.enset.test.repositories;

import ma.enset.test.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findClientByUsername(String kw);
    Page<Client> findByNomContains(String keyword, Pageable pageable);

}
