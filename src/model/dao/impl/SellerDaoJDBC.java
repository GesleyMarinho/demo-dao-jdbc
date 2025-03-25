package model.dao.impl;

import db.DB;
import db.DbException;
import entities.Department;
import entities.Seller;
import modelDao.SellerDao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SellerDaoJDBC implements SellerDao {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Seller findById(int id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*,department.Name as DepName\n" +
                            "FROM seller INNER JOIN department\n" +
                            "ON seller.DepartmentId = department.Id\n" +
                            "WHERE seller.Id = ?");

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) { // teste se tem resultado;
                Department department = instantionDepartment(rs);
                Seller seller = instantionSeller(rs, department);

                return seller;
            }
            return null;


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                DB.closeResulSet(rs);
                DB.closeStatment(st);
            }
        }

    }

    @Override
    public List<Seller> findbyDepartment(Department department) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "WHERE DepartmentId = ?\n" +
                    "ORDER BY Name");


            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> sellerList = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {

                Department dep = map.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantionDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), dep);
                }

                Seller seller = instantionSeller(rs, dep);

                sellerList.add(seller);
            }

            return sellerList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {

            DB.closeStatment(st);
            DB.closeResulSet(rs);

        }

    }

    private Seller instantionSeller(ResultSet rs, Department department) throws SQLException, ParseException {
        Seller seller = new Seller();

        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));

        /* Convertendo a data para um texto, logo no SQLite não aceita o formato de data diretamente e precisa fazer a conversão*/
        String birthDateStr = rs.getString("BirthDate"); // Pegando a data como String
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date birthDate = format.parse(birthDateStr);

        seller.setBirthDate(birthDate);


        seller.setDepartment(department);
        return seller;
    }


    private Department instantionDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();

        department.setId(rs.getInt("DepartmentId"));
        department.setName(rs.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
