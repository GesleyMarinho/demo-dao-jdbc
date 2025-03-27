package model.dao.impl;

import db.DB;
import db.DbException;
import entities.Department;
import modelDao.DepartmentDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department obj) {

        PreparedStatement ps;
        ResultSet rs = null;

        try {

            ps = connection.prepareStatement("insert into department (Name) values  (?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, obj.getName());


            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                    DB.closeStatment(ps);
                    System.out.println("Departamento Inserido com sucesso!");
                } else {
                    throw new RuntimeException();
                }
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResulSet(rs);
        }

    }

    @Override
    public void update(Department obj) {

        PreparedStatement ps = null;

        try {
            ps = connection.prepareStatement("Update department set Name = ? where id = ?");

            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());

            System.out.println(ps);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Departamento atualizados");
            } else {
                throw new DbException("Departamento não atualizados !");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }

    }

    @Override
    public void deleteById(int id) {

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM department WHERE ID = ?");

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Departamento Deletado com Sucesso");
            } else {
                throw new DbException("Departamento não encontrado");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeStatment(ps);
        }

    }

    @Override
    public Department findById(int id) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("select * from department where id  = ?");
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {

                Department department = instantionDepartment(rs);
                System.out.println("Departamento encontrado: " + department.getName());
                return department;

            } else {
                System.out.println("Departamento não encontrado;");
                return null;
            }


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResulSet(rs);
            DB.closeStatment(ps);
        }


    }

    @Override
    public List<Department> findAll() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement("Select * from department ");

            rs = ps.executeQuery();
            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department department = instantionDepartment(rs);
                list.add(department);
            }

            if(list.isEmpty()) {
                System.out.println("Nenhum Departamento não encontrado.");
            }
            return list;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Department instantionDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();

        department.setId(rs.getInt("id"));
        department.setName(rs.getString("name"));
        return department;
    }
}
