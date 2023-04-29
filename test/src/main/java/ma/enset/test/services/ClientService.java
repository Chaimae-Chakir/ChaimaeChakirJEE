package ma.enset.test.services;


import ma.enset.test.entities.Client;
import ma.enset.test.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    public Client saveClient(Client client) {
        if (client != null && client.getNom() != null && client.getEmail() != null) {
            return clientRepository.save(client);
        }
        return null;
    }
    public Client findClientById(Long id){return clientRepository.findById(id).orElse(null);}
    public Page<Client> findNomContains(String keyword, Pageable pageable) {
        return clientRepository.findByNomContains(keyword,pageable);
    }
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Client findClientByUsername(String username) {
        return clientRepository.findClientByUsername(username);
    }
}
