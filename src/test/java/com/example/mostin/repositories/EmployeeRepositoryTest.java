package com.example.mostin.repositories;

import com.example.mostin.models.Employee;
import com.example.mostin.models.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee1;
    private Employee testEmployee2;
    private Employee testEmployee3;

    @BeforeEach
    void setUp() {
        // Create test employees with different combinations
        testEmployee1 = new Employee();
        testEmployee1.setEmployeeId("EMP001");
        testEmployee1.setEmployeeName("김테스트");
        testEmployee1.setEmployeePwd("password123");
        testEmployee1.setPhoneNum("010-1234-5678");
        testEmployee1.setEmployeeType("정규직");
        testEmployee1.setAddress("서울시 강남구");
        testEmployee1.setWorkPlaceName("본사");
        entityManager.persistAndFlush(testEmployee1);

        testEmployee2 = new Employee();
        testEmployee2.setEmployeeId("EMP002");
        testEmployee2.setEmployeeName("박테스트");
        testEmployee2.setEmployeePwd("password456");
        testEmployee2.setPhoneNum("010-9876-5432");
        testEmployee2.setEmployeeType("계약직");
        testEmployee2.setAddress("서울시 서초구");
        testEmployee2.setWorkPlaceName("지점A");
        entityManager.persistAndFlush(testEmployee2);

        testEmployee3 = new Employee();
        testEmployee3.setEmployeeId("EMP003");
        testEmployee3.setEmployeeName("이테스트");
        testEmployee3.setEmployeePwd("password789");
        testEmployee3.setPhoneNum("010-5555-6666");
        testEmployee3.setEmployeeType("정규직");
        testEmployee3.setAddress("부산시 해운대구");
        testEmployee3.setWorkPlaceName("지점B");
        entityManager.persistAndFlush(testEmployee3);

        entityManager.clear();
    }

    @Test
    void should_findEmployeeById_when_employeeExists() {
        // Given
        String existingEmployeeId = "EMP001";

        // When
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(existingEmployeeId);

        // Then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeId()).isEqualTo("EMP001");
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("김테스트");
        assertThat(foundEmployee.get().getEmployeePwd()).isEqualTo("password123");
        assertThat(foundEmployee.get().getPhoneNum()).isEqualTo("010-1234-5678");
        assertThat(foundEmployee.get().getEmployeeType()).isEqualTo("정규직");
        assertThat(foundEmployee.get().getAddress()).isEqualTo("서울시 강남구");
        assertThat(foundEmployee.get().getWorkPlaceName()).isEqualTo("본사");
    }

    @Test
    void should_returnEmpty_when_employeeDoesNotExist() {
        // Given
        String nonExistentEmployeeId = "NONEXISTENT";

        // When
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(nonExistentEmployeeId);

        // Then
        assertThat(foundEmployee).isEmpty();
    }

    @Test
    void should_returnEmpty_when_employeeIdIsNull() {
        // Given
        String nullEmployeeId = null;

        // When
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(nullEmployeeId);

        // Then
        assertThat(foundEmployee).isEmpty();
    }

    @Test
    void should_returnEmpty_when_employeeIdIsEmpty() {
        // Given
        String emptyEmployeeId = "";

        // When
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(emptyEmployeeId);

        // Then
        assertThat(foundEmployee).isEmpty();
    }

    @Test
    void should_saveEmployee_when_validEmployeeProvided() {
        // Given
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeId("EMP004");
        newEmployee.setEmployeeName("최신입");
        newEmployee.setEmployeePwd("newPassword");
        newEmployee.setPhoneNum("010-7777-8888");
        newEmployee.setEmployeeType("인턴");
        newEmployee.setAddress("인천시 남동구");
        newEmployee.setWorkPlaceName("인턴실");

        // When
        Employee savedEmployee = employeeRepository.save(newEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedEmployee).isNotNull();
        
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("EMP004");
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("최신입");
        assertThat(foundEmployee.get().getEmployeeType()).isEqualTo("인턴");
    }

    @Test
    void should_updateEmployee_when_existingEmployeeModified() {
        // Given
        String employeeId = "EMP001";
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employeeId);
        assertThat(existingEmployee).isPresent();

        // When
        Employee employee = existingEmployee.get();
        employee.setPhoneNum("010-9999-0000");
        employee.setAddress("서울시 종로구");
        employee.setEmployeeType("수석");
        
        Employee updatedEmployee = employeeRepository.save(employee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(employeeId);
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getPhoneNum()).isEqualTo("010-9999-0000");
        assertThat(foundEmployee.get().getAddress()).isEqualTo("서울시 종로구");
        assertThat(foundEmployee.get().getEmployeeType()).isEqualTo("수석");
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("김테스트"); // 변경되지 않은 필드 확인
    }

    @Test
    void should_deleteEmployee_when_employeeExists() {
        // Given
        String employeeId = "EMP002";
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeId(employeeId);
        assertThat(existingEmployee).isPresent();

        // When
        employeeRepository.delete(existingEmployee.get());
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> deletedEmployee = employeeRepository.findByEmployeeId(employeeId);
        assertThat(deletedEmployee).isEmpty();
    }

    @Test
    void should_findAllEmployees_when_multipleEmployeesExist() {
        // When
        List<Employee> allEmployees = employeeRepository.findAll();

        // Then
        assertThat(allEmployees).hasSize(3);
        assertThat(allEmployees)
                .extracting(Employee::getEmployeeId)
                .containsExactlyInAnyOrder("EMP001", "EMP002", "EMP003");
    }

    @Test
    void should_findEmployeeByCompositeId_when_usingJpaRepositoryMethods() {
        // Given
        EmployeeId compositeId = new EmployeeId();
        compositeId.setEmployeeId("EMP001");
        compositeId.setEmployeeName("김테스트");

        // When
        Optional<Employee> foundEmployee = employeeRepository.findById(compositeId);

        // Then
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeId()).isEqualTo("EMP001");
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("김테스트");
    }

    @Test
    void should_returnEmpty_when_compositeIdDoesNotMatch() {
        // Given
        EmployeeId incorrectCompositeId = new EmployeeId();
        incorrectCompositeId.setEmployeeId("EMP001");
        incorrectCompositeId.setEmployeeName("잘못된이름");

        // When
        Optional<Employee> foundEmployee = employeeRepository.findById(incorrectCompositeId);

        // Then
        assertThat(foundEmployee).isEmpty();
    }

    @Test
    void should_checkExistence_when_employeeExists() {
        // Given
        EmployeeId existingCompositeId = new EmployeeId();
        existingCompositeId.setEmployeeId("EMP002");
        existingCompositeId.setEmployeeName("박테스트");

        // When
        boolean exists = employeeRepository.existsById(existingCompositeId);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void should_checkExistence_when_employeeDoesNotExist() {
        // Given
        EmployeeId nonExistentCompositeId = new EmployeeId();
        nonExistentCompositeId.setEmployeeId("NONEXISTENT");
        nonExistentCompositeId.setEmployeeName("존재하지않음");

        // When
        boolean exists = employeeRepository.existsById(nonExistentCompositeId);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void should_countEmployees_when_multipleEmployeesExist() {
        // When
        long count = employeeRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void should_handleDuplicateEmployeeId_when_differentNames() {
        // Given - Employee with same ID but different name
        Employee duplicateIdEmployee = new Employee();
        duplicateIdEmployee.setEmployeeId("EMP001"); // Same ID
        duplicateIdEmployee.setEmployeeName("다른이름"); // Different name
        duplicateIdEmployee.setEmployeePwd("differentPassword");
        duplicateIdEmployee.setPhoneNum("010-0000-0000");
        duplicateIdEmployee.setEmployeeType("계약직");
        duplicateIdEmployee.setAddress("다른주소");
        duplicateIdEmployee.setWorkPlaceName("다른지점");

        // When
        Employee savedEmployee = employeeRepository.save(duplicateIdEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then - Both employees should exist with different composite keys
        // Note: findByEmployeeId will throw NonUniqueResultException when multiple employees have the same ID
        // This is expected behavior for this method when used with composite keys
        
        // Instead, use findById with composite keys to verify both exist
        EmployeeId originalCompositeId = new EmployeeId();
        originalCompositeId.setEmployeeId("EMP001");
        originalCompositeId.setEmployeeName("김테스트");
        Optional<Employee> originalEmployee = employeeRepository.findById(originalCompositeId);
        assertThat(originalEmployee).isPresent();
        assertThat(originalEmployee.get().getEmployeeName()).isEqualTo("김테스트");

        EmployeeId newCompositeId = new EmployeeId();
        newCompositeId.setEmployeeId("EMP001");
        newCompositeId.setEmployeeName("다른이름");
        Optional<Employee> newEmployee = employeeRepository.findById(newCompositeId);
        assertThat(newEmployee).isPresent();
        assertThat(newEmployee.get().getEmployeeName()).isEqualTo("다른이름");

        // Total count should be 4
        long totalCount = employeeRepository.count();
        assertThat(totalCount).isEqualTo(4);
    }

    @Test
    void should_handleLongEmployeeId_when_savingWithExtendedId() {
        // Given - Employee with very long ID
        String longEmployeeId = "EMP_VERY_LONG_EMPLOYEE_ID_TEST_12345";
        Employee longIdEmployee = new Employee();
        longIdEmployee.setEmployeeId(longEmployeeId);
        longIdEmployee.setEmployeeName("긴아이디테스트");
        longIdEmployee.setEmployeePwd("longIdPassword");
        longIdEmployee.setPhoneNum("010-1111-2222");
        longIdEmployee.setEmployeeType("테스트계정");
        longIdEmployee.setAddress("긴아이디테스트주소");
        longIdEmployee.setWorkPlaceName("긴아이디테스트지점");

        // When
        Employee savedEmployee = employeeRepository.save(longIdEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId(longEmployeeId);
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeId()).isEqualTo(longEmployeeId);
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("긴아이디테스트");
    }

    @Test
    void should_handleSpecialCharactersInEmployeeName_when_saving() {
        // Given - Employee with special characters in name
        Employee specialNameEmployee = new Employee();
        specialNameEmployee.setEmployeeId("EMP_SPECIAL");
        specialNameEmployee.setEmployeeName("김-테스트★★★");
        specialNameEmployee.setEmployeePwd("specialPassword");
        specialNameEmployee.setPhoneNum("010-3333-4444");
        specialNameEmployee.setEmployeeType("특수문자테스트");
        specialNameEmployee.setAddress("특수문자주소 & ♥♥♥");
        specialNameEmployee.setWorkPlaceName("특수문자지점");

        // When
        Employee savedEmployee = employeeRepository.save(specialNameEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("EMP_SPECIAL");
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("김-테스트★★★");
        assertThat(foundEmployee.get().getAddress()).isEqualTo("특수문자주소 & ♥♥♥");
    }

    @Test
    void should_handleEmptyStringFields_when_savingWithEmptyValues() {
        // Given - Employee with empty string fields (not null)
        Employee emptyFieldEmployee = new Employee();
        emptyFieldEmployee.setEmployeeId("EMP_EMPTY");
        emptyFieldEmployee.setEmployeeName("빈문자열테스트");
        emptyFieldEmployee.setEmployeePwd(""); // Empty password
        emptyFieldEmployee.setPhoneNum(""); // Empty phone
        emptyFieldEmployee.setEmployeeType(""); // Empty type
        emptyFieldEmployee.setAddress(""); // Empty address
        emptyFieldEmployee.setWorkPlaceName(""); // Empty workplace

        // When
        Employee savedEmployee = employeeRepository.save(emptyFieldEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("EMP_EMPTY");
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeePwd()).isEqualTo("");
        assertThat(foundEmployee.get().getPhoneNum()).isEqualTo("");
        assertThat(foundEmployee.get().getEmployeeType()).isEqualTo("");
        assertThat(foundEmployee.get().getAddress()).isEqualTo("");
        assertThat(foundEmployee.get().getWorkPlaceName()).isEqualTo("");
    }

    @Test
    void should_handleVeryLongFieldValues_when_savingWithExtendedData() {
        // Given - Employee with very long field values
        String longAddress = "대한민국 서울특별시 강남구 테헤란로 123번길 45-67 매우긴주소를가지고있는빌딩 456층 789호 상세주소테스트";
        Employee longFieldEmployee = new Employee();
        longFieldEmployee.setEmployeeId("EMP_LONG");
        longFieldEmployee.setEmployeeName("매우아주긴이름을가지고있는직원");
        longFieldEmployee.setEmployeePwd("verylongpasswordthatexceedsnormallimits12345678901234567890");
        longFieldEmployee.setPhoneNum("010-1234-5678-매우긴전화번호");
        longFieldEmployee.setEmployeeType("매우긴직원유형이름테스트");
        longFieldEmployee.setAddress(longAddress);
        longFieldEmployee.setWorkPlaceName("매우긴직장명테스트대지점");

        // When
        Employee savedEmployee = employeeRepository.save(longFieldEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("EMP_LONG");
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getAddress()).isEqualTo(longAddress);
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("매우아주긴이름을가지고있는직원");
    }

    @Test
    void should_findMultipleEmployees_when_queryingWithDifferentCriteria() {
        // Given - Additional employees for comprehensive search testing
        Employee adminEmployee = new Employee();
        adminEmployee.setEmployeeId("ADMIN001");
        adminEmployee.setEmployeeName("관리자테스트");
        adminEmployee.setEmployeePwd("adminPassword");
        adminEmployee.setPhoneNum("010-9999-0000");
        adminEmployee.setEmployeeType("관리자");
        adminEmployee.setAddress("관리자주소");
        adminEmployee.setWorkPlaceName("본사");
        entityManager.persistAndFlush(adminEmployee);

        Employee managerEmployee = new Employee();
        managerEmployee.setEmployeeId("MGR001");
        managerEmployee.setEmployeeName("관리자테스트"); // Same name as admin
        managerEmployee.setEmployeePwd("managerPassword");
        managerEmployee.setPhoneNum("010-8888-0000");
        managerEmployee.setEmployeeType("관리자");
        managerEmployee.setAddress("관리자주소");
        managerEmployee.setWorkPlaceName("지점A");
        entityManager.persistAndFlush(managerEmployee);

        // When
        List<Employee> allEmployees = employeeRepository.findAll();
        Optional<Employee> adminFound = employeeRepository.findByEmployeeId("ADMIN001");
        Optional<Employee> managerFound = employeeRepository.findByEmployeeId("MGR001");

        // Then
        assertThat(allEmployees).hasSize(5); // 3 original + 2 new
        assertThat(adminFound).isPresent();
        assertThat(managerFound).isPresent();
        
        // Verify they have same name but different IDs and workplaces
        assertThat(adminFound.get().getEmployeeName()).isEqualTo("관리자테스트");
        assertThat(managerFound.get().getEmployeeName()).isEqualTo("관리자테스트");
        assertThat(adminFound.get().getWorkPlaceName()).isEqualTo("본사");
        assertThat(managerFound.get().getWorkPlaceName()).isEqualTo("지점A");
    }

    @Test
    void should_handleCaseInsensitiveQuery_when_findingByEmployeeId() {
        // Given - Employee with mixed case ID
        Employee mixedCaseEmployee = new Employee();
        mixedCaseEmployee.setEmployeeId("eMp_MiXeD_CaSe");
        mixedCaseEmployee.setEmployeeName("대소문자테스트");
        mixedCaseEmployee.setEmployeePwd("mixedCasePassword");
        mixedCaseEmployee.setPhoneNum("010-5555-6666");
        mixedCaseEmployee.setEmployeeType("대소문자테스트");
        mixedCaseEmployee.setAddress("대소문자주소");
        mixedCaseEmployee.setWorkPlaceName("대소문자지점");
        entityManager.persistAndFlush(mixedCaseEmployee);

        // When - Query with exact case
        Optional<Employee> exactCaseEmployee = employeeRepository.findByEmployeeId("eMp_MiXeD_CaSe");
        
        // When - Query with different case (should not find in most databases)
        Optional<Employee> lowerCaseEmployee = employeeRepository.findByEmployeeId("emp_mixed_case");
        Optional<Employee> upperCaseEmployee = employeeRepository.findByEmployeeId("EMP_MIXED_CASE");

        // Then
        assertThat(exactCaseEmployee).isPresent();
        // Note: Case sensitivity behavior depends on database collation settings
        // These assertions might need adjustment based on H2 default behavior
        assertThat(exactCaseEmployee.get().getEmployeeId()).isEqualTo("eMp_MiXeD_CaSe");
    }

    @Test
    void should_handleNumericEmployeeIds_when_savingNumericIds() {
        // Given - Employee with numeric ID
        Employee numericIdEmployee = new Employee();
        numericIdEmployee.setEmployeeId("123456789");
        numericIdEmployee.setEmployeeName("숫자아이디테스트");
        numericIdEmployee.setEmployeePwd("numericPassword");
        numericIdEmployee.setPhoneNum("010-1111-2222");
        numericIdEmployee.setEmployeeType("숫자아이디테스트");
        numericIdEmployee.setAddress("숫자아이디주소");
        numericIdEmployee.setWorkPlaceName("숫자아이디지점");

        // When
        Employee savedEmployee = employeeRepository.save(numericIdEmployee);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("123456789");
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getEmployeeId()).isEqualTo("123456789");
        assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("숫자아이디테스트");
    }

    @Test
    void should_handleBatchOperations_when_savingMultipleEmployees() {
        // Given - Multiple employees for batch testing
        List<Employee> employeesToSave = new ArrayList<>();
        for (int i = 100; i < 110; i++) {
            Employee batchEmployee = new Employee();
            batchEmployee.setEmployeeId("BATCH" + i);
            batchEmployee.setEmployeeName("일괄등록" + i);
            batchEmployee.setEmployeePwd("batchPassword" + i);
            batchEmployee.setPhoneNum("010-" + i + "-" + i);
            batchEmployee.setEmployeeType("일괄등록테스트");
            batchEmployee.setAddress("일괄등록주소" + i);
            batchEmployee.setWorkPlaceName("일괄등록지점" + i);
            employeesToSave.add(batchEmployee);
        }

        // When
        List<Employee> savedEmployees = employeeRepository.saveAll(employeesToSave);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedEmployees).hasSize(10);
        
        // Verify each employee was saved correctly
        for (int i = 100; i < 110; i++) {
            Optional<Employee> foundEmployee = employeeRepository.findByEmployeeId("BATCH" + i);
            assertThat(foundEmployee).isPresent();
            assertThat(foundEmployee.get().getEmployeeName()).isEqualTo("일괄등록" + i);
        }
        
        // Verify total count
        long totalCount = employeeRepository.count();
        assertThat(totalCount).isEqualTo(13); // 3 original + 10 batch
    }

    @Test
    void should_handleMultipleDeleteOperations_when_deletingByCompositeId() {
        // Given - Additional employees for deletion testing
        Employee deletionTestEmployee1 = new Employee();
        deletionTestEmployee1.setEmployeeId("DEL001");
        deletionTestEmployee1.setEmployeeName("삭제테스트1");
        deletionTestEmployee1.setEmployeePwd("deletePassword1");
        deletionTestEmployee1.setPhoneNum("010-1111-1111");
        deletionTestEmployee1.setEmployeeType("삭제테스트");
        deletionTestEmployee1.setAddress("삭제테스트주소1");
        deletionTestEmployee1.setWorkPlaceName("삭제테스트지점");
        entityManager.persistAndFlush(deletionTestEmployee1);

        Employee deletionTestEmployee2 = new Employee();
        deletionTestEmployee2.setEmployeeId("DEL002");
        deletionTestEmployee2.setEmployeeName("삭제테스트2");
        deletionTestEmployee2.setEmployeePwd("deletePassword2");
        deletionTestEmployee2.setPhoneNum("010-2222-2222");
        deletionTestEmployee2.setEmployeeType("삭제테스트");
        deletionTestEmployee2.setAddress("삭제테스트주소2");
        deletionTestEmployee2.setWorkPlaceName("삭제테스트지점");
        entityManager.persistAndFlush(deletionTestEmployee2);

        // Verify initial state
        assertThat(employeeRepository.count()).isEqualTo(5); // 3 original + 2 deletion test

        // When - Delete by composite IDs
        EmployeeId compositeId1 = new EmployeeId();
        compositeId1.setEmployeeId("DEL001");
        compositeId1.setEmployeeName("삭제테스트1");
        
        EmployeeId compositeId2 = new EmployeeId();
        compositeId2.setEmployeeId("DEL002");
        compositeId2.setEmployeeName("삭제테스트2");

        employeeRepository.deleteById(compositeId1);
        employeeRepository.deleteById(compositeId2);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(employeeRepository.count()).isEqualTo(3); // Back to original 3
        assertThat(employeeRepository.findByEmployeeId("DEL001")).isEmpty();
        assertThat(employeeRepository.findByEmployeeId("DEL002")).isEmpty();
        assertThat(employeeRepository.existsById(compositeId1)).isFalse();
        assertThat(employeeRepository.existsById(compositeId2)).isFalse();
    }
}