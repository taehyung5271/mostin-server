package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CommuteId 복합키 테스트")
class CommuteIdTest {

    private CommuteId commuteId1;
    private CommuteId commuteId2;
    private CommuteId commuteId3;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        
        commuteId1 = new CommuteId();
        commuteId1.setCommuteDay(testDate);
        commuteId1.setEmployeeId("EMP001");

        commuteId2 = new CommuteId();
        commuteId2.setCommuteDay(testDate);
        commuteId2.setEmployeeId("EMP001");

        commuteId3 = new CommuteId();
        commuteId3.setCommuteDay(LocalDate.of(2024, 1, 16));
        commuteId3.setEmployeeId("EMP002");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameFieldValues")
    void should_returnTrue_when_sameFieldValues() {
        // Given: 같은 필드 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함
        assertEquals(commuteId1, commuteId2);
        assertTrue(commuteId1.equals(commuteId2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentFieldValues")
    void should_returnFalse_when_differentFieldValues() {
        // Given: 다른 필드 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(commuteId1, commuteId3);
        assertFalse(commuteId1.equals(commuteId3));
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingObjects")
    void should_maintainEqualsSymmetry_when_comparingObjects() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(commuteId1.equals(commuteId2));
        assertTrue(commuteId2.equals(commuteId1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameObject")
    void should_maintainEqualsReflexivity_when_comparingSameObject() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함
        assertTrue(commuteId1.equals(commuteId1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingThreeObjects")
    void should_maintainEqualsTransitivity_when_comparingThreeObjects() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        CommuteId commuteId4 = new CommuteId();
        commuteId4.setCommuteDay(testDate);
        commuteId4.setEmployeeId("EMP001");
        
        // When & Then: equals 이행성이 유지되어야 함
        assertTrue(commuteId1.equals(commuteId2));
        assertTrue(commuteId2.equals(commuteId4));
        assertTrue(commuteId1.equals(commuteId4));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_objectsAreEqual")
    void should_returnSameHashCode_when_objectsAreEqual() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: 같은 객체는 같은 hashCode를 가져야 함
        assertEquals(commuteId1.hashCode(), commuteId2.hashCode());
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_calledMultipleTimes")
    void should_returnConsistentHashCode_when_calledMultipleTimes() {
        // Given: 객체
        int firstHashCode = commuteId1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = commuteId1.hashCode();
        int thirdHashCode = commuteId1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_comparedWithNull")
    void should_returnFalse_when_comparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(commuteId1.equals(null));
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: 객체
        
        // When: toString 호출
        String result = commuteId1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("2024-01-15"));
        assertTrue(result.contains("EMP001"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("commuteDay="));
        assertTrue(result.contains("employeeId="));
    }

    @Test
    @DisplayName("should_handleLocalDateEquals_when_differentDateInstances")
    void should_handleLocalDateEquals_when_differentDateInstances() {
        // Given: 다른 인스턴스지만 같은 날짜를 나타내는 LocalDate 객체들
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.parse("2024-01-15");
        
        CommuteId id1 = new CommuteId();
        id1.setCommuteDay(date1);
        id1.setEmployeeId("EMP001");
        
        CommuteId id2 = new CommuteId();
        id2.setCommuteDay(date2);
        id2.setEmployeeId("EMP001");
        
        // When & Then: LocalDate의 equals가 올바르게 동작해야 함
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    @DisplayName("should_handleNullFields_when_equalsAndHashCodeCalled")
    void should_handleNullFields_when_equalsAndHashCodeCalled() {
        // Given: null 필드를 가진 객체들
        CommuteId nullCommuteId1 = new CommuteId();
        nullCommuteId1.setCommuteDay(null);
        nullCommuteId1.setEmployeeId(null);
        
        CommuteId nullCommuteId2 = new CommuteId();
        nullCommuteId2.setCommuteDay(null);
        nullCommuteId2.setEmployeeId(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullCommuteId1, nullCommuteId2);
        assertEquals(nullCommuteId1.hashCode(), nullCommuteId2.hashCode());
        assertNotEquals(commuteId1, nullCommuteId1);
    }

    @Test
    @DisplayName("should_ensureUniqueCompositeKey_when_differentCombinations")
    void should_ensureUniqueCompositeKey_when_differentCombinations() {
        // Given: 다른 조합의 복합키들
        CommuteId key1 = new CommuteId();
        key1.setCommuteDay(LocalDate.of(2024, 1, 15));
        key1.setEmployeeId("EMP001");
        
        CommuteId key2 = new CommuteId();
        key2.setCommuteDay(LocalDate.of(2024, 1, 15));
        key2.setEmployeeId("EMP002");  // 다른 직원
        
        CommuteId key3 = new CommuteId();
        key3.setCommuteDay(LocalDate.of(2024, 1, 16));  // 다른 날짜
        key3.setEmployeeId("EMP001");
        
        // When & Then: 모든 조합이 서로 다른 복합키여야 함
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key2, key3);
    }

    @Test
    @DisplayName("should_handleDateEdgeCases_when_creatingCompositeKey")
    void should_handleDateEdgeCases_when_creatingCompositeKey() {
        // Given: 경계값 날짜들
        LocalDate minDate = LocalDate.MIN;
        LocalDate maxDate = LocalDate.MAX;
        LocalDate leapDate = LocalDate.of(2024, 2, 29); // 윤년
        
        CommuteId minDateId = new CommuteId();
        minDateId.setCommuteDay(minDate);
        minDateId.setEmployeeId("EMP001");
        
        CommuteId maxDateId = new CommuteId();
        maxDateId.setCommuteDay(maxDate);
        maxDateId.setEmployeeId("EMP001");
        
        CommuteId leapDateId = new CommuteId();
        leapDateId.setCommuteDay(leapDate);
        leapDateId.setEmployeeId("EMP001");
        
        // When & Then: 극값 날짜들도 올바르게 처리되어야 함
        assertNotNull(minDateId.toString());
        assertNotNull(maxDateId.toString());
        assertNotNull(leapDateId.toString());
        
        assertNotEquals(minDateId, maxDateId);
        assertNotEquals(minDateId, leapDateId);
        assertNotEquals(maxDateId, leapDateId);
    }
}