# Spring MVC JPA Hibernate Spring Data Thymeleaf
# Objectif 
L'objectif de cette activté est de créer dans la premiere partie une application Web JEE basée sur Spring MVC, Thylemeaf et Spring Data JPA qui permet de gérer les patients. dans la deuxiéme partien, on va sécurisé notre application à l'aide de spring security.
# Partie 1 : Gestion Patient | Application Web
Nous allons prendre en charge le projet de gestion des patients que nous allons effectuer dans le cadre de l'activité 3, puis nous allons le transformer en application Web. qui vq permet de réaliser ses fonctionnalié suivante: 
- Afficher les patients.
- Faire la pagination.
- Chercher les patients.
- Supprimer un patient.
- Faire des améliorations supplémentaires.
# Partie 2 : Spring Security - Spring Data | Securing a Web Application.
Spring securite offre plusieure methode pour faire l'authentification dans ce projet nous avons utliser Statefull authentification 
## Statefull authentification 
### Principe : 
Statefull authentification est couramment utilisée dans de nombreuses applications, en particulier pour les applications qui ne nécessitent pas trop d'évolutivité.
### Comment ça fonctionne ? 
La session avec état est créée côté backend et l'ID de référence de session correspondant est envoyé au client. Chaque fois que le client fait une demande au serveur, le serveur localise la mémoire de session en utilisant l'identifiant de référence du client et trouve les informations d'authentification.

Dans ce modèle, vous pouvez facilement imaginer que si la mémoire de session est supprimée côté backend, alors l'ID de référence de session, que le client détient, n'a aucun sens.

![image](https://github.com/sokainadaabal/SokainaDaabalJEE/assets/48890714/650027c1-4d15-4bd9-a9fb-921f8fe617c3)

## Dependencies

Ajoutez les dépendances suivantes au projet ``` pom.xml ```  a fin d'utliser la bilotheque de ``` Spring Security``` :

``` xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
```
## Configuration de la securite | Spring Security
Dans le package ```ma.enset.sec```, on va ajouter class ```SecurityConfig```. ce dernier va nous permettons de configurer la securite de notre application.
```java 
@Bean
    SecurityFilterChain defaultSecurityFilterChain(@NotNull HttpSecurity http) throws Exception {
        http.formLogin().permitAll(); // formulaire authentication contient csrf caché // personnaliser la page
        http.authorizeRequests().requestMatchers("/").permitAll();
        http.authorizeRequests().requestMatchers("/webjars/***").permitAll();
        http.authorizeRequests().requestMatchers("/admin/**").hasAuthority("ADMIN");
        http.authorizeRequests().requestMatchers("/user/**").hasAuthority("USER");
        http.authorizeRequests().anyRequest().authenticated(); // toute request envoyer doit que l'utilisateur authentifier
        http.exceptionHandling().accessDeniedPage("/403");
        return http.build();
    }
``` 
> cette fonction nous permet de donnee les droits d'acces certain url pour des utlisateurs specifiaues. et autorise l'acces a des url sans specifie le role de l'utlisateur. En generale, Cette fontion permet de montionner les requetes qui doivent etre authentifiees.
## Creation des utlisateurs 

Pour cree des utlisateurs de l'application on va ajouter cette fonction.
``` java
 @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder passwordEncoder=passwordEncoder();
        ArrayList<UserDetails> users= new ArrayList<>();

        UserDetails user = User.withUsername("user1")
                .username("user1")
                .password(passwordEncoder.encode("1234")) 
                .roles("USER")
                .build();
        users.add(user);
        return new InMemoryUserDetailsManager(users);
```
> Explication :

> - Si le mot de pass n'est pas encode en va utliser cette instruction ```password("{noop} 123")```
> - ```PasswordEncoded``` est pour encoder le mot de passe, sera explique par la suite.
> - Pour assossier l'utilisateur a un role on va utiliser ```roles("USER")``` si l'utilisateur a un seul role. si possede plusieurs role roles("USER","ADMIN").
> - ``` User.withUsername("user1").username("user1").password(passwordEncoder.encode("1234")) .roles("USER").build() ``` on cree un utlisateur avec le nom ```user1``` et avec un mot de passe ```1234``` et le role ```user```.
## Encoder le mot de passe 
pour encoder le mot de passe, nous avons utliser ```PasswordEncoder```.
Dans la classe ```SecurityConfig```,on declare un objet de type ``` PasswordEncoder : 

``` java 
@Autowired 
    private PasswordEncoder passwordEncoder;
```
Dans la classe ```ApplicationSpringApplication```,on ajoute la methode suivante : 
``` java 
 @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    } 

```
> - cette methode permet de faire le hashage de password au lieu d'utiliser md5 crypt password.
> - pour encoder le mot de passe, nous utilisons l'instruction suivant ```passwordEncoder.encode("1234")```.
## Sécurité côté Frontend :
Pour la gestion de la securite dans les themplate, spring boot offre une depandance ``` Thymeleaf extra-springsecurity6 ``` pour definire les droit d'acces dans chaque page.

Dans ```pom.xml``` ajouter la dependance suivante :

``` xml
        <dependency>
            <groupId>org.thymeleaf.extras</groupId>
            <artifactId>thymeleaf-extras-springsecurity6</artifactId>
        </dependency>
```
### Navbar 
Pour afficher le nom d'utlisateur dans navbar, nous modifions la balise suivante :

``` html
 <li class="nav-item dropdown" sec:authorize="isAuthenticated()">
     <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
        <span sec:authentication="name"></span>
     </a>
     <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
          <a class="dropdown-item" th:href="@{/logout}" >Logout</a>
     </div>
</li>
```
> Explication
 
> - ```isAuthenticated()``` permet de verifier si l'utilisatuer est connecter.
> - ``` sec:authentication="name"``` permet d'afficher le nom d'utlisateur connecte.
> - ``` th:href="@{/logout}"``` permet de deconnecter.
### Droit d'acces
Pour verifier les doroits d'acces du l'utilisateur connecter : 
``` html 
<th class="col"  sec:authorize="hasAuthority('ADMIN')"> Action </th>
```
> cette colonne ne sera afficher que pour les utilisateurs qu'ont le role admin.
# Tools
Spring boot offre plusieurs outils pour facilite le developpements d'une application. parmis ses outils on ``` Dev Tools ``` qui permet de charger automatiquement l'application apres chaque modification. 
Pour ajouter cette fonctionnalite,il existe deux etapes a suivre :
- Etape 1 : Ajouter la dependance dans ```pom.xml``` 
``` XML 
 <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
  </dependency>
```
- Etape 2 : Active cette fonctionnalite
Pour l'active, ilfaut ajouter une ligne dans le fichier ```application.properties```
```properties
spring.devtools.restart.enabled=true
```
A ce stade, on peut voir les modificaations sans avoir redemarrer l'application.
# Démenstration 

Dans cette video, on va voir notre application(Partie 1 | Partie 2).

## Utilisateur avec Role Admin et User 

https://github.com/sokainadaabal/SokainaDaabalJEE/assets/48890714/c6412126-a3c5-4bdb-a540-83d3601c0567

## Utilisateur avec Role User 

[Role_User.webm](https://github.com/sokainadaabal/SokainaDaabalJEE/assets/48890714/717c8d6b-8a2e-49df-a84b-29d961b643a0)
