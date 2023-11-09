import api.CompanyService;
import api.EmployeeService;
import com.github.javafaker.Faker;
import db.CompanyRepository;
import db.EmployeeRepository;
import ext.*;
import model.api.ApiResponse;
import model.api.Company;
import model.api.CreateEmployeeResponse;
import model.api.Employee;
import model.bd.CompanyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({EmployeeServiceResolver.class, EmployeeRepositoryResolver.class, CompanyRepositoryResolver.class, CompanyServiceResolver.class})
@DisplayName("Итоговая аттестация")
public class FinalTest {

    @ParameterizedTest(name = "= {0}")
    @ValueSource(booleans = {true, false})
    public void shouldCompanyActiveTest(boolean isActive, CompanyService service, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        int companyId2 = createCompany(companyRepository);
        companyRepository.updateActive(companyId);
        List<Company> company = service.getAll(isActive);
        List<CompanyEntity> companyEntity = companyRepository.getAll(isActive);
        companyRepository.deleteById(companyId);
        companyRepository.deleteById(companyId2);
            for (Company list : company) {
                assertEquals(isActive, list.isActive());
            };
        assertEquals(company.size(), companyEntity.size());
    }

    @Test
    public void shouldNotCreateEmployeeCompany500(
            EmployeeService service, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        companyRepository.deleteById(companyId);
        Faker fakerEmployee = new Faker(new Locale("en"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "8(800)1000000";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        assertEquals(500, response.getStatusCode());
        assertEquals("Internal server error", response.getError().getMessage());
    }

    @Test
    public void shouldNoActiveEmployee(
            EmployeeService service, EmployeeRepository repository, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        int newIdEmployee = createEmployee(service, companyId);
        int newIdEmployee2 = createEmployee(service, companyId);
        repository.updateActive(newIdEmployee);
        List<Employee> list = service.getCompanyId(companyId);
        repository.deleteById(newIdEmployee);
        repository.deleteById(newIdEmployee2);
        companyRepository.deleteById(companyId);
        for (Employee employee : list) {
                assertTrue(employee.isActive());
            };
    }

    @Test
    public void shouldCompanyDeletedAt(
            CompanyService service, CompanyRepository companyRepository) throws IOException, SQLException, InterruptedException {
        int companyId = createCompany(companyRepository);
        service.deleteById(companyId);
        Thread.sleep(1000);
        CompanyEntity companyEntity = companyRepository.getById(companyId);
        companyRepository.deleteById(companyId);
        assertNotNull(companyEntity.getDeletedAt());
    }

    public int createCompany (
            CompanyRepository companyRepository) throws SQLException {
        Faker fakerCompany = new Faker(new Locale("en"));
        String nameCompany = fakerCompany.company().name();
        String descriptionCompany = fakerCompany.address().fullAddress();
        return companyRepository.create(nameCompany, descriptionCompany);
    }

    public int createEmployee (
            EmployeeService service, int companyId) throws IOException {
        Faker fakerEmployee = new Faker(new Locale("en"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "8(800)1000000";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        return response.getBody().getId();
    }

    public String dateConversion (Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(timestamp.getTime());
    }}
