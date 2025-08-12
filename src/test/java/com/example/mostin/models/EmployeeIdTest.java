package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmployeeId 복합키 테스트")
class EmployeeIdTest {

    private EmployeeId employeeId1;
    private EmployeeId employeeId2;
    private EmployeeId employeeId3;

    @BeforeEach
    void setUp() {
        employeeId1 = new EmployeeId();
        employeeId1.setEmployeeId("EMP001");
        employeeId1.setEmployeeName("김철수");

        employeeId2 = new EmployeeId();
        employeeId2.setEmployeeId("EMP001");
        employeeId2.setEmployeeName("김철수");

        employeeId3 = new EmployeeId();
        employeeId3.setEmployeeId("EMP002");
        employeeId3.setEmployeeName("이영희");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameFieldValues")
    void should_returnTrue_when_sameFieldValues() {
        // Given: 같은 필드 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함
        assertEquals(employeeId1, employeeId2);
        assertTrue(employeeId1.equals(employeeId2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentFieldValues")
    void should_returnFalse_when_differentFieldValues() {
        // Given: 다른 필드 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(employeeId1, employeeId3);
        assertFalse(employeeId1.equals(employeeId3));
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingObjects")
    void should_maintainEqualsSymmetry_when_comparingObjects() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(employeeId1.equals(employeeId2));
        assertTrue(employeeId2.equals(employeeId1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameObject")
    void should_maintainEqualsReflexivity_when_comparingSameObject() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함 (자기 자신과 비교시 true)
        assertTrue(employeeId1.equals(employeeId1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingThreeObjects")
    void should_maintainEqualsTransitivity_when_comparingThreeObjects() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        EmployeeId employeeId4 = new EmployeeId();
        employeeId4.setEmployeeId("EMP001");
        employeeId4.setEmployeeName("김철수");
        
        // When & Then: equals 이행성이 유지되어야 함 (A=B, B=C이면 A=C)
        assertTrue(employeeId1.equals(employeeId2));
        assertTrue(employeeId2.equals(employeeId4));
        assertTrue(employeeId1.equals(employeeId4));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_objectsAreEqual")
    void should_returnSameHashCode_when_objectsAreEqual() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: 같은 객체는 같은 hashCode를 가져야 함
        assertEquals(employeeId1.hashCode(), employeeId2.hashCode());
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_calledMultipleTimes")
    void should_returnConsistentHashCode_when_calledMultipleTimes() {
        // Given: 객체
        int firstHashCode = employeeId1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = employeeId1.hashCode();
        int thirdHashCode = employeeId1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_comparedWithNull")
    void should_returnFalse_when_comparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(employeeId1.equals(null));
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: 객체
        
        // When: toString 호출
        String result = employeeId1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("EMP001"));
        assertTrue(result.contains("김철수"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("employeeId="));
        assertTrue(result.contains("employeeName="));
    }

    @Test
    @DisplayName("should_handleNullFields_when_equalsAndHashCodeCalled")
    void should_handleNullFields_when_equalsAndHashCodeCalled() {
        // Given: null 필드를 가진 객체들
        EmployeeId nullEmployeeId1 = new EmployeeId();
        nullEmployeeId1.setEmployeeId(null);
        nullEmployeeId1.setEmployeeName(null);
        
        EmployeeId nullEmployeeId2 = new EmployeeId();
        nullEmployeeId2.setEmployeeId(null);
        nullEmployeeId2.setEmployeeName(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullEmployeeId1, nullEmployeeId2);
        assertEquals(nullEmployeeId1.hashCode(), nullEmployeeId2.hashCode());
        assertNotEquals(employeeId1, nullEmployeeId1);
    }

    @Test
    @DisplayName("should_ensureUniqueCompositeKey_when_differentCombinations")
    void should_ensureUniqueCompositeKey_when_differentCombinations() {
        // Given: 다른 조합의 복합키들
        EmployeeId key1 = new EmployeeId();
        key1.setEmployeeId("EMP001");
        key1.setEmployeeName("김철수");
        
        EmployeeId key2 = new EmployeeId();
        key2.setEmployeeId("EMP001");
        key2.setEmployeeName("이영희");  // 다른 이름
        
        EmployeeId key3 = new EmployeeId();
        key3.setEmployeeId("EMP002");
        key3.setEmployeeName("김철수");  // 다른 ID
        
        // When & Then: 모든 조합이 서로 다른 복합키여야 함
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key2, key3);
        
        // hashCode도 다를 확률이 높아야 함 (완전히 보장되지는 않음)
        assertNotEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }
}