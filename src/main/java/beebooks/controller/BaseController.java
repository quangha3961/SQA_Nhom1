package beebooks.controller;

import beebooks.entities.*;
import beebooks.service.*;
import beebooks.service.impl.CategoriesBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class BaseController {

    @Autowired
    private CategoriesService categoriesService;
    @Autowired
    private CategoriesBlogService categoriesBlogService;
    @Autowired
    private SaleorderService saleorderService;
    @Autowired
    private ManufacturerService manufacturerService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private PromotionService promotionService;

    @ModelAttribute("isLogined")
    public boolean isLogined() {
        boolean isLogined = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            isLogined = true;
        }
        return isLogined;
    }

    @ModelAttribute("userLogined")
    public User getUserLogined() {
        Object userLogined = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userLogined != null && userLogined instanceof UserDetails)
            return (User) userLogined;

        return null;
    }

    @ModelAttribute("categories")
    public List<Categories> getAllCategories() {
        return categoriesService.findAll();
    }
    @ModelAttribute("manufacturer")
    public List<Manufacturer> getAllManufacturer() {
        return manufacturerService.findAll();
    }
    @ModelAttribute("author")
    public List<Author> getAllAuthor() {
        return authorService.findAll();
    }
    @ModelAttribute("promotion")
    public List<Promotion> getAllPromotion() {
        return promotionService.findAll();
    }

    @ModelAttribute("categoriesBlog")
    public List<CategoriesBlog> getAllCategoriesBlog() {
        return categoriesBlogService.findAll();
    }

    @ModelAttribute("saleOrder")
    public List<Saleorder> getAllSaleOrder() {
        return saleorderService.findAll();
    }


    @ModelAttribute("menus")
    public String getCategories() {
        return this.buildMenu();
    }

    private String buildMenu() {
        StringBuilder menu = new StringBuilder();
        return menu.toString();

    }


    public int getCurrentPage(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            return 1;
        }
    }


    public Integer getInteger(HttpServletRequest request, String paramName) {
        try {
            return Integer.parseInt(request.getParameter(paramName));
        } catch (Exception e) {
            return null;
        }
    }


}
