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

        System.out.println( "teste encontrado pelo ID");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("Encontrando pelo departamento ");
        Department department = new Department(2,null );
        List<Seller> sellers = sellerDao.findbyDepartment(department);

        for(Seller obj : sellers){
            System.out.println(obj);
        }

        System.out.println("Listando todos os vendedores");
        List<Seller> list = sellerDao.findAll();

        for (Seller obj : list){
            System.out.println(obj);
        }
    }
}