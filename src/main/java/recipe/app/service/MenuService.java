package recipe.app.service;

import org.springframework.stereotype.Service;
import recipe.app.dto.MenuResultDto;
import recipe.app.repository.MenuMasterRepository;

import java.util.List;

@Service
public class MenuService {

    private final MenuMasterRepository menuMasterRepository;

    public MenuService(MenuMasterRepository menuMasterRepository) {
        this.menuMasterRepository = menuMasterRepository;
    }

    public List<MenuResultDto> getAllMenu(){
        return menuMasterRepository.getMenuResultOrderByMenuCode();
    }
}
