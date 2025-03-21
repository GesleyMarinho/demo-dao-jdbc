import entities.Department;
import entities.Seller;
import modelDao.DaoFactory;
import modelDao.SellerDao;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Department department = new Department(1, "table");
        Seller seller = new Seller(1, "bob", "bob@bob.com", new Date(), 4000.0, department);

        SellerDao sellerDao = DaoFactory.createSellerDao();


        System.out.println(department);

        System.out.println(seller);
    }
}