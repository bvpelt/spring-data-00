package hello;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	private static int PAGE_SIZE = 10;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner demo00(CustomerRepository repository) {
		return (args) -> {
			log.info("Start demo00");

			// save a couple of customers
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Customer customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Customer customer = repository.findOne(1L);
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			for (Customer bauer : repository.findByLastName("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");

			// count all customers
			log.info("Customer count:");
			log.info("--------------------------------------------");
			log.info("Customer count (expect 5):" + repository.count());
			log.info("");

			// Test on existance bij ID
			log.info("Customer with ID 1L exists:");
			log.info("--------------------------------------------");
			boolean exists = repository.exists(1L);
			log.info("exists (expect true): " + exists);
			log.info("");

			// Delete Customer with ID 1L
			log.info("Delete Customer with ID 1L:");
			log.info("--------------------------------------------");
			repository.delete(1L);
			exists = repository.exists(1L);
			log.info("exists (expect false): " + exists);
			log.info("");

			// count all customers
			log.info("Customer count:");
			log.info("--------------------------------------------");
			log.info("Customer count (expect 4):" + repository.count());
			log.info("");

			// remove all customers
			log.info("Remove all Customers findAll():");
			log.info("-------------------------------");
			for (Customer customer01 : repository.findAll()) {
				log.info(customer01.toString());
				repository.delete(customer01);
			}
			log.info("Customer count (expect 0):" + repository.count());
			log.info("");

		};
	}

	public Page<Customer> getSortedCustomer(Integer pageNumber, WebRepository repository) {
		PageRequest pageRequest = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.ASC, "firstName", "id");
		return repository.findAll(pageRequest);
	}

	@Bean
	public CommandLineRunner demo01(WebRepository repository) {
		return (args) -> {
			log.info("Start demo01");
			// fill database with a couple of customers
			for (int i = 0; i < 100; i++) {
				repository.save(new Customer("Jack_" + i, "Bauer_" + i));
				repository.save(new Customer("Chloe_" + i, "O'Brian_" + i));
				repository.save(new Customer("Kim_" + i, "Bauer_" + i));
				repository.save(new Customer("David_" + i, "Palmer_" + i));
				repository.save(new Customer("Michelle_" + i, "Dessler_" + i));
			}

			// retrieve all customers paged from database, starting with page 1 until no customers left
			int page = 1;
			Page<Customer> customer = getSortedCustomer(page, repository);
						
			while (customer.hasContent()) {
				log.info("Start page: " + page);
				List<Customer> clist = customer.getContent();
				for (Customer c : clist) {
					log.info(c.toString());
				}
				log.info("End   page: " + page);
				customer = getSortedCustomer(page++, repository);
			}						
		};
	}
}