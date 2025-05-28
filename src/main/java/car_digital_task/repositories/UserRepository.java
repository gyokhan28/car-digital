package car_digital_task.repositories;

import car_digital_task.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u ORDER BY u.lastName ASC, u.birthDate ASC")
    List<User> findAllSorted();

    @Query("""
    SELECT u FROM User u 
    WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%')) 
       OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
    ORDER BY u.lastName ASC, u.birthDate ASC
""")
    List<User> findAllBySearch(@Param("search") String search);

}
