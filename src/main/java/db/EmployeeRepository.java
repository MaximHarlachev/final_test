package db;

import model.bd.EmployeeEntity;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeRepository {
    List<EmployeeEntity> getByCompany(int companyId) throws SQLException;
    EmployeeEntity getById(int id) throws SQLException;
    int deleteById(int id) throws SQLException;
    int updateActive(int id) throws SQLException;
}
