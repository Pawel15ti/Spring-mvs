package ozdoba.pawel.demo.repository;

import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.List;

public interface CrudProduct<T, Id> {

    List<T> findAll();
    T findById(Id id);
    List<T> saveAll(List<Id> object);
    T save(Id object);
}
