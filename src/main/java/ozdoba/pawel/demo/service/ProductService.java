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

    @Autowired
    ProductRepository productRepository;
    static List<Product> productList;


    //    @PostConstruct
    public List<Product> getProducts1() {
        productList = new LinkedList<>(productRepository.findAll());
        productList.stream().filter(s -> s.getIdSort() == null).map(s -> setIdSort(s)).collect(Collectors.toList());
        productRepository.saveAll(productList);
        Collections.sort(productList, Comparator.comparing(Product::getIdSort).thenComparing(Product::getId));
        return productList;

    }

    private Product setIdSort(Product product) {
        product.setIdSort(product.getId());
        return product;
    }

    public List<Product> getProducts() {
//        return productRepository.findAll();
        return getProducts1();

    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        Optional<Product> productFindById = productRepository.findById(id);
        return productFindById;
    }

    public List<Product> changePosition(Long id, Short oper) {

        Product productFindById = productRepository.findById(id).get();
        getProducts1().
                stream().
                filter(s -> s.getIdSort() < productFindById.getIdSort()).
                forEach(s -> {
                    s.setIdSort(s.getIdSort() + 1);
                    productRepository.save(s);
                });

        productFindById.setIdSort(new Long(1));
        productRepository.save(productFindById);

        return getProducts1();
    }

    public List<Product> changePositionToLast(Long id, Short oper) {

        Product productFindById = productRepository.findById(id).get();
        getProducts1().
                stream().
                filter(s -> s.getIdSort() > productFindById.getIdSort()).
                forEach(s -> {
                    s.setIdSort(s.getIdSort() - 1);
                    productRepository.save(s);
                });

        productFindById.setIdSort(new Long(getProducts1().size() - 1));
        productRepository.save(productFindById);

        return getProducts1();
    }

    public List<Product> changePositionToLower(Long id, Short oper) {
        Product productFindById = productRepository.findById(id).get();
//        getProducts1().
//                stream().
//                filter(s -> s.getIdSort() < productFindById.getIdSort()).
//                forEach(s -> {
//                    s.setIdSort(s.getIdSort() +1);
//                    productRepository.save(s);
//                });

        Product product = getProducts1().stream().filter(s -> s.getIdSort().equals(productFindById.getIdSort() + 1)).findFirst().map(s -> convert(s)).get();
        productFindById.setIdSort(productFindById.getIdSort() + 1);

        productRepository.save(productFindById);
        productRepository.save(product);

        return getProducts1();
    }

    @Transactional
    public List<Product> changePositionOnFirst(Long id, List<Product> productList) {

        if(productList.isEmpty()){
            return null;
        }

        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();
        Product findFirstProduct = productRepository.findFirstByOrderByIdSortAsc();

        tmpIdSort = productFindById.getIdSort();



        List<Product> byIdSortLessThanAndIdSortGreaterThan = productRepository.findByIdSortLessThanAndIdSortGreaterThanEqual( tmpIdSort, findFirstProduct.getIdSort());

        byIdSortLessThanAndIdSortGreaterThan.stream().forEach(s->
        {
            s.setIdSort(s.getIdSort() +1);
            productRepository.save(s);

        }
        );
        productFindById.setIdSort(productList.get(0).getIdSort());
        productRepository.save(productFindById);

//        getProducts1().
//                stream().
//                filter(s -> s.getIdSort() < productFindById.getIdSort()).
//                forEach(s -> {
//                    s.setIdSort(s.getIdSort() +1);
//                    productRepository.save(s);
//                });



//        Product topByIdSortOrderByIdSort2 = productRepository.findFirstByOrderByIdSortAsc();
//        List<Product> topByIdSortOrderByIdSort3 = productRepository.findByIdSortLessThanAndIdSortGreaterThan(productFindById.getIdSort(), topByIdSortOrderByIdSort2.getIdSort());
//        List<Product> topByIdSortOrderByIdSort4 = productRepository.findByIdSortGreaterThanAndIdSortLessThan(productFindById.getIdSort(), topByIdSortOrderByIdSort.getIdSort());
//        Product product = getProducts1().stream().filter(s -> s.getIdSort().equals(productFindById.getIdSort() - 1)).findFirst().map(s -> convertWDol(s)).get();
//        productFindById.setIdSort(productFindById.getIdSort() - 1);
//
//        productRepository.save(productFindById);
//        productRepository.save(product);

        return getProducts1();
    }

    @Transactional
    public List<Product> changePositionOnLast(Long id, List<Product> productList) {

        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();
        Product findFirstProduct = productRepository.findFirstByOrderByIdSortDesc();

        tmpIdSort = productFindById.getIdSort();



        List<Product> byIdSortLessThanAndIdSortGreaterThan = productRepository.findByIdSortGreaterThanAndIdSortLessThanEqual( tmpIdSort, findFirstProduct.getIdSort());

        byIdSortLessThanAndIdSortGreaterThan.stream().forEach(s->
                {
                    s.setIdSort(s.getIdSort() -1);
                    productRepository.save(s);

                }
        );
        productFindById.setIdSort((long) productList.get(productList.size() -1).getIdSort());
        productRepository.save(productFindById);

//        getProducts1().
//                stream().
//                filter(s -> s.getIdSort() < productFindById.getIdSort()).
//                forEach(s -> {
//                    s.setIdSort(s.getIdSort() +1);
//                    productRepository.save(s);
//                });



//        Product topByIdSortOrderByIdSort2 = productRepository.findFirstByOrderByIdSortAsc();
//        List<Product> topByIdSortOrderByIdSort3 = productRepository.findByIdSortLessThanAndIdSortGreaterThan(productFindById.getIdSort(), topByIdSortOrderByIdSort2.getIdSort());
//        List<Product> topByIdSortOrderByIdSort4 = productRepository.findByIdSortGreaterThanAndIdSortLessThan(productFindById.getIdSort(), topByIdSortOrderByIdSort.getIdSort());
//        Product product = getProducts1().stream().filter(s -> s.getIdSort().equals(productFindById.getIdSort() - 1)).findFirst().map(s -> convertWDol(s)).get();
//        productFindById.setIdSort(productFindById.getIdSort() - 1);
//
//        productRepository.save(productFindById);
//        productRepository.save(product);

        return getProducts1();
    }

    private Product convert(Product s) {
        s.setIdSort(s.getIdSort() - 1);
        return s;
    }

    private Product convertWDol(Product s) {
        s.setIdSort(s.getIdSort() + 1);
        return s;
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

        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();


        tmpIdSort = productFindById.getIdSort();

        Product findFirstOnBelow = productRepository.findFirstByIdSortLessThanOrderByIdSortDesc(tmpIdSort);
        if(findFirstOnBelow == null){
            return getProducts1();
        }

log.info("current object: name: {}, idSort: {} "+ " object on first above: name: {}, idSort: {} " , productFindById.getName(), productFindById.getIdSort() , findFirstOnBelow.getName(), findFirstOnBelow.getIdSort());

        productFindById.setIdSort(findFirstOnBelow.getIdSort());
        findFirstOnBelow.setIdSort(tmpIdSort);
        productRepository.save(productFindById);
        productRepository.save(findFirstOnBelow);
        return getProducts1();
    }

    public List<Product> changePositionOnBelow(Long id, List<Product> productList) {
        Long tmpIdSort;
        Product productFindById = productRepository.findById(id).get();


        tmpIdSort = productFindById.getIdSort();

        Product findFirstOnAbove = productRepository.findFirstByIdSortGreaterThanOrderByIdSortAsc(tmpIdSort);
        if(findFirstOnAbove == null){
            return getProducts1();
        }

        log.info("current object: name: {}, idSort: {} "+ " object on first above: name: {}, idSort: {} " , productFindById.getName(), productFindById.getIdSort() , findFirstOnAbove.getName(), findFirstOnAbove.getIdSort());

        productFindById.setIdSort(findFirstOnAbove.getIdSort());
        findFirstOnAbove.setIdSort(tmpIdSort);
        productRepository.save(productFindById);
        productRepository.save(findFirstOnAbove);
        return getProducts1();
    }
}
