# Principe de l’Inversion de Contrôle et Injection des dépendances

Dans cette partie, nous allons aborder l'inversion de contrôle et l'injection de dépendances, en séparant le code métier du code technique. Nous effectuerons l'injection des dépendances en utilisant un couplage faible et la dépendance des classes avec des interfaces.

## Architecture d'application 
![image](https://user-images.githubusercontent.com/48890714/230694406-2793f4a6-fe1d-458f-b922-4b6b8b41fadb.png)

## Notion de cours 

Il existe deux type d'architectures des applications : 

- ```Monotolithique``` : C'est un modèle traditionnel de développement de programmes où l'application est construite avec une seule architecture et une seule technologie. Elle est développée comme une unité autonome et indépendante des autres applications, et déployée sur un serveur d'application. L'objectif est de créer une seule application qui centralise toutes les fonctionnalités liées à un problème donné.

Avantages d'une architecture monolithique :

        - Déploiement facile.
        - Développement basé sur un seul ensemble de code.
        - Performances optimisées.
        - Tests simplifiés.
        - Facilité de débogage.

Inconvénients d'une architecture monolithique :

        - Centralisation de tous les besoins fonctionnels.
        - Utilisation d'une seule technologie.
        - Chaque modification nécessite des tests de régression pour vérifier l'impact sur les autres fonctionnalités, ainsi qu'un redéploiement de l'ensemble de l'application, rendant difficile son évolution au niveau fonctionnel.
        - Temps d'attente important pour les premières versions.
        - Processus de mise en production long.
        - Difficulté à effectuer des tests.
        - Performances limitées en termes de scalabilité. Si des problèmes de montée en charge surviennent, la solution de scalabilité horizontale peut être trop complexe.
        - Développement plus lent.
        - Manque d'évolutivité.
        - Fiabilité incertaine.
        - Obstacle à l'adoption de nouvelles technologies.
        - Manque de flexibilité.
        - Difficulté dans le déploiement.

    
- ```Micro-Services```: Il s'agit d'une approche architecturale basée sur une série de services indépendants, déployables de manière autonome. Chaque service possède sa propre logique métier et sa propre base de données, avec un objectif spécifique. Les mises à jour, les tests, le déploiement et la mise à l'échelle sont effectués individuellement pour chaque service. Chaque microservice est responsable d'une fonctionnalité et s'exécute dans un processus séparé.

Avantages des microservices :

        - Agilité accrue.
        - Évolutivité flexible.
        - Déploiement continu.
        - Administration et tests facilités.
        - Déploiement indépendant.
        - Flexibilité technologique.
        - Fiabilité élevée.
        - Satisfaction des équipes.
        - Choix de technologies.
        - Livraison continue.
        - Facilité des tests et du déploiement.
        - S'adapte bien aux processus de génie logiciel tels que le TDD (Test Driven Development) et les méthodes agiles.

       ``` java 
         // developpement classique 
          public class Calcul()
          {
            public doubke somme(double a, double b)
            {
               return a+b;
            }
          }
          
          // Class de test
          public class CalculTest()
          {
            @Test 
            public void testSomme(){
              double a=5;
              double b=8;
              double exp=a+b;
              Calcul c= new Calcul();
              double rest= c.somme(a,b);
              assertEqual(exp,rest);
            }
          }
       ```
        ``` java 
         // developpement  basé sur TDD
          // Class de test
          public class TestCalcul()
          {
            @Test 
            public void testSomme(){
              double a=5;
              double b=8;
              double exp=a+b;
              Calcul c= new Calcul(); // il va demander de créer la classe Calcul
              double rest= c.somme(a,b); // il va demander de créer la méthode somme
              assertEqual(exp,rest);
            }
          }
          
          public class Calcul()
          {
            public double somme(double a, double b)
            {
               return 0; // votre travaille est seulement de coder la fonction.
            }
          }
       ```

  Inconvénients des microservices :

        - Complexité accrue du développement.
        - Augmentation des coûts d'infrastructure.
        - Besoins organisationnels supplémentaires.
        - Défis de débogage.
        - Manque de standardisation.
        - Manque de responsabilité clairement définie.

    
    Vers la fin du développement, il est nécessaire de mettre en place un Gateway qui joue le rôle de réception des requêtes et d'équilibrage de charge à l'aide d'un load balancer. De plus, nous avons également besoin d'un Discovery Service qui enregistre les services et permet de les identifier par leur nom, tout en fournissant leur adresse au gateway. Enfin, un Config Service est requis pour créer un référentiel contenant le fichier application.properties où seront regroupées toutes les propriétés communes entre les services.

![image](https://user-images.githubusercontent.com/48890714/230246863-4872eb6e-7dc3-4a30-a5d2-aea6e1be4d8d.png)

    
    
## Exigences d'un projet informatique

Chaque projet informatique à deux types des exigences :
 - ```Exigences Fonctionnelle``` sont les besoins fonctionnelles , les attentes du utilisateurs finale ou bien les besoins métiers de l'entreprise.
 - ```Exigences Techniques``` sont les besoins techniques quand peut résumer dans les points suivants :
    - La performance : ce que concerne le temps de réponse de l'application, le problème de montée en charge, qu'est peut-être résolue par la scalabilité, cette dérnière peut prendre deux formes:
        - Horizontale : se caractérise par l’équilibrage de charge et la tolérance aux pannes, cette scalabilité conciste sur le démarrage de l’application en  plusieurs instances dans différentes machines pour palier au problème de montée en charge, avec un serveur Load Balencer pour l’équilibrage de charge des requete reçu de chez les clients.
        - Verticale : c'est la technique qui permet d'augmenter les resources de la machine ou est exécuté une application, par resources on entend par là, la RAM, le disque dure, le processeur/CPU etc. l'application va créer un thread pour chaque requete d'un client.
    
      ![image](https://user-images.githubusercontent.com/48890714/230366076-441c472a-7ff6-40f6-9a5e-6a4e699fd221.png)
    
        -La maintenance : Il est essentiel que l'application puisse évoluer dans le temps, tout en respectant le principe selon lequel une application doit être fermée aux modifications et ouverte à l'extension. Cela permet de minimiser les problèmes de régression qui peuvent survenir lors des modifications apportées à l'application.

        -La sécurité : Les failles de sécurité représentent l'un des problèmes les plus critiques dans le développement informatique. Il est primordial de porter une attention particulière à la sécurité des données et à la gestion des transactions afin de garantir la protection de l'application contre les menaces potentielles.

        -Les versions : Il existe différentes versions d'une application selon les plateformes, telles que web, mobile et desktop. Chaque version peut nécessiter des adaptations spécifiques pour répondre aux besoins et aux contraintes de la plateforme cible. Il est donc important de prendre en compte ces différences lors du développement et de la maintenance de l'application.

## Inversion de contrôle
L'inversion de contrôle est un principe qui vise à séparer les aspects métier des aspects techniques dans le développement logiciel. Les développeurs se concentrent uniquement sur la partie du code liée au métier, tandis que le framework se charge des aspects techniques. Ce principe s'appuie sur l'architecture de programmation orientée aspect (AOP).

```
La programmation orientée aspect (AOP) complète la programmation orientée objet (POO) en offrant une autre façon de penser la structure du programme. L'unité clé de la modularité en POO est la classe, alors qu'en AOP l'unité de modularité est l' aspect . 
Les aspects permettent la modularisation de préoccupations telles que la gestion des transactions qui couvrent plusieurs types et objets. (Ces préoccupations sont souvent qualifiées de préoccupations transversales dans la littérature AOP.)
```
## Injection des dépandances

L'injection de dépendances se divise en deux types :

    -Couplage fort : Dans ce cas, les classes dépendent directement d'autres classes, ce qui rend les modifications difficiles car toute modification nécessite une modification du code source.
    -Couplage faible : En revanche, les classes dépendent d'interfaces plutôt que d'autres classes spécifiques. Ce type de couplage permet une plus grande flexibilité lors des modifications, car il est possible de substituer les dépendances par d'autres implémentations en respectant les contrats définis par les interfaces.
 
 ## Spring IOC
Il est préférable d'utiliser un framework pour la liaison des composants d'un programme, car cela permet de changer facilement les composants ou leur comportement.

Dans le cas de Spring IOC, le framework utilise un fichier XML de configuration pour déclarer les différentes classes à instancier et gérer les dépendances entre ces instances. Ainsi, lorsqu'une nouvelle implémentation est ajoutée à l'application, elle peut être déclarée dans le fichier XML de Spring.
 Il existe deux méthodes pour effectuer cette liaison :

    -```Méthode XML``` : Spring lit le fichier XML de configuration, puis se charge des injections de dépendances en fonction des déclarations du fichier.

    - ```Méthode par annotations``` : Des annotations sont ajoutées aux classes pour indiquer à Spring qu'il doit les instancier lors du démarrage de l'application. De plus, ces annotations permettent de spécifier les dépendances entre les instances des différentes classes qui ont déjà été déclarées.
 

## Architecture de probleme
## La couche DAO 
Cette couche est responsable de l'interrogation de la base de données.
- La première étape consiste à créer une interface qui contient les méthodes nécessaires dans la couche DAO :
``` java
public interface IDao {
     double getdate();
}
```
- Ensuite, il faut créer une classe d'implémentation de l'interface DAO, où l'on va redéfinir la méthode getData.

``` java 
public class DaoImpl implements IDao{

    @Override
    public double getdate() {
        /*
         se connecter à la base de données pour récupérer la température
         */
        System.out.println("Version Base de données");
        double date = Math.random() * 50;
        return date;
    }
}
```
Dans le cas de modifications, on ajoute un autre classe qui va implémenter l'interface DAO sans besoin de modifier le code source. à ce stade la, on applique la régle ```l’application doit être fermée à la modification et ouverte à l’extension```.
## La couche metier
Cette couche continet les besoins fonctionnels de l'application.
- La première étape : Création d'interface contient la métode qu'on aura besoin dans la couche Metier, cette méthode va faire le traitement de besoin fonctionnel du utilisateur.
``` java
public interface Imetier {
    double calcul();
}
```
- La deuxieme étape : Création de classe d'implémentation de interface Metier, dans la quelle on va redéfini la méthode 'calcul', avec la déclaration d'un objet de type d'interface 'DAO' en utilisant un couplage faible.

``` java 
public class MetierImpl implements Imetier {
    private IDao dao=null; //coublage faible
    @Override
    public double calcul() {
        double tmp=dao.getdate();
        double resultat =tmp*420 / Math.tan(tmp*Math.PI);
        return resultat;
    }
   /*
    Inejecter dans la variable dao  un object une classe qui implémente l'interface IDao
    */
    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```
## La couche présentation 
Cette couche représente la partie présentation de l'interface utilisateur (UI).

Pour assurer la communication entre la classe Métier et la couche DAO, il est nécessaire d'effectuer une injection de dépendances. Il existe deux méthodes pour réaliser cette injection : 
### Instanciation Statique
Dans la couche présentation, on crée une classe ```Presenatation.java``` ou on appliquera une injection statique des dépendances. il joue le rôle de Factory Class, elle génère les dépendances.

``` java
public class Presentation {
    public static void main(String[] args) throws  Exception{
        /*
         Injection des dépendances par instanciation statique => new  (couplage forte)
         */
        IDao dao=new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        System.out.println("Résultat :"+metier.calcul());
        
    }
}
```
### Instatnciation Dynamique
Si on veut respecter la régle ```l’application doit être fermée à la modification et ouverte à l’extension```, avec la méhtode statique dans le cas d'une nouvelle extension nous obligons à modifer le code. par contre avec la méthode dynamique nous faisons seulement l'injection des dépendances sans changement de code. une simple modification au niveau de fichier ```config.txt```

``` java
public class Presentation2 {
    // les exception a connaitre FileNotFoundException,ClassNotFoundException,InstantiationException,IllegalAccessException,ClassCastException

    public static void main(String[] args) throws  Exception{
         /*
        Injection des dependances par instanciation dynamique => Fichier de configuration
         */
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClasseName= scanner.nextLine();
        Class cDao = Class.forName(daoClasseName);
        cDao.newInstance(); // creation instance , appelle au constructeur par défaut.
        IDao iDao=(IDao) cDao.newInstance();
        System.out.println("Résultat :" + iDao.getdate());

        String metierClasseName= scanner.nextLine();
        Class cMetier = Class.forName(metierClasseName);
        cMetier.newInstance(); // creation instance , ppelle au constructeur par défaut.
        Imetier iMetier=(Imetier) cMetier.newInstance();

        Method method = cMetier.getMethod("setDao", IDao.class);
        method.invoke(iMetier,iDao); // la meme chose que metier.setDao(dao);

        System.out.println("Résultat :" + iMetier.calcul());

    }
}
```

## Le dossier ext 
Ce dossier contient la partie d'extension de l'application.
à la maintenace on a respecté la régle d'or ```l’application doit être fermée à la modification et ouverte à l’extension``` et on a ajouté un autre dossier pour déclarer des nouvelles implémentations de l'interface IDao.
``` java
public class DaoImplVWeb implements IDao {
    @Override
    public double getdate() {
        System.out.println("Version web Service");
        return 90;
    }
}
```
une autre implémentation de l'interface IDao dans la partie d'extension :
``` java
public class MetierImplVWeb implements Imetier {
    IDao dao =null;
    @Override
    public double calcul() {
        System.out.println("Version web Service");
        return 203;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```
## Fichier config.txt
Ce fichier contient la configuration pour l'injection des dépendances dynamiquement.
il contient les noms des différentes implémentations déja déclarés qu'on va l'utiliser pour l'injection des dépandances d'une façon dynamique sans passer par l'instanciation des objets en utilisant mot cle ```new```.

``` txt 
ma.enset.ext.DaoImplVWeb
ma.enset.ext.MetierImplVWeb
```

## FrameWork Spring 
### Les dépandances ```pom.xml```
Dans «External librairies» on a téléchargé 3 jars :
1. Spring core
2. Spring context
3. Spring beans
Ces jars vont être utilisés par Spring.
On utilisant Spring pour injecter les dépendances automatiquement dans le fichier ```pom.xml```: 
``` xml
<dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>5.3.20</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.16</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>5.3.18</version>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
 
 ### XML
 ``` java
 public class PresentationSpringXML {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Imetier imetier =(Imetier) context.getBean("metier");
        System.out.println(imetier.calcul());
        IDao iDao = (IDao) context.getBean("dao");
        System.out.println(iDao.getdate());
    }
}
```
La structure de fichier ```applicationContext.xml```
Le fichier xml de configuration de spring, dans la balise « beans » on déclare les instances qu’on a besoin avec ‘id’ est le nom de chaque instance, et ‘class’ le nom de la classe, et pour injecter la dépendance il y a la balise « property » avec un ‘name’ nom de l’objet et ‘ref’ la référence vers quelle instance :
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="dao" class="ma.enset.ext.DaoImplVWeb"></bean>
    <bean id="metier" class="ma.enset.metier.MetierImpl">
        <property name="dao" ref="dao"></property>
        <!--constructor-arg ref="dao"></constructor-arg-->
    </bean>
</beans>
```
### Annotation
``` java
public class PresSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("ma.enset.dao","ma.enset.metier","ma.enset.ext");
        Imetier imetier = (Imetier) context.getBean("metier");
        System.out.println(imetier.calcul());
        IDao iDao = (IDao) context.getBean("dao");
        System.out.println(iDao.getdate());
    }
}
```
## Maven 
Maven est un outil qui facilite l'automatisation des processus de développement d'un projet Java. Il ne s'agit pas d'un framework, mais plutôt d'un système de gestion de projet. Maven utilise un paradigme appelé POM (Project Object Model) pour décrire et configurer les dépendances, les plugins, les paramètres de construction et d'autres aspects du projet.
Principe : à chaque fois on ajoute une dépendance au fichier xml ```pom.xml```, il va  chercher dans le ```repository local``` s’il en trouve il va l'utiliser, sinon il va se connecter à l’internet et il va télécharger les dépendances déclarées.
### Les commandes de Maven
- ```mvn compile``` ->  compile le code source du projet.
- ```mvn test``` ->  parcourir le projet et à chaque fois il trouve un test unitaire il va l’exécuter, puis il montre qui sont les tests réussit et qui ne sont pas.
- ```mvn package``` ->  exécute la commande ```mvn compile``` et ```mvn test``` puis archive le projet maven dans archive (.jar / .war).
- ```mvn install``` ->  exécute la commande ```mvn compile``` et ```mvn test``` puis il installe le projet dans le repository local pour l’utiliser au cas de besoin.
- ```mvn deploy``` ->  déployer un projet vers un serveur.
- ```mvn site``` ->  générer un site de documentation.

Dans cette partie, nous avons étudier l'injection des dépendance et l'inversion de contrôle avec instation dynamique et statique, Sprig XML et annotation et finalement avec le framework Spring (XML et Annotation).
