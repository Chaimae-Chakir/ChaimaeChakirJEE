package ma.enset.hospital.web;

import ma.enset.hospital.entities.Patient;
import ma.enset.hospital.repositories.PatientRepository;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class PatientRestController {
    private PatientRepository patientRepository;
    @GetMapping("/patients")
    public List<Patient> patientList(){
        return patientRepository.findAll();
    }
}
