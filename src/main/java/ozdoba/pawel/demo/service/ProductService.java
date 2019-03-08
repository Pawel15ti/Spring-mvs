package ozdoba.pawel.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ozdoba.pawel.demo.model.Product;
import ozdoba.pawel.demo.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService implements Comparator<Product> {

    static List<Product> productList;
    private ProductRepository productRepository;

    @Autowired
    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//    public List<Product> getProducts1() {
//        productList =  iterable.forEach((productRepository.findAll());
//        productList = new LinkedList<>(productRepository.findAll());
//        productList.stream().filter(s -> s.getIdSort() == null).map(s -> setIdSort(s)).collect(Collectors.toList());
//        productRepository.saveAll(productList);
//        Collections.sort(productList, Comparator.comparing(Product::getIdSort).thenComparing(Product::getId));
//        return productList;
//    }

    private Product setIdSort(Product product) {
        product.setIdSort(product.getId());
        return product;
    }

    public List<Product> getProducts() {
        productList = new LinkedList<>(productRepository.findAll());
        productList.stream().filter(s -> s.getIdSort() == null).map(s -> setIdSort(s)).collect(Collectors.toList());
        productRepository.saveAll(productList);
        Collections.sort(productList, Comparator.comparing(Product::getIdSort).thenComparing(Product::getId));
        return productList;

    }

    public void saveProduct(Product product) {

        productRepository.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        log.info("rozpoczęcie w serwisie metody: findProductById id={} ",id);
        Optional<Product> productFindById = productRepository.findById(id);
        return productFindById;
    }

//    public List<Product> changePosition(Long id, Short oper) {
//
//        Product productFindById = productRepository.findById(id).get();
//        getProducts().
//                stream().
//                filter(s -> s.getIdSort() < productFindById.getIdSort()).
//                forEach(s -> {
//                    s.setIdSort(s.getIdSort() + 1);
//                    productRepository.save(s);
//                });
//
//        productFindById.setIdSort(new Long(1));
//        productRepository.save(productFindById);
//
//        return getProducts();
//    }

//    public List<Product> changePositionToLast(Long id, Short oper) {
//
//        Product productFindById = productRepository.findById(id).get();
//        getProducts().
//                stream().
//                filter(s -> s.getIdSort() > productFindById.getIdSort()).
//                forEach(s -> {
//                    s.setIdSort(s.getIdSort() - 1);
//                    productRepository.save(s);
//                });
//        productFindById.setIdSort(new Long(getProducts().size() - 1));
//        productRepository.save(productFindById);
//
//        return getProducts();
//    }

//    public List<Product> changePositionToLower(Long id, Short oper) {
//        Product productFindById = productRepository.findById(id).get();
//        Product product = getProducts().stream().filter(s -> s.getIdSort().equals(productFindById.getIdSort() + 1)).findFirst().map(s -> convert(s)).get();
//        productFindById.setIdSort(productFindById.getIdSort() + 1);
//        productRepository.save(productFindById);
//        productRepository.save(product);
//
//        return getProducts();
//    }

    @Transactional
    public List<Product> changePositionOnFirst(Long id, List<Product> productList) {
        log.info("rozpoczęcie w serwisie metody: changePositionOnFirst id={} ",id);
        if (productList.isEmpty()) {
            return null;
        }

        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();
        Product findFirstProduct = productRepository.findFirstByOrderByIdSortAsc();
        tmpIdSort = productFindById.getIdSort();
        List<Product> byIdSortLessThanAndIdSortGreaterThan = productRepository.findByIdSortLessThanAndIdSortGreaterThanEqual(tmpIdSort, findFirstProduct.getIdSort());
        byIdSortLessThanAndIdSortGreaterThan.stream().forEach(s ->
                {
                    s.setIdSort(s.getIdSort() + 1);
                    productRepository.save(s);
                }
        );
        productFindById.setIdSort(productList.get(0).getIdSort());
        productRepository.save(productFindById);
        log.info("zakończenie w serwisie metody: changePositionOnFirst");
        return getProducts();
    }

    @Transactional
    public List<Product> changePositionOnLast(Long id, List<Product> productList) {
        log.info("rozpoczęcie w serwisie metody: changePositionOnLast id={} ",id);
        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).orElseGet(Product::new);
        if (productFindById.getId() == null) {
            return getProducts();
        }
        Product findFirstProduct = productRepository.findFirstByOrderByIdSortDesc();
        tmpIdSort = productFindById.getIdSort();
        List<Product> byIdSortLessThanAndIdSortGreaterThan = productRepository.findByIdSortGreaterThanAndIdSortLessThanEqual(tmpIdSort, findFirstProduct.getIdSort());
        byIdSortLessThanAndIdSortGreaterThan.stream().forEach(s ->
                {
                    s.setIdSort(s.getIdSort() - 1);
                    productRepository.save(s);
                }
        );
        productFindById.setIdSort((long) productList.get(productList.size() - 1).getIdSort());
        productRepository.save(productFindById);
        log.info("zakończenie w serwisie metody: changePositionOnLast");
        return getProducts();
    }

    @Override
    public int compare(Product o1, Product o2) {
        Long first = o1.getIdSort();
        Long second = o2.getIdSort();
        if (first < second) {
            return -1;
        } else if (first > second) {
            return 1;
        }
        return 0;
    }


    public List<Product> changePositionOnAbove(Long id, List<Product> productList) {
        log.info("rozpoczęcie w serwisie metody: changePositionOnAbove id={} ",id);

        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();
        tmpIdSort = productFindById.getIdSort();
        Product findFirstOnBelow = productRepository.findFirstByIdSortLessThanOrderByIdSortDesc(tmpIdSort);
        if (findFirstOnBelow == null) {
            return getProducts();
        }
        log.info("current object: name: {}, idSort: {} " + " object on first above: name: {}, idSort: {} ", productFindById.getName(), productFindById.getIdSort(), findFirstOnBelow.getName(), findFirstOnBelow.getIdSort());
        productFindById.setIdSort(findFirstOnBelow.getIdSort());
        findFirstOnBelow.setIdSort(tmpIdSort);
        productRepository.save(productFindById);
        productRepository.save(findFirstOnBelow);
        log.info("zakończenie w serwisie metody: changePositionOnAbove");
        return getProducts();
    }

    public List<Product> changePositionOnBelow(Long id, List<Product> productList) {
        log.info("rozpoczęcie w serwisie metody: changePositionOnBelow id={} ",id);
        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();
        tmpIdSort = productFindById.getIdSort();
        Product findFirstOnAbove = productRepository.findFirstByIdSortGreaterThanOrderByIdSortAsc(tmpIdSort);
        if (findFirstOnAbove == null) {
            return getProducts();
        }
        log.info("current object: name: {}, idSort: {} " + " object on first above: name: {}, idSort: {} ", productFindById.getName(), productFindById.getIdSort(), findFirstOnAbove.getName(), findFirstOnAbove.getIdSort());
        productFindById.setIdSort(findFirstOnAbove.getIdSort());
        findFirstOnAbove.setIdSort(tmpIdSort);
        productRepository.save(productFindById);
        productRepository.save(findFirstOnAbove);
        log.info("zakończenie w serwisie metody: changePositionOnBelow");
        return getProducts();
    }
}
