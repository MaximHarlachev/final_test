package api;

import model.api.ApiResponse;
import model.api.Company;
import model.api.CreateCompanyResponse;

import java.io.IOException;
import java.util.List;

public interface CompanyService extends Authorization{
    List<Company> getAll() throws IOException;

    List<Company> getAll(boolean isActive) throws IOException;
    Company getById(int id) throws IOException;

    ApiResponse<CreateCompanyResponse> create(String name, String description) throws IOException;

    void deleteById(int id) throws IOException;
}
