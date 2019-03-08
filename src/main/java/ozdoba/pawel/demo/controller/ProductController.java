package ozdoba.pawel.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ozdoba.pawel.demo.dto.ProductDto;
import ozdoba.pawel.demo.model.Product;
import ozdoba.pawel.demo.repository.ProductRepository;
import ozdoba.pawel.demo.service.ProductService;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Controller
@Slf4j
public class ProductController {

    static List<Product> productList = new LinkedList<>();
    private ProductService productService;


    @Autowired
    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(
            path = "/products"
    )
    public String getProducts(Model model) {
        productList = productService.getProducts();
        if (productList.isEmpty()) {
            model.addAttribute("message", "Brak elementów");
            log.warn("Brak elementów w bazie");
            return "index";
        }
        model.addAttribute("products", productList);
        return "index";
    }

    @RequestMapping(
            path = "/productsadd"
    )
    public String createProduct(Model model) {
        model.addAttribute("product", new ProductDto());
        return "productform";
    }

    @RequestMapping(
            path = "/products",
            method = RequestMethod.POST
    )
    public String saveProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Błąd walidacji nazwy produktu");
            return "productform";
        } else {
            productService.saveProduct(product);
            return "redirect:products";
        }
    }

    @RequestMapping(
            path = "/product",
            method = RequestMethod.GET
    )
    public String showProduct(@RequestParam(name = "id") Long id, Model model) {
        Optional<Product> productById = productService.findProductById(id);
        if (!productById.isPresent()) {
            model.addAttribute("message", "Nie stworzono produktu");
            return "index";
        }
        model.addAttribute("product", productById.get());
        return "product";


    }

    @RequestMapping(
            path = "/move/{id}"
    )
    public String changePosition(@PathVariable("id") Long id, @RequestParam(name = "oper") Short oper, RedirectAttributes redirectAttributes, Model model) {

        List<Product> products;
        if (oper == 1) {
            products = productService.changePositionOnFirst(id, productList);
        } else if (oper == 2) {
            products = productService.changePositionOnAbove(id, productList);
        } else if (oper == 3) {
            products = productService.changePositionOnBelow(id, productList);
        } else if (oper == 4) {
            products = productService.changePositionOnLast(id, productList);
        } else {
            products = new LinkedList<>();
        }
        if (products == null || products.isEmpty()) {
            log.warn("Brak elementów w bazie");
            model.addAttribute("message", "Brak elementów");
            return "index";
        }
        model.addAttribute("products", products);
        return "index";
    }


}
