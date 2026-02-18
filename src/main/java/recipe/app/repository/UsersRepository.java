package recipe.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import recipe.app.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsernameAndDeleteFlg(String username, Integer deleteFlg);
}
