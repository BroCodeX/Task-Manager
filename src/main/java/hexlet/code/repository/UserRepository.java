package hexlet.code.repository;

import hexlet.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstName(String firstName);
    Optional<User> findByLastName(String lastName);
    Optional<User> findByEmail(String email);
}
