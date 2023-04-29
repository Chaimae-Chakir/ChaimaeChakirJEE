package ma.enset.test.repositories;

import ma.enset.test.entities.Abonnement;
import ma.enset.test.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface AbonnementRepository extends JpaRepository<Abonnement,Long> {
    @Query("SELECT a FROM Abonnement a WHERE a.client = :client ")
    Page<Abonnement> findAllByClient(@Param("client") Client client, Pageable pageable);
}
