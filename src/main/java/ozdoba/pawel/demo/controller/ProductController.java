package ozdoba.pawel.demo.controller;

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
public class ProductController {

    static List<Product> productList = new LinkedList<>();

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @RequestMapping(
            path = "/products"
    )

    public String getProducts(Model model) {
        productList = productService.getProducts();

//        productList.forEach(s -> System.out.println(s));
//        ozdoba.pawel.app.model.dto.Product product1 = new ozdoba.pawel.app.model.dto.Product(productList.get(0).getId(), productList.get(0).getName());
//        ozdoba.pawel.app.model.dto.Product product2 = new ozdoba.pawel.app.model.dto.Product(all.get(1).getId(), all.get(1).getName());

//        List<ozdoba.pawel.app.model.dto.Product> lists = new ArrayList<>();
//
//        lists.add(product1);
//        lists.add(product2);
//        model.addAttribute("product",product);
        model.addAttribute("products", productList);
        return "index";


    }

    @RequestMapping(
            path = "/productsadd"

    )
    public String createProduct( Model model) {

        model.addAttribute("product", new ProductDto());
        return "productform";
    }

    @RequestMapping(
            path = "/products",
            method = RequestMethod.POST
    )

    public String saveProduct(@Valid Product product, BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            System.out.println("There were errors");
            return "productform";
        }else {
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
        if (productById.isPresent()) {
            model.addAttribute("product", productById.get());
        }

        return "product";


    }

    @RequestMapping(
            path = "/move/{id}"
//            method = RequestMethod.GET
//            ,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
//            method = RequestMethod.GET
    )

    public String changePosition(@PathVariable("id") Long id, @RequestParam(name = "oper") Short oper, RedirectAttributes redirectAttributes, Model model) {

        List<Product> products1;
        if(oper == 1) {
            products1 = productService.changePositionOnFirst(id, productList);
        }else if(oper == 2){
            products1 = productService.changePositionOnAbove(id, productList);
        }else if(oper == 3){
            products1 = productService.changePositionOnBelow(id, productList);
        }else if(oper == 4){
             products1 = productService.changePositionOnLast(id, productList);
        }else{
            products1 = new LinkedList<>();
        }
//        if (productById.isPresent()) {
//            model.addAttribute("product", productById.get());
//        }


        model.addAttribute("products", products1);

//        redirectAttributes.addFlashAttribute("show", false);
//        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        return "index";


    }


}
