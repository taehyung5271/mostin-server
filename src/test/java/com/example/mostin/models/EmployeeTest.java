package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Employee 엔티티 테스트")
class EmployeeTest {

    private Employee employee1;
    private Employee employee2;
    private Employee employee3;

    @BeforeEach
    void setUp() {
        employee1 = new Employee();
        employee1.setEmployeeId("EMP001");
        employee1.setEmployeeName("김철수");
        employee1.setEmployeePwd("password123");
        employee1.setPhoneNum("010-1234-5678");
        employee1.setEmployeeType("정규직");
        employee1.setAddress("서울시 강남구");
        employee1.setWorkPlaceName("본사");

        employee2 = new Employee();
        employee2.setEmployeeId("EMP001");
        employee2.setEmployeeName("김철수");
        employee2.setEmployeePwd("password123");
        employee2.setPhoneNum("010-1234-5678");
        employee2.setEmployeeType("정규직");
        employee2.setAddress("서울시 강남구");
        employee2.setWorkPlaceName("본사");

        employee3 = new Employee();
        employee3.setEmployeeId("EMP002");
        employee3.setEmployeeName("이영희");
        employee3.setEmployeePwd("password456");
        employee3.setPhoneNum("010-9876-5432");
        employee3.setEmployeeType("계약직");
        employee3.setAddress("서울시 서초구");
        employee3.setWorkPlaceName("지점A");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameCompositeKeyValues")
    void should_returnTrue_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함 (복합키 기준)
        assertEquals(employee1, employee2);
        assertTrue(employee1.equals(employee2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentCompositeKeyValues")
    void should_returnFalse_when_differentCompositeKeyValues() {
        // Given: 다른 복합키 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(employee1, employee3);
        assertFalse(employee1.equals(employee3));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_sameCompositeKeyValues")
    void should_returnSameHashCode_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: 같은 hashCode를 반환해야 함
        assertEquals(employee1.hashCode(), employee2.hashCode());
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: Employee 객체
        
        // When: toString 호출
        String result = employee1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("EMP001"));
        assertTrue(result.contains("김철수"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("employeeId="));
        assertTrue(result.contains("employeeName="));
        // 민감정보인 비밀번호도 포함되지만, 실제 환경에서는 제외하는 것을 권장
    }

    @Test
    @DisplayName("should_setAndGetAllFields_when_lombokDataUsed")
    void should_setAndGetAllFields_when_lombokDataUsed() {
        // Given: 새로운 Employee 객체
        Employee employee = new Employee();
        
        // When: 모든 필드 설정
        employee.setEmployeeId("EMP999");
        employee.setEmployeeName("테스트사용자");
        employee.setEmployeePwd("testPassword");
        employee.setPhoneNum("010-0000-0000");
        employee.setEmployeeType("인턴");
        employee.setAddress("테스트 주소");
        employee.setWorkPlaceName("테스트 근무지");
        
        // Then: 모든 getter가 올바르게 동작해야 함
        assertEquals("EMP999", employee.getEmployeeId());
        assertEquals("테스트사용자", employee.getEmployeeName());
        assertEquals("testPassword", employee.getEmployeePwd());
        assertEquals("010-0000-0000", employee.getPhoneNum());
        assertEquals("인턴", employee.getEmployeeType());
        assertEquals("테스트 주소", employee.getAddress());
        assertEquals("테스트 근무지", employee.getWorkPlaceName());
    }

    @Test
    @DisplayName("should_handleNullFields_when_equalsAndHashCodeCalled")
    void should_handleNullFields_when_equalsAndHashCodeCalled() {
        // Given: null 복합키를 가진 객체들
        Employee nullEmployee1 = new Employee();
        nullEmployee1.setEmployeeId(null);
        nullEmployee1.setEmployeeName(null);
        
        Employee nullEmployee2 = new Employee();
        nullEmployee2.setEmployeeId(null);
        nullEmployee2.setEmployeeName(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullEmployee1, nullEmployee2);
        assertEquals(nullEmployee1.hashCode(), nullEmployee2.hashCode());
        assertNotEquals(employee1, nullEmployee1);
    }

    @Test
    @DisplayName("should_handleOneToManyRelationships_when_settingCollections")
    void should_handleOneToManyRelationships_when_settingCollections() {
        // Given: Employee와 연관관계 리스트들
        List<Commute> commutes = new ArrayList<>();
        List<Ordering> orders = new ArrayList<>();
        
        // When: 연관관계 설정
        employee1.setCommutes(commutes);
        employee1.setOrders(orders);
        
        // Then: 연관관계가 올바르게 설정되어야 함
        assertNotNull(employee1.getCommutes());
        assertNotNull(employee1.getOrders());
        assertEquals(commutes, employee1.getCommutes());
        assertEquals(orders, employee1.getOrders());
    }

    @Test
    @DisplayName("should_maintainLombokDataEqualsContract_when_allFieldsConsidered")
    void should_maintainLombokDataEqualsContract_when_allFieldsConsidered() {
        // Given: 같은 복합키, 다른 비키 필드를 가진 객체들 (Lombok @Data는 모든 필드를 비교)
        Employee sameKeyDifferentFields = new Employee();
        sameKeyDifferentFields.setEmployeeId("EMP001");
        sameKeyDifferentFields.setEmployeeName("김철수");
        sameKeyDifferentFields.setEmployeePwd("differentPassword");  // 다른 비밀번호
        sameKeyDifferentFields.setPhoneNum("010-0000-1111");        // 다른 전화번호
        sameKeyDifferentFields.setEmployeeType("파트타임");           // 다른 직종
        sameKeyDifferentFields.setAddress("부산시 해운대구");         // 다른 주소
        sameKeyDifferentFields.setWorkPlaceName("지점B");           // 다른 근무지
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로 비키 필드가 다르면 false여야 함
        assertNotEquals(employee1, sameKeyDifferentFields);
        assertNotEquals(employee1.hashCode(), sameKeyDifferentFields.hashCode());
        
        // 모든 필드가 같으면 true여야 함 (null 컬렉션 제외)
        Employee exactCopy = new Employee();
        exactCopy.setEmployeeId("EMP001");
        exactCopy.setEmployeeName("김철수");
        exactCopy.setEmployeePwd("password123");
        exactCopy.setPhoneNum("010-1234-5678");
        exactCopy.setEmployeeType("정규직");
        exactCopy.setAddress("서울시 강남구");
        exactCopy.setWorkPlaceName("본사");
        exactCopy.setCommutes(null);  // 컬렉션은 null로 설정
        exactCopy.setOrders(null);
        
        assertEquals(employee1, exactCopy);
        assertEquals(employee1.hashCode(), exactCopy.hashCode());
    }

    @Test
    @DisplayName("should_handleSpecialCharactersInFields_when_settingValues")
    void should_handleSpecialCharactersInFields_when_settingValues() {
        // Given: 특수문자가 포함된 필드값들
        Employee specialEmployee = new Employee();
        specialEmployee.setEmployeeId("EMP-001");
        specialEmployee.setEmployeeName("김철수 Jr.");
        specialEmployee.setEmployeePwd("P@ssw0rd!@#");
        specialEmployee.setPhoneNum("010-1234-5678");
        specialEmployee.setEmployeeType("정규직(팀장)");
        specialEmployee.setAddress("서울시 강남구 테헤란로 123-45 (역삼동)");
        specialEmployee.setWorkPlaceName("본사 1층");
        
        // When & Then: 특수문자가 포함된 값들이 올바르게 처리되어야 함
        assertNotNull(specialEmployee.toString());
        assertEquals("EMP-001", specialEmployee.getEmployeeId());
        assertEquals("김철수 Jr.", specialEmployee.getEmployeeName());
        assertEquals("P@ssw0rd!@#", specialEmployee.getEmployeePwd());
        assertEquals("정규직(팀장)", specialEmployee.getEmployeeType());
    }

    @Test
    @DisplayName("should_maintainConsistentBehavior_when_multipleOperations")
    void should_maintainConsistentBehavior_when_multipleOperations() {
        // Given: Employee 객체
        
        // When: 여러 번의 연속된 연산
        int hashCode1 = employee1.hashCode();
        String toString1 = employee1.toString();
        boolean equals1 = employee1.equals(employee2);
        
        int hashCode2 = employee1.hashCode();
        String toString2 = employee1.toString();
        boolean equals2 = employee1.equals(employee2);
        
        // Then: 일관된 결과를 반환해야 함
        assertEquals(hashCode1, hashCode2);
        assertEquals(toString1, toString2);
        assertEquals(equals1, equals2);
        assertTrue(equals1);
    }

    @Test
    @DisplayName("should_handleEmptyStrings_when_settingFields")
    void should_handleEmptyStrings_when_settingFields() {
        // Given: 빈 문자열을 가진 Employee 객체들
        Employee emptyEmployee1 = new Employee();
        emptyEmployee1.setEmployeeId("");
        emptyEmployee1.setEmployeeName("");
        
        Employee emptyEmployee2 = new Employee();
        emptyEmployee2.setEmployeeId("");
        emptyEmployee2.setEmployeeName("");
        
        // When & Then: 빈 문자열도 적절히 처리되어야 함
        assertEquals(emptyEmployee1, emptyEmployee2);
        assertEquals(emptyEmployee1.hashCode(), emptyEmployee2.hashCode());
        assertNotEquals(employee1, emptyEmployee1);
    }
}