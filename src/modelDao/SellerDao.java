package modelDao;

import entities.Department;
import entities.Seller;

import java.util.List;

public interface SellerDao {
    void insert(Seller obj);

    void update(Seller obj);

    void deleteById(int id);

    Seller findById(int id);

    List<Seller> findbyDepartment(Department department);

    List<Seller> findAll();
}
