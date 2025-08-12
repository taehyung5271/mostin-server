package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderingId 3중 복합키 테스트")
class OrderingIdTest {

    private OrderingId orderingId1;
    private OrderingId orderingId2;
    private OrderingId orderingId3;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        
        orderingId1 = new OrderingId();
        orderingId1.setOrderingDay(testDate);
        orderingId1.setEmployeeId("EMP001");
        orderingId1.setBarcode("8801234567890");

        orderingId2 = new OrderingId();
        orderingId2.setOrderingDay(testDate);
        orderingId2.setEmployeeId("EMP001");
        orderingId2.setBarcode("8801234567890");

        orderingId3 = new OrderingId();
        orderingId3.setOrderingDay(LocalDate.of(2024, 1, 16));
        orderingId3.setEmployeeId("EMP002");
        orderingId3.setBarcode("8801234567891");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameTripleKeyValues")
    void should_returnTrue_when_sameTripleKeyValues() {
        // Given: 같은 3중 필드 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함
        assertEquals(orderingId1, orderingId2);
        assertTrue(orderingId1.equals(orderingId2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentTripleKeyValues")
    void should_returnFalse_when_differentTripleKeyValues() {
        // Given: 다른 3중 필드 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(orderingId1, orderingId3);
        assertFalse(orderingId1.equals(orderingId3));
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingTripleKeyObjects")
    void should_maintainEqualsSymmetry_when_comparingTripleKeyObjects() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(orderingId1.equals(orderingId2));
        assertTrue(orderingId2.equals(orderingId1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameTripleKeyObject")
    void should_maintainEqualsReflexivity_when_comparingSameTripleKeyObject() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함
        assertTrue(orderingId1.equals(orderingId1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingTripleKeyObjects")
    void should_maintainEqualsTransitivity_when_comparingTripleKeyObjects() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        OrderingId orderingId4 = new OrderingId();
        orderingId4.setOrderingDay(testDate);
        orderingId4.setEmployeeId("EMP001");
        orderingId4.setBarcode("8801234567890");
        
        // When & Then: equals 이행성이 유지되어야 함
        assertTrue(orderingId1.equals(orderingId2));
        assertTrue(orderingId2.equals(orderingId4));
        assertTrue(orderingId1.equals(orderingId4));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_tripleKeyObjectsAreEqual")
    void should_returnSameHashCode_when_tripleKeyObjectsAreEqual() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: 같은 객체는 같은 hashCode를 가져야 함
        assertEquals(orderingId1.hashCode(), orderingId2.hashCode());
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_tripleKeyCalledMultipleTimes")
    void should_returnConsistentHashCode_when_tripleKeyCalledMultipleTimes() {
        // Given: 객체
        int firstHashCode = orderingId1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = orderingId1.hashCode();
        int thirdHashCode = orderingId1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_tripleKeyComparedWithNull")
    void should_returnFalse_when_tripleKeyComparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(orderingId1.equals(null));
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_tripleKeyToStringCalled")
    void should_returnProperStringFormat_when_tripleKeyToStringCalled() {
        // Given: 객체
        
        // When: toString 호출
        String result = orderingId1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("2024-01-15"));
        assertTrue(result.contains("EMP001"));
        assertTrue(result.contains("8801234567890"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("orderingDay="));
        assertTrue(result.contains("employeeId="));
        assertTrue(result.contains("barcode="));
    }

    @Test
    @DisplayName("should_handleNullFields_when_tripleKeyEqualsAndHashCodeCalled")
    void should_handleNullFields_when_tripleKeyEqualsAndHashCodeCalled() {
        // Given: null 필드를 가진 객체들
        OrderingId nullOrderingId1 = new OrderingId();
        nullOrderingId1.setOrderingDay(null);
        nullOrderingId1.setEmployeeId(null);
        nullOrderingId1.setBarcode(null);
        
        OrderingId nullOrderingId2 = new OrderingId();
        nullOrderingId2.setOrderingDay(null);
        nullOrderingId2.setEmployeeId(null);
        nullOrderingId2.setBarcode(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullOrderingId1, nullOrderingId2);
        assertEquals(nullOrderingId1.hashCode(), nullOrderingId2.hashCode());
        assertNotEquals(orderingId1, nullOrderingId1);
    }

    @Test
    @DisplayName("should_ensureUniqueTripleCompositeKey_when_differentCombinations")
    void should_ensureUniqueTripleCompositeKey_when_differentCombinations() {
        // Given: 각각 하나씩 다른 필드를 가진 복합키들
        OrderingId key1 = new OrderingId();
        key1.setOrderingDay(LocalDate.of(2024, 1, 15));
        key1.setEmployeeId("EMP001");
        key1.setBarcode("8801234567890");
        
        // 날짜만 다름
        OrderingId key2 = new OrderingId();
        key2.setOrderingDay(LocalDate.of(2024, 1, 16));
        key2.setEmployeeId("EMP001");
        key2.setBarcode("8801234567890");
        
        // 직원ID만 다름
        OrderingId key3 = new OrderingId();
        key3.setOrderingDay(LocalDate.of(2024, 1, 15));
        key3.setEmployeeId("EMP002");
        key3.setBarcode("8801234567890");
        
        // 바코드만 다름
        OrderingId key4 = new OrderingId();
        key4.setOrderingDay(LocalDate.of(2024, 1, 15));
        key4.setEmployeeId("EMP001");
        key4.setBarcode("8801234567891");
        
        // When & Then: 모든 조합이 서로 다른 복합키여야 함
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key1, key4);
        assertNotEquals(key2, key3);
        assertNotEquals(key2, key4);
        assertNotEquals(key3, key4);
    }

    @Test
    @DisplayName("should_handleTripleKeyComplexScenario_when_multipleFieldChanges")
    void should_handleTripleKeyComplexScenario_when_multipleFieldChanges() {
        // Given: 복잡한 시나리오 - 두 필드가 다른 경우들
        OrderingId baseKey = new OrderingId();
        baseKey.setOrderingDay(LocalDate.of(2024, 1, 15));
        baseKey.setEmployeeId("EMP001");
        baseKey.setBarcode("8801234567890");
        
        // 날짜와 직원ID가 다름
        OrderingId key1 = new OrderingId();
        key1.setOrderingDay(LocalDate.of(2024, 1, 16));
        key1.setEmployeeId("EMP002");
        key1.setBarcode("8801234567890");
        
        // 날짜와 바코드가 다름
        OrderingId key2 = new OrderingId();
        key2.setOrderingDay(LocalDate.of(2024, 1, 16));
        key2.setEmployeeId("EMP001");
        key2.setBarcode("8801234567891");
        
        // 직원ID와 바코드가 다름
        OrderingId key3 = new OrderingId();
        key3.setOrderingDay(LocalDate.of(2024, 1, 15));
        key3.setEmployeeId("EMP002");
        key3.setBarcode("8801234567891");
        
        // When & Then: 모든 경우가 서로 다른 키여야 함
        assertNotEquals(baseKey, key1);
        assertNotEquals(baseKey, key2);
        assertNotEquals(baseKey, key3);
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key2, key3);
    }

    @Test
    @DisplayName("should_maintainTripleKeyUniqueness_when_allFieldsCombined")
    void should_maintainTripleKeyUniqueness_when_allFieldsCombined() {
        // Given: 서로 다른 모든 필드 조합의 키들
        OrderingId[] keys = {
            createOrderingId("2024-01-15", "EMP001", "8801234567890"),
            createOrderingId("2024-01-15", "EMP001", "8801234567891"),
            createOrderingId("2024-01-15", "EMP002", "8801234567890"),
            createOrderingId("2024-01-15", "EMP002", "8801234567891"),
            createOrderingId("2024-01-16", "EMP001", "8801234567890"),
            createOrderingId("2024-01-16", "EMP001", "8801234567891"),
            createOrderingId("2024-01-16", "EMP002", "8801234567890"),
            createOrderingId("2024-01-16", "EMP002", "8801234567891")
        };
        
        // When & Then: 모든 키가 서로 달라야 함
        for (int i = 0; i < keys.length; i++) {
            for (int j = i + 1; j < keys.length; j++) {
                assertNotEquals(keys[i], keys[j], 
                    String.format("Keys at index %d and %d should be different", i, j));
            }
        }
    }
    
    private OrderingId createOrderingId(String date, String employeeId, String barcode) {
        OrderingId id = new OrderingId();
        id.setOrderingDay(LocalDate.parse(date));
        id.setEmployeeId(employeeId);
        id.setBarcode(barcode);
        return id;
    }
}