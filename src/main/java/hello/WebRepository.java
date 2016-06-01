package hello;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface WebRepository extends PagingAndSortingRepository<Customer, Long> {

	 List<Customer> findByLastName(String lastName);
}
