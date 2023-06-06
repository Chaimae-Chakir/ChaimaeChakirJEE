# Use case JPA, Hibernate Spring Data, One To Many, One To One | Gestion d'un hopital

Dans cette partie on va traiter  les relations One To Many et One To One avec l'utilisation de JPA, Hibernate et Spring web pour implémenter un Système de gestion hôpital. Ce projet va initialiser par H2 vers la fin nous merger vers MySql.
## Les dépandances :
Nous devons créer le projet avec les dépandances suivantes :

- Spring web
- Spring Data JPA
- Lombok
- H2 Database

## La conception du projet

![image](https://user-images.githubusercontent.com/48890714/229516751-4d58aa57-7e21-4a49-9be4-4aa37b97b7d9.png)


## Configuration de base de données 

H2 est une base de données mémoire pour le test, dans le fichier ``` application.properties ``` on va configurer notre base de données de test.

``` properties
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:hobital
server.port=8080
spring.datasource.username=root
spring.datasource.password=
```

## Les entités 

Dans le packages entities  ```com.example.gestionhopital.entities```, nous créons les entités suivantes :

- ``` Patient.java ```
- ``` Consultation.java ```
- ``` Medecin.java ```
- ``` RendezVous.java ```
- ``` StatusRDV.java ``` : enumération permet de connaitre l'état de rendez vous

## Les repositories 

Dans le packages reporistories  ```com.example.gestionhopital.repositories```, nous créons les reporistories suivantes :
 
- ``` PatientRepository.java ```
- ``` ConsultationRepository.java ```
- ``` MedecinRepository.java ```
- ``` RendezVousRepository.java ```

## Les services 
Dans le package service ```com.example.gestionhopital.services``` , nous créons les services suivants :

- ``` HospitalService.java ```
- ``` HospitalServiceImpl.java ```

## Les controllers

Dans le package web ```com.example.gestionhopital.web```, nous créons les controllers suivants :
- ``` PatientRestController.java ```

## Test 

Pour effectuer le test, nous avons creer des donnees dans la classe ```GestionHopitalApplication.java```  qui contient le code suivant :

``` java

    @SpringBootApplication
    public class GestionHopitalApplication {


        public static void main(String[] args) {
            SpringApplication.run(GestionHopitalApplication.class, args);
        }

        @Bean
        CommandLineRunner start(HospitalServiceImpl hospitalService){
            return args -> {
                Stream.of("mohammed","hassan","Najet","saad","sokaina").forEach(name->{
                    Patient patient= new Patient();
                    patient.setNom(name);
                    patient.setDateNaisssance(new Date());
                    patient.setMalade(false);
                    hospitalService.savePatient(patient);
                });
                Stream.of("mohammed","yassir","yasmine").forEach(name->{
                    Medecin medecin= new Medecin();
                    medecin.setNom(name);
                    medecin.setSpecialite(Math.random()>0.5?"Cardio":"Dentiste");
                    medecin.setEmail(name+"@gmail.com");
                    hospitalService.saveMedecin(medecin);
                });

                Patient patient = hospitalService.findPatientById(1L); // s'il n'existe pas va retourner null
                Patient patient2= hospitalService.findPatientByNom("saad");

                Medecin medecin = hospitalService.findMedecinByNom("yasmine");
                RendezVous rendezVous = new RendezVous();
                rendezVous.setDate(new Date());
                rendezVous.setStatusRDV(StatusRDV.PENDING);
                rendezVous.setPatient(patient);
                rendezVous.setMedecin(medecin);
                RendezVous saveRDV=hospitalService.saveRendezVous(rendezVous);
                System.out.println(saveRDV.getId());  // si on veut consulter
                RendezVous rendezVous1 = hospitalService.findRendezVousById(1l);
                Consultation consultation = new Consultation();
                consultation.setDateConsultation(rendezVous1.getDate());
                consultation.setRendezVous(rendezVous1);
                consultation.setRapport(" Rapport de la consultation .......");
                hospitalService.saveConsultation(consultation);


            };
        }
}

```
Aprés avoir éxécuter le code, accéder à ce lien http://localhost:8080/h2-console est vérifier si les données existe dans la base de données H2.

## Changer Base de données à MySQL

On va ajouter ces parametres au fichier application.properties.

``` properties
  server.port=8080
  spring.datasource.username=root
  spring.datasource.password=
  spring.datasource.url=jdbc:mysql://localhost:3306/hospital?createDatabaseIfNotExist=true
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
  spring.jpa.show-sql=true
```
et dans le fichier pom.xml rajouter cette dépandance :

``` xml
   <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.32</version>
   </dependency>
```

## Test

Pour tester si migration de h2 vers MySQL bien passe . vérifier si les données sont insérer dans la base de données avec succés.

ou peut acceder à un URL ``` localhost:8080/patients ``` , grace à le code suivant dans le classe ``` PatientRestController.java ```

``` java
  @RestControlle
  public class PatientRestController {
      @Autowired
      private PatientRepository patientRepository;

      @GetMapping("/patients")
      public List<Patient> patientList(){
          return patientRepository.findAll();
      }
  }

````


Ce travail nous a permis de bien connaitre comment implémenter les relations  One To Many, One To On est  ce que fait au niveau du Java grace à l’API JPA, et on a initié avec Hibernate, et bien évidemment la manière de configuration d’un projet Spring pour faciliter la tâche au développeur.
