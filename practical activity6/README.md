# Mise en oeuvre d'un micro-service

## Introduction 

Dans cette activité, nous apprendrons à créer un micro-service de gestion des comptes bancaires.

## Les dépendances
Le projet va initiliser avec ses dépendances suivantes :
* Spring Web
* Spring Data JPA
* H2 Database
* Lombok
* Spring GraphQL
* Mysql 
## Crée les entités JPA
Dans le package ```ma.enset.microService.entities```, nous aons ajouter les entités suivantes : 
* ``` Client.java ```
``` java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nom;
    @OneToMany(mappedBy = "client")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Compte> compteList;
}

```
* ``` Compte.java ```
``` java
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Compte {
    @Id
    private String id;
    private Date creatAt;
    private Double balance;
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @ManyToOne
    private  Client client;
}
```
Dans le packages ```ma.enset.microService.enums```, nous avons ajouter une enumeration a fin de determiner le type de compte.
* ``` AccountType.java ```
``` java
public enum AccountType {
    CURRENT_ACCOUNT,SAVING_ACCOUNT
}
```
## Les répositories
Dans le packages ```ma.enset.microService.repositories```, nous avons ajouter les interfaces suivantes : 
* ``` ClientRepository.java ```
``` java 
public interface ClientRepository extends JpaRepository<Client,Long>{
}
```
* ``` CompteRepository.java ```
``` java 
@RepositoryRestResource
public interface CompteRepository extends JpaRepository<Compte,String> {
    @RestResource(path = "/byType")
    List<Compte> findByType(@Param("t") AccountType type); // spring rest va implementer la methode.
    @RestResource(path = "/byClient")
    List<Compte> findByClient(@Param("c") Client client); // spring rest va implementer la methode.

}
```


Remarque :
 * ```@RepositoryRestResource``` : permet la génération automatique de endpoint RESTful pour effectuer des opérations CRUD (Create, Read, Update, Delete) sur les données sous-jacentes gérées par le référentiel. 
 * ```@RestResource(path = " ")``` : est une annotation fournie par Spring Data qui permet de personnaliser les endPoints RESTful générés pour un référentiel (repository) spécifique. cette notation utiliser pour les methodes. comme l'endPoint RESTful suivant:  ```http://localhost:8080/comptes/search/byType?t=CURRENT_ACCOUNT```.

## Les services
Dans le package ```ma.enset.microService.service```, nous ajoutons l'interface ```CompteService``` et leur calsse d'implementation.
* ``` CompteService.java ```
``` java 
public interface CompteService {
     CompteResponseDTO addCompte(CompteRequestDTO compteRequestDTO);

     CompteResponseDTO updateCompte(String id, CompteRequestDTO compteRequestDTO);
}
```
* ```CompteServiceImpl.java```
```java

@Service
@Transactional
public class CompteServiceImpl implements CompteService {
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteMapper compteMapper;
    @Override
    public CompteResponseDTO addCompte(CompteRequestDTO compteRequestDTO) {
        Compte compte = compteMapper.getCompte(compteRequestDTO);
        Compte compte1=compteRepository.save(compte);
        return compteMapper.getCompteResponseDTO(compte1);
    }

    @Override
    public CompteResponseDTO updateCompte(String id, CompteRequestDTO compteRequestDTO) {
        Compte compteFind=compteRepository.findById(id).orElseThrow();
        Compte compte = new Compte();
        if(compteFind!=null) compte=compteMapper.getCompteById(compteFind,compteRequestDTO);
        return compteMapper.getCompteResponseDTO(compteRepository.save(compte));
    }
}

```
## Les DTOs

Les DTOs sont des objets légers utilisés pour transférer des données entre les entites JPA et GraphQL.
Dans le package ```ma.enset.microService.dto```, nous avons ajouter les classe suivantes :

* ``` CompteRequestDTO.java ```
``` java 
@Data @NoArgsConstructor @AllArgsConstructor @Builder

public class CompteRequestDTO {

    private Double balance;
    private String currency;
    private AccountType type;

}
```
* ``` CompteResponseDTO.java ```
``` java 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompteResponseDTO {
    private String id;
    private Date creatAt;
    private Double balance;
    private String currency;
    private AccountType type;

}
```

## Les mappers

Les mappers sont des composants logiciels utilisés pour convertir des objets d'un type à un autre, dans notre cas on va covertire des objets de type JPA a autre de type GraphQL.

Dans le package ```ma.enset.microService.mappers```, nous ajetons les mappers suivants : 

* ```CompteMapper.java```
``` java
@Component
public class CompteMapper
{
    @Autowired
    CompteRepository compteRepository;
    public CompteResponseDTO fromCompte(Compte compte){
        return getCompteResponseDTO(compte);
    }

    public static CompteResponseDTO getCompteResponseDTO(Compte compte) {
        CompteResponseDTO compteResponseDTO = new CompteResponseDTO();
        BeanUtils.copyProperties(compte,compteResponseDTO);
        return compteResponseDTO;
    }
    public Compte getCompte(CompteRequestDTO compteRequestDTO){
        Compte compte = new Compte();
        compte.setId(UUID.randomUUID().toString());
        compte.setCreatAt(new Date());
        BeanUtils.copyProperties(compteRequestDTO,compte);
        return compte;
    }
    public Compte getCompte(Compte compte, Compte contFind){
        BeanUtils.copyProperties(compte,contFind);
        return contFind;
    }

    public Compte getCompteById(Compte compteFind, CompteRequestDTO compteRequestDTO) {
        Compte compte = new Compte();
        BeanUtils.copyProperties(compteRequestDTO,compte);
        compte.setId(compteFind.getId());
        compte.setCreatAt(compteFind.getCreatAt());
        return  compte;
    }
}
```
## Tester la couche DAO 

Pour tester notre application ,  nous ajoutons ce code dans la classe ```MicroServiceApplication.java```.

``` java
@SpringBootApplication

public class MicroServiceApplication {
    @Autowired
    CompteRepository compteRepository;
    @Autowired
    ClientRepository clientRepository;
    public static void main(String[] args) {
        SpringApplication.run(MicroServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start()
    {
        return args -> {
            Stream.of("Sokaina","Saad","Ahmed saad","Mohammed saad").forEach((m)->
                   clientRepository.save(Client.builder().nom(m).build()));
            clientRepository.findAll().forEach(client -> {
                for(int i=0 ; i<10; i++){
                    Compte compte =Compte.builder()
                            .id(UUID.randomUUID().toString())
                            .type(Math.random()>0.5? AccountType.SAVING_ACCOUNT:AccountType.CURRENT_ACCOUNT)
                            .balance(1000*Math.random()*90000)
                            .creatAt(new Date())
                            .currency("MAD")
                            .client(client)
                            .build();
                    compteRepository.save(compte);
                }
            });
            clientRepository.findAll().forEach(client -> {
               List<Compte> compteList=compteRepository.findByClient(client);
               client.setCompteList(compteList);
            });
        };
    }
}

```

- ``` @Bean ``` : ca permet de lancer la methode start lorsque l'application demarre. 
- Nous avons utliser les repositories JPA pour creer les comptes bancaires.
- pour verifie que les comptes sont ajoute a la base de donnee, on va consulter l'interface graphique de MySql ``` phpMyAdmin ```.

![image](https://user-images.githubusercontent.com/48890714/236681784-f90e2083-fcee-4ff6-b4b0-1e6dbffa4d21.png)


![image](https://user-images.githubusercontent.com/48890714/236681656-4dffb3fa-1567-4902-91fd-b87db42038d9.png)

## Créer le Web service Restfull qui permet de gérer des comptes

Pour creer le web service Restfull qui permet de  gérer des comptes, nous avons besoin seulement d'ajouter une annotation ```RepositoryRestResource``` dans l'interface 
```CompteRepository.java```. 

Cette annotation nous permet d'utiliser Spring data Rest pour generer automatiquement les endPoints.

* Exemple des endPoints Generer :
  - ```http://localhost:8080/comptes```: permet de récupérer la liste des comptes bancaires.
  
  ![image](https://user-images.githubusercontent.com/48890714/236682864-4fe51bf5-d18a-4dc5-8c68-8e45b87ec852.png)
  - ``` http://localhost:8080/clients```:  permet de récupérer la liste des clients.
  
  ![image](https://user-images.githubusercontent.com/48890714/236682927-d392517f-b194-4b93-ba1a-abb66e8735c5.png)
  
## RestController

Dans le package ```ma.enset.microService.web```, nous avons creer un controller Rest qui permet de gérer des comptes bancaires.
- ``` CompteRestController.java```
``` java
@RestController
@RequestMapping("/api")
public class CompteRestController {
    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private CompteMapper compteMapper;
    @Autowired
    private CompteService compteService;
    public CompteRestController(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }
    @GetMapping("/banckCompte")
    public List<Compte> listofCompte(){
        return compteRepository.findAll();
    }
    @GetMapping("/banckCompte/{id}")
    public Compte getCompte(@PathVariable("id") String id){
        return compteRepository.findById(id).orElseThrow(()->new RuntimeException(String.format("Compte non trouve",id)));
    }
    /* En utilisant la projection */
    @PostMapping("/banckCompte")
    public CompteResponseDTO save(@RequestBody CompteRequestDTO compte){
        return  compteService.addCompte(compte);
    }
    @PutMapping("/banckCompte/{id}")
    public Compte update(@PathVariable String id,@RequestBody Compte compte){
        Compte compteFind=compteRepository.findById(id).orElseThrow();
        compteFind=compteMapper.getCompte(compte,compteFind);
        return  compteRepository.save(compteFind);
    }
    @DeleteMapping("/banckCompte/{id}")
    public void deleteCompte(@PathVariable("id") String id){
        compteRepository.deleteById(id);
    }
}
```
Nous avons utliser la projection ```CompteResponseDTO``` pour la methode  save.

## Test EndPoints

Maintenant en test les endPoints s'il fonctioone.

- ```http://localhost:8080/api/banckCompte``` : affiche une liste des comptes bancaires.
- ```http://localhost:8080/api/banckCompte/fe910435-f43c-4d12-8de0-b223016f81e5```: permet de recupperer le compte de l'identifiant suivant : ```fe910435-f43c-4d12-8de0-b223016f81e5```.

il existe d'autre endPoints que ce soit pour supprimer, modifier ou ajouter un compte.

## Test Client comme Postman

Maintenat on va tester ses endPoints on utilisons Postman.

![image](https://user-images.githubusercontent.com/48890714/236683760-7e6268bc-8105-43dd-8a86-58fb0939cece.png)

## Générer et tester le documentation Swagger de des API Rest du Web service

Pour la documentation de notre Api, nous utilisons Swagger.

C'est quoi Swagger ?  on peut dire que c’est un ensemble d’outils pour aider les développeurs dans la conception, le build, la documentation et la consommation d’API.

Pour ouvrire l'interface graphique de Swagger, on acceder a le lien suivant : ```http://localhost:8080/swagger-ui/index.html```.

Cette interface va illustrer l'ensemble des apis de votre micro service et comment l'utliser.

![image](https://user-images.githubusercontent.com/48890714/236684221-a63b6d00-56f4-485d-b98d-7f4a00daea8c.png)

## Créer un Web service GraphQL pour ce micro-service

GraphQL est un langage de requête pour les API et un runtime pour répondre à ces requêtes avec vos données existantes. GraphQL fournit une description complète et compréhensible des données de votre API, donne aux clients le pouvoir de demander exactement ce dont ils ont besoin et rien de plus, facilite l'évolution des API au fil du temps et active de puissants outils de développement.

Pour utiliser GrapheQL dans notre projet existe des depandances  dans le fichier ``` pom.xml ```.
``` xml 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-graphql</artifactId>
        </dependency>
```

Dans Le dossier Ressource, nous ajoutons un dossier grapheQL dans lequel on va creer fichier ```schema.graphqls```.  dans cette fichier on va presicer le schema GraphQL.
- ```schema.graphqls```.
```graphqls
type Query{
    ComptesListe:[Compte],
    getCompteById(id:String):Compte,
    clients:[Client]
}
type Client{
    id:Float,
    nom:String,
    compte:[Compte]
}
type Compte {
    id: String,
    creatAt: Float,
    balance: Float,
    currency: String,
    type:String,
    client:Client
}


```
### Query
cette schema contient des requetes, une qui retoure une liste des comptes, autre retourne compte en specifiant l'identifiant et le dernier permet de recuperer une liste des clients.

Pour tester, dans le package ```ma.enset.microService.web``` nous creons la classe suivante ```CompteGrapheQLController.java``` qui vq nous permettre de tester le schema GraphQL.
- ```CompteGrapheQLController.java```
``` java
@Controller
public class CompteGrapheQLController {
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CompteService compteService;

    @Autowired
    private ClientRepository clientRepository;

    @QueryMapping
    public List<Compte> ComptesListe(){
           return compteRepository.findAll();
    }
    @QueryMapping
    public List<Client> clients(){
        return  clientRepository.findAll();
    }
}
```

 - @QueryMapping permet de liee les methodes avec les requetes GraphQL. il faut utliser les memes noms de la requete GrapheQL et de la methode.
 - Il faut ajouter une configuration dans le fichier ```application.properties```.
 
 ``` properties
 spring.graphql.graphiql.enabled=true
 ```
 
 Pour acceder a l'interface de test de GraphQL, utliser URl suivant ```http://localhost:8080/graphiql?path=/graphql```.
 > Remarque : toute les requetes utiliser dans GraphQl sont en post,


Il existe deux notions important dans GraphQL :
 - ```Query```  : nous utilisons des requêtes pour récupérer des données.
 - ```Mutation``` : nous utilisons des mutations pour modifier les données côté serveur.

> GraphQL nous permet de recupper uniquement les donnees dont on a besoin.

Pour la gestion des exceptions, on cree le package ```ma.enset.microService.exceptions``` et on cree la classe ```CustomDataFetcherExceptionResolver```  qui herite de classe ```DataFetcherExceptionResolverAdapter```.
- ```CustomDataFetcherExceptionResolver.java```
``` java
@Controller
public class CustomDataFetcherExceptionResolver  extends DataFetcherExceptionResolverAdapter {
    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return new GraphQLError() {
            @Override
            public String getMessage() {
                return ex.getMessage();
            }

            @Override
            public List<SourceLocation> getLocations() {
                return null;
            }

            @Override
            public ErrorClassification getErrorType() {
                return null;
            }
        };
    }
}
```
> Si on cherche maintenant un compte n'existe pas va retourner un message ```compte n'existe pas ``` au lien de generer une execption.
### Mutation 

les mutations sont utilises pour modifier les données côté serveur.
Dans notre application, nous avons utliser 3 mutations :

- ``` addCompte ``` pour ajouter un compte bancaire.
- ``` updateCompte ``` pour modifier un compte bancaire.
- ``` deleteCompte ``` pour supprimer un compte bancaire.

ses methodes sont ajouter dans la classe ```CompteGrapheQLController.java```:

``` java
    @MutationMapping
    public CompteResponseDTO addCompte(@Argument CompteRequestDTO compte){

        return compteService.addCompte(compte);
    }

    @MutationMapping
    public CompteResponseDTO updateCompte(@Argument String id,@Argument CompteRequestDTO compte){

        return compteService.updateCompte(id,compte);
    }
    @MutationMapping
    public Boolean deleteCompte(@Argument String id){
        compteRepository.deleteById(id);
        return true;
    }
```

dans le fichier ```schema.graphqls``` ajouter les mutations:

``` graphqls
type Mutation{
    addCompte(compte:CompteDTO):Compte
    updateCompte(id:String,compte:CompteDTO):Compte
    deleteCompte(id:String):Boolean
}
input  CompteDTO
{
    balance: Float
    currency: String
    type:String
}
```
 - ``` type mutation ``` permet de definir les mutations
 - ``` input ``` permet de definir les parametres d'entrees
 
## test GraphQL 
### Query 
on execute la requete suivante :
```
    query{
      ComptesListe{id,balance}
    }
```
on obient les resultat suivant :
``` json 
{
  "data": {
    "ComptesListe": [
      {
        "id": "0983f32e-75a6-418e-8504-2d809ed88a39",
        "balance": 60568913.63253504
      },
      {
        "id": "1699268f-c96c-4f15-8ede-a361696c8259",
        "balance": 13089866.692350285
      },
      {
        "id": "1c71cc54-f9ac-4c12-844d-be154c92b295",
        "balance": 49477828.37263954
      }
    ]
  }
}
```
### Mutation

on test la mutation suivante ;
```
mutation{
 addCompte(compte:{balance: 15000, currency: "DH", type: "SAVING_ACCOUNT"}) {
    id, balance
  }
}
```
on obtient la resultat suivante :

``` json 
{
  "data": {
    "addCompte": {
      "id": "c9165d59-3d40-4828-b43c-04e5f41128c1",
      "balance": 15000
    }
  }
}
```
## Conclusion
Dans cette activite nous avons connaitre comment cree un micro service et generer une documentation Swagger et aussi nous avons location de creer un web service GraphQL pour le micro-service que nous avons deja cree.


