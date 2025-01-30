package com.rt_chatApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class RtChatAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtChatAppApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner commandLineRunner(
//			AuthenticationService service,
//			UsernameGenRepository repository
//	) {
//		if (!service.userExists("admin@mail.com") && !service.userExists("manager@mail.com")) {
//			return args -> {
//				var admin = RegisterRequest.builder()
//						.firstname("Admin")
//						.lastname("Admin")
//						.email("admin@mail.com")
//						.password("password")
//						.role(ADMIN)
//						.build();
//				System.out.println("Admin token: " + service.register(admin).getAccessToken());
//
//				var manager = RegisterRequest.builder()
//						.firstname("Admin")
//						.lastname("Admin")
//						.email("manager@mail.com")
//						.password("password")
//						.role(MANAGER)
//						.build();
//				System.out.println("Manager token: " + service.register(manager).getAccessToken());
//			};
//		}
//		return null;
//	}
}
