package ing.bank.app.repositories;

import ing.bank.app.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}
