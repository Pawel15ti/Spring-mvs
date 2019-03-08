package ozdoba.pawel.demo.dto;

import java.util.Objects;

public class ProductDto {

    private Long id;
    private Long idSort;
    private String name;

    public ProductDto(Long id, Long idSort, String name) {
        this.id = id;
        this.idSort = idSort;
        this.name = name;
    }

    public ProductDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
        if (!(o instanceof ProductDto)) return false;
        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(idSort, that.idSort) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idSort, name);
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", idSort=" + idSort +
                ", name='" + name + '\'' +
                '}';
    }
}
