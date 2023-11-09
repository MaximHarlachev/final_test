package api;

import model.api.ApiResponse;
import model.api.CreateEmployeeResponse;
import model.api.EditEmployeeResponse;
import model.api.Employee;

import java.io.IOException;
import java.util.List;

public interface EmployeeService extends Authorization {

    List<Employee> getCompanyId(int CompanyId) throws IOException;

    Employee getById(int id) throws IOException;

    ApiResponse<EditEmployeeResponse> edit(String lastName, String email, String url, String phone, boolean isActive, int lastId) throws IOException;

    ApiResponse<CreateEmployeeResponse> create(String firstName, String lastName, String middleName, int companyId, String phone) throws IOException;

}