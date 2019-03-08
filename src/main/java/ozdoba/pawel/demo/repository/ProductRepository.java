package ozdoba.pawel.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ozdoba.pawel.demo.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByIdSortLessThanOrderByIdSortDesc(Long idSort);// znajdz pierwszy mniejszy od aktualnego dla operaz w góre
    Product findFirstByIdSortGreaterThanOrderByIdSortAsc(Long idSort);// znajdz pierwszy większy od aktualnego dla operaz w dół
    Product findFirstByOrderByIdSortAsc();// znajdz ostatni element od aktualnego dla operaz na samą górę
    Product findFirstByOrderByIdSortDesc();// znajdz pierwszy element od aktualnego dla operaz na sam dół
    List<Product> findByIdSortLessThanAndIdSortGreaterThanEqual(Long first, Long Last);// te elementy którym będę zwiększał idSort ++ z findFirstByOrderByIdSortAsc
    List<Product> findByIdSortGreaterThanAndIdSortLessThanEqual(Long first, Long Last);// te elementy którym będę zmniejszał idSort -- z findFirstByOrderByIdSortDesc

//    @Query("delete from User u where user.role.id = ?1")
//    void deleteInBulkByRoleId(long roleId);


}
