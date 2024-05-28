package jar.StockValueApp.repository;


import jar.StockValueApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves an {@link Optional} containing a {@link User} entity identified by the given username,
     * utilizing Spring Boot's repository mechanism to avoid explicit SQL queries.
     * <p>
     * By adhering to Spring Data JPA's method naming conventions, this method enables the automatic generation
     * of the underlying SQL query required to fetch a user by their username. Spring Boot handles the creation
     * and execution of the query, eliminating the need for manual SQL coding and reducing the potential for
     * errors related to query syntax or execution. The use of {@link Optional} as the return type enhances
     * the method's robustness, gracefully handling cases where no user matches the provided username by returning
     * an empty {@link Optional}, thereby avoiding {@code null} checks in the calling code.
     * </p>
     *
     * @param userName The username of the user to be retrieved; must not be {@code null}. The username is
     *                 expected to uniquely identify a user within the database.
     * @return An {@link Optional} instance containing the retrieved {@link User} entity if a user with the
     * specified username exists, or an empty {@link Optional} if no matching user is found.
     */
    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

}
