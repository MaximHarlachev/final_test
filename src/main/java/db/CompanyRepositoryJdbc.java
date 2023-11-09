package db;

import model.bd.CompanyEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepositoryJdbc implements CompanyRepository{
    private final static String SELECT = "select * from company where id = ?";
    private final static String INSERT = "insert into company(name) values(?)";
    private final static String INSERT_WITH_DESC = "insert into company(name, description) values(?, ?)";
    private final static String DELETE = "delete from company where id = ?";
    private final static String IS_ACTIVE = "select * from company where deleted_at is null and is_active = ?";
    private final static String UPDATE_COMPANY = "update company set is_active = false where id = ?";

    private Connection connection;

    public CompanyRepositoryJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<CompanyEntity> getAll() throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(SELECT);
        List<CompanyEntity> list = new ArrayList<>();
        while (resultSet.next()) {
            CompanyEntity entity = new CompanyEntity();
            entity.setId(resultSet.getInt("id"));
            entity.setActive(resultSet.getBoolean("is_active"));
            entity.setCreateDateTime(resultSet.getTimestamp("create_timestamp"));
            entity.setLastChangedDateTime(resultSet.getTimestamp("change_timestamp"));
            entity.setName(resultSet.getString("name"));
            entity.setDescription(resultSet.getString("description"));
            entity.setDeletedAt(resultSet.getTimestamp("deleted_at"));
            list.add(entity);
        }
        return list;    }

    @Override
    public List<CompanyEntity> getAll(boolean isActive) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(IS_ACTIVE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setBoolean(1, isActive);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<CompanyEntity> list = new ArrayList<>();
        while (resultSet.next()) {
            CompanyEntity entity = new CompanyEntity();
            entity.setId(resultSet.getInt("id"));
            entity.setActive(resultSet.getBoolean("is_active"));
            entity.setCreateDateTime(resultSet.getTimestamp("create_timestamp"));
            entity.setLastChangedDateTime(resultSet.getTimestamp("change_timestamp"));
            entity.setName(resultSet.getString("name"));
            entity.setDescription(resultSet.getString("description"));
            entity.setDeletedAt(resultSet.getTimestamp("deleted_at"));
            list.add(entity);
        }
        return list;    }

    @Override
    public CompanyEntity getLast() throws SQLException {
        return null;
    }

    @Override
    public CompanyEntity getById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        CompanyEntity entity = new CompanyEntity();
        entity.setName(generatedKeys.getString("name"));
        entity.setActive(generatedKeys.getBoolean("isActive"));
        entity.setId(generatedKeys.getInt("id"));
        entity.setDescription(generatedKeys.getString("description"));
        return entity;
    }

    @Override
    public int create(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public int create(String name, String description) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WITH_DESC, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, description);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }

    @Override
    public void deleteById(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    public int updateActive(int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COMPANY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getInt(1);
    }
}
