package ma.enset.jpaenset;

import ma.enset.jpaenset.entities.Role;
import ma.enset.jpaenset.entities.User;
import ma.enset.jpaenset.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class JpaEnsetApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaEnsetApplication.class, args);
	}
	@Bean
    CommandLineRunner start(UserService userService){
		return args -> {
			User u=new User();
			u.setUsername("user1");
			u.setPassword("123456");
             userService.addNewUser(u);

			User u2=new User();
			u2.setUsername("admin");
			u2.setPassword("123456");
			userService.addNewUser(u2);

			Stream.of("STUDENT","USER","ADMIN").forEach(r->{
				Role role1=new Role();
				role1.setRoleName(r);
				userService.addNewRole(role1);
			});

			userService.addRoleToUser("user1","STUDENT");
			userService.addRoleToUser("user1","USER");
			userService.addRoleToUser("admin","USER");
			userService.addRoleToUser("admin","ADMIN");

            try {
				User users=userService.authenticate("user1","123456");
				System.out.println(users.getUserId());
				System.out.println(users.getUsername());
				System.out.println("Roles=>");
				users.getRoles().forEach(r->{
					System.out.println("Role=>"+r.toString() );
				});
			}
			catch (Exception e){
				e.printStackTrace();
			}
		};
	}
}
