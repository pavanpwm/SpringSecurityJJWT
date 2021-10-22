package sec.eci.poc.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POCUserRepo extends CrudRepository<POCUser, Integer>{
	
	POCUser findByUsername(String username);



}
