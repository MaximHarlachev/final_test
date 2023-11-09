import api.EmployeeService;
import com.github.javafaker.Faker;
import db.CompanyRepository;
import db.EmployeeRepository;
import ext.Auth;
import ext.CompanyRepositoryResolver;
import ext.EmployeeRepositoryResolver;
import ext.EmployeeServiceResolver;
import model.api.ApiResponse;
import model.api.CreateEmployeeResponse;
import model.api.EditEmployeeResponse;
import model.api.Employee;
import model.bd.EmployeeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith({EmployeeServiceResolver.class, EmployeeRepositoryResolver.class, CompanyRepositoryResolver.class})
public class EmployeeTest {

    @Test
    @DisplayName("Получить список сотрудников для компании")
    public void employeeListCompanyId(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, EmployeeRepository repository, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        int newIdEmployee = createEmployee(service, companyId);
        int newIdEmployee2 = createEmployee(service, companyId);
        List<Employee> list = service.getCompanyId(companyId);
        List<EmployeeEntity> listEntity = repository.getByCompany(companyId);
        assertEquals(list.size(), listEntity.size());
        repository.deleteById(newIdEmployee);
        repository.deleteById(newIdEmployee2);
        companyRepository.deleteById(companyId);
    }

    @Test
    @DisplayName("Добавить нового сотрудника. Получить сотрудника по id")
    public void shouldCreateEmployee(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, EmployeeRepository repository, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        int newIdEmployee = createEmployee(service, companyId);
        Employee employee = getEmployeeService(service, newIdEmployee);
        EmployeeEntity employeeEntity = getEmployeeRepository(repository, newIdEmployee);
        assertEquals(employee.getId(), employeeEntity.getId());
        assertEquals(employee.getFirstName(), employeeEntity.getFirstName());
        repository.deleteById(newIdEmployee);
        companyRepository.deleteById(companyId);
    }

    @Test
    @DisplayName("Без авторизации сотрудник не создается. Ошибка 401")
    public void shouldNotCreateEmployee401(
            EmployeeService service, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        Faker fakerEmployee = new Faker(new Locale("ru"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "+7(999)0000001";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        assertEquals(401, response.getStatusCode());
        assertEquals("Unauthorized", response.getError().getMessage());
        companyRepository.deleteById(companyId);
    }

    @Test
    @DisplayName("Добавить нового сотрудника. Некорректные данные в companyId. Ошибка 500")
    public void shouldNotCreateEmployeeCompany500(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service) throws IOException {
        Faker fakerEmployee = new Faker(new Locale("ru"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "+7(999)0000001";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, 0, phone);
        assertEquals(500, response.getStatusCode());
        assertEquals("Internal server error", response.getError().getMessage());
    }

    @Test
    @DisplayName("Добавить нового сотрудника. Некорректные данные в phone - номер с дефисами. Ошибка 500")
    public void shouldNotCreateEmployeePhoneBlank500(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        Faker fakerEmployee = new Faker(new Locale("ru"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "+7-(999)-000-00-01";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        assertEquals(500, response.getStatusCode());
        assertEquals("Internal server error", response.getError().getMessage());
        companyRepository.deleteById(companyId);
    }

    @Test
    @DisplayName("Добавить нового сотрудника. Некорректные данные в phone (null). Ошибка 500")
    public void shouldNotCreateEmployeePhoneNull500(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        System.out.println(companyId);
        Faker fakerEmployee = new Faker(new Locale("ru"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = null;
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        assertEquals(500, response.getStatusCode());
        assertEquals("Internal server error", response.getError().getMessage());
        companyRepository.deleteById(companyId);
    }

    @Test
    @DisplayName("Изменение информации о сотруднике")
    public void shouldEditEmployee(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, EmployeeRepository repository, CompanyRepository companyRepository) throws IOException, SQLException {
        int companyId = createCompany(companyRepository);
        int newIdEmployee = createEmployee(service, companyId);
        Faker faker = new Faker(new Locale("ru"));
        String lastName = faker.name().lastName();
        String email = "testing_mail@inbox.ru";
        String urlEmployee = "http://testing.ru";
        String phone = "+7(999)0000001";
        boolean isActive = false;
        ApiResponse<EditEmployeeResponse> response = service.edit(lastName, email, urlEmployee, phone, isActive, newIdEmployee);
        Employee employee = getEmployeeService(service, newIdEmployee);
        EmployeeEntity employeeEntity = getEmployeeRepository(repository, newIdEmployee);
        assertEquals(newIdEmployee, employeeEntity.getId());
        assertEquals(lastName, employeeEntity.getLastName());
        assertEquals(email, employeeEntity.getEmail());
        assertEquals(urlEmployee, employeeEntity.getAvatarUrl());
        assertFalse(employeeEntity.isActive());
        repository.deleteById(newIdEmployee);
        companyRepository.deleteById(companyId);

    }

    public int createCompany(
            CompanyRepository companyRepository) throws SQLException {
        Faker fakerCompany = new Faker(new Locale("ru"));
        String nameCompany = fakerCompany.company().name();
        String descriptionCompany = fakerCompany.address().fullAddress();
        int companyId = companyRepository.create(nameCompany, descriptionCompany);
        return companyId;
    }

    public int createEmployee(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, int companyId) throws IOException {
        Faker fakerEmployee = new Faker(new Locale("ru"));
        String firstName = fakerEmployee.name().firstName();
        String lastName = fakerEmployee.name().lastName();
        String middleName = fakerEmployee.name().firstName();
        String phone = "+7(999)0000001";
        ApiResponse<CreateEmployeeResponse> response = service.create(firstName, lastName, middleName, companyId, phone);
        int newIdEmployee = response.getBody().getId();
        return newIdEmployee;
    }

    public Employee getEmployeeService(
            @Auth(username = "donatello", password = "does-machines")
            EmployeeService service, int employeeId) throws IOException {
        Employee employee = service.getById(employeeId);
        return employee;
    }

    public EmployeeEntity getEmployeeRepository(
            EmployeeRepository repository, int employeeId) throws SQLException {
        EmployeeEntity employee = repository.getById(employeeId);
        return employee;
    }

}
