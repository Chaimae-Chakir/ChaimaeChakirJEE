package ma.enset.test.services;


import ma.enset.test.entities.Abonnement;
import ma.enset.test.entities.Client;
import ma.enset.test.entities.TypeAbonnement;
import ma.enset.test.repositories.AbonnementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AbonnementService {
    @Autowired
    private AbonnementRepository abonnementRepository;

    public Abonnement saveAbonnementToClient(Date date_abonnement, TypeAbonnement type_abonnement, double solde, double montant_mensuel, Client client) {
        Abonnement abonnement = new Abonnement(null, date_abonnement, type_abonnement, solde, montant_mensuel, client);
        if (client == null)
            throw new IllegalArgumentException("Client cannot be null");
        return abonnementRepository.save(abonnement);
    }
    public void deleteAbonnement(Long id) {
        abonnementRepository.deleteById(id);
    }
    public Page<Abonnement> findAbonnements(Client c,Pageable pageable) {
        return abonnementRepository.findAllByClient(c,pageable);
    }
    public  Abonnement findAbonnement(Long id){
        return abonnementRepository.findById(id).orElse(null);}
}
