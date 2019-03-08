package ozdoba.pawel.demo.model;


import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
@Transient
    private Long idSort;
    @NotNull
    @Size(min = 1, max = 100 , message = "Nazwa produktu musi miec co najmniej 1 znak i nie może przekroczyć 100 znaków")//, message = "cos"
//    @Range
    private String name;

    public Product(String name) {
        this.name = name;
    }

    public Product() {
        this.name = name;
    }

    public Long getIdSort() {
        return idSort;
    }

    public void setIdSort(Long idSort) {
        this.idSort = idSort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", idSort=" + idSort +
                ", name='" + name + '\'' +
                '}';
    }
}
