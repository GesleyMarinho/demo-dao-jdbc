import entities.Department;
import modelDao.DaoFactory;
import modelDao.DepartmentDao;

import java.util.List;

public class Program {
    public static void main (String[] args){

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("Teste Insert");
        Department dep = new Department(0,"Lutador");
        departmentDao.insert(dep);

       // System.out.println("Teste Update");
        //departmentDao.update(new Department(7,"T.I"));

        //System.out.println("Teste Delete");
        //departmentDao.deleteById(9);

        //System.out.println("Teste Encontrar departamento pelo ID");
        //departmentDao.findById(7);

        /*System.out.println("Teste Encontrar todos os Departamento");
        List<Department> list = departmentDao.findAll();

        for(Department obj : list){
            System.out.println(obj);
        }*/

    }
}
