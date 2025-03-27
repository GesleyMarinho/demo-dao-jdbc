import entities.Department;
import entities.Seller;
import modelDao.DaoFactory;
import modelDao.SellerDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("teste encontrado pelo ID");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("Encontrando pelo departamento ");
        Department department = new Department(2, null);
        List<Seller> sellers = sellerDao.findbyDepartment(department);

        for (Seller obj : sellers) {
            System.out.println(obj);
        }

        System.out.println("Listando todos os vendedores");
        List<Seller> list = sellerDao.findAll();

        for (Seller obj : list) {
            System.out.println(obj);
        }

        System.out.println("Teste Insert");

        Seller newSeller = new Seller(0,"greg", "greg@greg.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("insert, new ID = " + newSeller.getId());
        System.out.println("Teste Update");
        sellerDao.update(new Seller(30,"teste","teste@teste.com",new Date(), 3500.0,department));

        System.out.println("Teste Delete");
        sellerDao.deleteById(1);
    }
}