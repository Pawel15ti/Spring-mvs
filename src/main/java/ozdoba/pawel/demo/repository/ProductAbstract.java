package ozdoba.pawel.demo.repository;

import org.springframework.data.repository.CrudRepository;
import ozdoba.pawel.demo.model.Product;

public abstract class ProductAbstract implements CrudRepository<Product, Long> {

//    List<T> findAll();
//    T findById(Id id);
//    List<T> saveAll(List<Id> object);
//    T save(Id object);
//    void delete(T object);
//    void deleteById(Id id);
}
