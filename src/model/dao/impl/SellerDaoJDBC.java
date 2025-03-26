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

    /**
     * Insere um novo vendedor (Seller) no banco de dados e recupera o ID gerado automaticamente.
     *
     * @param obj Objeto Seller a ser inserido no banco de dados.
     * @throws DbException Se ocorrer um erro durante a inserção.
     */
    @Override
    public void insert(Seller obj) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("INSERT INTO seller\n" +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId)\n" +
                            "VALUES\n" +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS); // Habilita a recuperação do ID gerado

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());


            int rows = st.executeUpdate();

            if (rows > 0) {
                rs = st.getGeneratedKeys();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                    DB.closeStatment(st);
                }

            } else {
                throw new DbException("Nenhuma linha afetada!");
            }
        } catch (Exception e) {
            throw new DbException(e.getMessage());
        } finally {

            DB.closeResulSet(rs);
        }
    }

    @Override
    public void update(Seller obj) {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("UPDATE seller\n" +
                            " SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?\n" +
                            " WHERE Id = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            /*Convertendo a data para Texto*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String birthDateStr = sdf.format(obj.getBirthDate());

            st.setString(3, birthDateStr);
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Erro nenhuma linha foi atualizada. verifique o ID");
            } else {
                System.out.println("Update realizado com sucesso! ID atualizado " + obj.getId());
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResulSet(rs);
            DB.closeStatment(st);
        }

    }

    @Override
    public void deleteById(int id) {

        PreparedStatement st = null;


        try {
            st = conn.prepareStatement("DELETE FROM seller\n" +
                    "WHERE Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Usuário não encontrado, Delete não foi aplicado! " + id);
            } else {
                System.out.println("Usuário Deletado com Sucesso " + id);
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {

            DB.closeStatment(st);
        }
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

    /**
     * Métod0 responsável por instanciar um objeto Seller a partir do resultado de uma consulta SQL.
     *
     * @param rs         ResultSet contendo os dados da consulta ao banco de dados.
     * @param department Objeto Department já instanciado, relacionado ao vendedor.
     * @return Um objeto Seller preenchido com os dados do banco de dados.
     * @throws SQLException   Caso ocorra um erro ao acessar os dados do ResultSet.
     * @throws ParseException Caso ocorra um erro ao converter a data de nascimento.
     **/
    private Seller instantionSeller(ResultSet rs, Department department) throws SQLException, ParseException {
        Seller seller = new Seller();

        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBaseSalary(rs.getDouble("BaseSalary"));

        /* Pegando a data como String */
        String birthDateStr = rs.getString("BirthDate");

        /* Ajustando o formato para corresponder à String do banco */
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date birthDate = format.parse(birthDateStr);
        System.out.println("Data retornada do banco: " + birthDate);
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

        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement("SELECT seller.*,department.Name as DepName\n" +
                    "FROM seller INNER JOIN department\n" +
                    "ON seller.DepartmentId = department.Id\n" +
                    "ORDER BY Name");

            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (rs.next()) {
                Department department = map.get(rs.getInt("DepartmentId"));
                if (department == null) {
                    department = instantionDepartment(rs);
                    map.put(rs.getInt("DepartmentId"), department);
                }

                Seller obj = instantionSeller(rs, department);
                list.add(obj);

            }

            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closeResulSet(rs);
            DB.closeStatment(st);
        }

    }
}
