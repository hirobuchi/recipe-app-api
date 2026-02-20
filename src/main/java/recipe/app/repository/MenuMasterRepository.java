package recipe.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import recipe.app.dto.MenuResultDto;
import recipe.app.entity.MenuMaster;

import java.util.List;

public interface MenuMasterRepository extends JpaRepository<MenuMaster, Long> {

    @Query("SELECT new recipe.app.dto.MenuResultDto(m.menu_name, m.menu_api, m.menu_description) FROM MenuMaster m ORDER BY m.menu_code")
    List<MenuResultDto> getMenuResultOrderByMenuCode();
}
