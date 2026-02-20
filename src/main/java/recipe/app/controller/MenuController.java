package recipe.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import recipe.app.dto.MenuResultDto;
import recipe.app.service.MenuService;

import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public List<MenuResultDto> getAllMenu() {
        return menuService.getAllMenu();
    }
}
