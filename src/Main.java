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

        Seller seller = sellerDao.findById(3);
        System.out.println(seller);


        Department department = new Department(2,null );
        List<Seller> sellers = sellerDao.findbyDepartment(department);

        for(Seller obj : sellers){
            System.out.println(obj);
        }
    }
}