package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ordering 엔티티 테스트")
class OrderingTest {

    private Ordering ordering1;
    private Ordering ordering2;
    private Ordering ordering3;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);

        ordering1 = new Ordering();
        ordering1.setOrderingDay(testDate);
        ordering1.setEmployeeId("EMP001");
        ordering1.setBarcode("8801234567890");
        ordering1.setEmployeeName("김철수");
        ordering1.setBoxNum(10);
        ordering1.setGoodsName("삼성 갤럭시");

        ordering2 = new Ordering();
        ordering2.setOrderingDay(testDate);
        ordering2.setEmployeeId("EMP001");
        ordering2.setBarcode("8801234567890");
        ordering2.setEmployeeName("김철수");
        ordering2.setBoxNum(10);
        ordering2.setGoodsName("삼성 갤럭시");

        ordering3 = new Ordering();
        ordering3.setOrderingDay(LocalDate.of(2024, 1, 16));
        ordering3.setEmployeeId("EMP002");
        ordering3.setBarcode("8801234567891");
        ordering3.setEmployeeName("이영희");
        ordering3.setBoxNum(5);
        ordering3.setGoodsName("애플 아이폰");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameTripleCompositeKeyValues")
    void should_returnTrue_when_sameTripleCompositeKeyValues() {
        // Given: 같은 3중 복합키 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함 (3중 복합키 기준)
        assertEquals(ordering1, ordering2);
        assertTrue(ordering1.equals(ordering2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentTripleCompositeKeyValues")
    void should_returnFalse_when_differentTripleCompositeKeyValues() {
        // Given: 다른 3중 복합키 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(ordering1, ordering3);
        assertFalse(ordering1.equals(ordering3));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_sameTripleCompositeKeyValues")
    void should_returnSameHashCode_when_sameTripleCompositeKeyValues() {
        // Given: 같은 3중 복합키 값을 가진 두 객체
        
        // When & Then: 같은 hashCode를 반환해야 함
        assertEquals(ordering1.hashCode(), ordering2.hashCode());
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: Ordering 객체
        
        // When: toString 호출
        String result = ordering1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("2024-01-15"));
        assertTrue(result.contains("EMP001"));
        assertTrue(result.contains("8801234567890"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("orderingDay="));
        assertTrue(result.contains("employeeId="));
        // Employee는 @ToString.Exclude로 인해 toString에서 제외됨
        assertFalse(result.contains("employee="));
    }

    @Test
    @DisplayName("should_setAndGetAllFields_when_lombokDataUsed")
    void should_setAndGetAllFields_when_lombokDataUsed() {
        // Given: 새로운 Ordering 객체
        Ordering ordering = new Ordering();
        LocalDate newDate = LocalDate.of(2024, 12, 25);
        
        // When: 모든 필드 설정
        ordering.setOrderingDay(newDate);
        ordering.setEmployeeId("EMP999");
        ordering.setBarcode("1234567890123");
        ordering.setEmployeeName("테스트사용자");
        ordering.setBoxNum(25);
        ordering.setGoodsName("테스트상품");
        
        // Then: 모든 getter가 올바르게 동작해야 함
        assertEquals(newDate, ordering.getOrderingDay());
        assertEquals("EMP999", ordering.getEmployeeId());
        assertEquals("1234567890123", ordering.getBarcode());
        assertEquals("테스트사용자", ordering.getEmployeeName());
        assertEquals(25, ordering.getBoxNum());
        assertEquals("테스트상품", ordering.getGoodsName());
    }

    @Test
    @DisplayName("should_maintainLombokDataEqualsContract_when_allFieldsConsidered")
    void should_maintainLombokDataEqualsContract_when_allFieldsConsidered() {
        // Given: 같은 3중 복합키, 다른 비키 필드를 가진 객체들 (Lombok @Data는 모든 필드를 비교)
        Ordering sameKeyDifferentFields = new Ordering();
        sameKeyDifferentFields.setOrderingDay(testDate);
        sameKeyDifferentFields.setEmployeeId("EMP001");
        sameKeyDifferentFields.setBarcode("8801234567890");
        sameKeyDifferentFields.setEmployeeName("다른이름");     // 다른 직원명
        sameKeyDifferentFields.setBoxNum(99);               // 다른 박스 수
        sameKeyDifferentFields.setGoodsName("다른상품명");    // 다른 상품명
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로 비키 필드가 다르면 false여야 함
        assertNotEquals(ordering1, sameKeyDifferentFields);
        assertNotEquals(ordering1.hashCode(), sameKeyDifferentFields.hashCode());
        
        // 모든 필드가 같으면 true여야 함 (Employee 컬렉션 제외)
        Ordering exactCopy = new Ordering();
        exactCopy.setOrderingDay(testDate);
        exactCopy.setEmployeeId("EMP001");
        exactCopy.setBarcode("8801234567890");
        exactCopy.setEmployeeName("김철수");
        exactCopy.setBoxNum(10);
        exactCopy.setGoodsName("삼성 갤럭시");
        exactCopy.setEmployee(null);  // Employee는 null로 설정
        
        assertEquals(ordering1, exactCopy);
        assertEquals(ordering1.hashCode(), exactCopy.hashCode());
    }

    @Test
    @DisplayName("should_handleTripleKeyUniqueness_when_oneFieldDiffers")
    void should_handleTripleKeyUniqueness_when_oneFieldDiffers() {
        // Given: 각각 하나의 키 필드만 다른 객체들
        Ordering differentDate = new Ordering();
        differentDate.setOrderingDay(LocalDate.of(2024, 1, 16)); // 다른 날짜
        differentDate.setEmployeeId("EMP001");
        differentDate.setBarcode("8801234567890");
        
        Ordering differentEmployee = new Ordering();
        differentEmployee.setOrderingDay(testDate);
        differentEmployee.setEmployeeId("EMP002");               // 다른 직원ID
        differentEmployee.setBarcode("8801234567890");
        
        Ordering differentBarcode = new Ordering();
        differentBarcode.setOrderingDay(testDate);
        differentBarcode.setEmployeeId("EMP001");
        differentBarcode.setBarcode("8801234567891");            // 다른 바코드
        
        // When & Then: 모든 객체가 서로 달라야 함
        assertNotEquals(ordering1, differentDate);
        assertNotEquals(ordering1, differentEmployee);
        assertNotEquals(ordering1, differentBarcode);
        assertNotEquals(differentDate, differentEmployee);
        assertNotEquals(differentDate, differentBarcode);
        assertNotEquals(differentEmployee, differentBarcode);
    }

    @Test
    @DisplayName("should_handleBoxNumValues_when_settingIntegerField")
    void should_handleBoxNumValues_when_settingIntegerField() {
        // Given: 다양한 boxNum 값들
        Ordering zeroBoxNum = new Ordering();
        zeroBoxNum.setOrderingDay(testDate);
        zeroBoxNum.setEmployeeId("EMP001");
        zeroBoxNum.setBarcode("8801234567890");
        zeroBoxNum.setBoxNum(0);
        
        Ordering negativeBoxNum = new Ordering();
        negativeBoxNum.setOrderingDay(testDate);
        negativeBoxNum.setEmployeeId("EMP002");
        negativeBoxNum.setBarcode("8801234567891");
        negativeBoxNum.setBoxNum(-5);  // 음수값
        
        Ordering maxBoxNum = new Ordering();
        maxBoxNum.setOrderingDay(testDate);
        maxBoxNum.setEmployeeId("EMP003");
        maxBoxNum.setBarcode("8801234567892");
        maxBoxNum.setBoxNum(Integer.MAX_VALUE);
        
        // When & Then: 다양한 Integer 값들이 올바르게 처리되어야 함
        assertEquals(0, zeroBoxNum.getBoxNum());
        assertEquals(-5, negativeBoxNum.getBoxNum());
        assertEquals(Integer.MAX_VALUE, maxBoxNum.getBoxNum());
        assertNotNull(zeroBoxNum.toString());
        assertNotNull(negativeBoxNum.toString());
        assertNotNull(maxBoxNum.toString());
    }

    @Test
    @DisplayName("should_handleNullBoxNum_when_settingNullInteger")
    void should_handleNullBoxNum_when_settingNullInteger() {
        // Given: null boxNum을 가진 Ordering
        Ordering nullBoxOrdering = new Ordering();
        nullBoxOrdering.setOrderingDay(testDate);
        nullBoxOrdering.setEmployeeId("EMP001");
        nullBoxOrdering.setBarcode("8801234567890");
        nullBoxOrdering.setBoxNum(null);  // null 박스 수
        
        // When & Then: null Integer 필드도 적절히 처리되어야 함
        assertNull(nullBoxOrdering.getBoxNum());
        assertNotNull(nullBoxOrdering.toString());
    }

    @Test
    @DisplayName("should_handleManyToOneRelationship_when_settingEmployee")
    void should_handleManyToOneRelationship_when_settingEmployee() {
        // Given: Employee 객체와 연관관계
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmployeeName("김철수");
        
        // When: 연관관계 설정
        ordering1.setEmployee(employee);
        
        // Then: 연관관계가 올바르게 설정되어야 함
        assertNotNull(ordering1.getEmployee());
        assertEquals(employee, ordering1.getEmployee());
        assertEquals("EMP001", ordering1.getEmployee().getEmployeeId());
        assertEquals("김철수", ordering1.getEmployee().getEmployeeName());
    }

    @Test
    @DisplayName("should_handleSpecialCharactersInGoodsName_when_settingValues")
    void should_handleSpecialCharactersInGoodsName_when_settingValues() {
        // Given: 특수문자가 포함된 상품명들
        Ordering specialGoods = new Ordering();
        specialGoods.setOrderingDay(testDate);
        specialGoods.setEmployeeId("EMP001");
        specialGoods.setBarcode("8801234567890");
        specialGoods.setGoodsName("삼성 갤럭시 S24+ (256GB) - 블랙");
        
        Ordering emojiGoods = new Ordering();
        emojiGoods.setOrderingDay(testDate);
        emojiGoods.setEmployeeId("EMP002");
        emojiGoods.setBarcode("8801234567891");
        emojiGoods.setGoodsName("스마일 😊 스마트폰");
        
        // When & Then: 특수문자가 포함된 상품명도 올바르게 처리되어야 함
        assertEquals("삼성 갤럭시 S24+ (256GB) - 블랙", specialGoods.getGoodsName());
        assertEquals("스마일 😊 스마트폰", emojiGoods.getGoodsName());
        assertNotNull(specialGoods.toString());
        assertNotNull(emojiGoods.toString());
    }

    @Test
    @DisplayName("should_maintainConsistentBehavior_when_multipleOperations")
    void should_maintainConsistentBehavior_when_multipleOperations() {
        // Given: Ordering 객체
        
        // When: 여러 번의 연속된 연산
        int hashCode1 = ordering1.hashCode();
        String toString1 = ordering1.toString();
        boolean equals1 = ordering1.equals(ordering2);
        
        int hashCode2 = ordering1.hashCode();
        String toString2 = ordering1.toString();
        boolean equals2 = ordering1.equals(ordering2);
        
        // Then: 일관된 결과를 반환해야 함
        assertEquals(hashCode1, hashCode2);
        assertEquals(toString1, toString2);
        assertEquals(equals1, equals2);
        assertTrue(equals1);
    }

    @Test
    @DisplayName("should_handleNullTripleCompositeKeyFields_when_equalsAndHashCode")
    void should_handleNullTripleCompositeKeyFields_when_equalsAndHashCode() {
        // Given: null 3중 복합키 필드를 가진 객체들
        Ordering nullOrdering1 = new Ordering();
        nullOrdering1.setOrderingDay(null);
        nullOrdering1.setEmployeeId(null);
        nullOrdering1.setBarcode(null);
        
        Ordering nullOrdering2 = new Ordering();
        nullOrdering2.setOrderingDay(null);
        nullOrdering2.setEmployeeId(null);
        nullOrdering2.setBarcode(null);
        
        // When & Then: null 3중 복합키 필드를 적절히 처리해야 함
        assertEquals(nullOrdering1, nullOrdering2);
        assertEquals(nullOrdering1.hashCode(), nullOrdering2.hashCode());
        assertNotEquals(ordering1, nullOrdering1);
    }

    @Test
    @DisplayName("should_handleComplexTripleKeyScenario_when_multipleFieldCombinations")
    void should_handleComplexTripleKeyScenario_when_multipleFieldCombinations() {
        // Given: 복잡한 3중 키 시나리오 - 8가지 다른 조합
        Ordering[] orderings = new Ordering[8];
        
        // 모든 가능한 조합 생성
        String[] dates = {"2024-01-15", "2024-01-16"};
        String[] employees = {"EMP001", "EMP002"};
        String[] barcodes = {"8801234567890", "8801234567891"};
        
        int index = 0;
        for (String date : dates) {
            for (String employee : employees) {
                for (String barcode : barcodes) {
                    orderings[index] = new Ordering();
                    orderings[index].setOrderingDay(LocalDate.parse(date));
                    orderings[index].setEmployeeId(employee);
                    orderings[index].setBarcode(barcode);
                    index++;
                }
            }
        }
        
        // When & Then: 모든 조합이 서로 달라야 함
        for (int i = 0; i < orderings.length; i++) {
            for (int j = i + 1; j < orderings.length; j++) {
                assertNotEquals(orderings[i], orderings[j], 
                    String.format("Orderings at index %d and %d should be different", i, j));
            }
        }
    }

    @Test
    @DisplayName("should_handleDateAndBarcodeEdgeCases_when_creatingOrdering")
    void should_handleDateAndBarcodeEdgeCases_when_creatingOrdering() {
        // Given: 경계값 날짜와 바코드들
        LocalDate leapDay = LocalDate.of(2024, 2, 29);
        String longBarcode = "12345678901234567890"; // 긴 바코드
        String shortBarcode = "123";                  // 짧은 바코드
        
        Ordering leapDayOrdering = new Ordering();
        leapDayOrdering.setOrderingDay(leapDay);
        leapDayOrdering.setEmployeeId("EMP001");
        leapDayOrdering.setBarcode(longBarcode);
        
        Ordering shortBarcodeOrdering = new Ordering();
        shortBarcodeOrdering.setOrderingDay(testDate);
        shortBarcodeOrdering.setEmployeeId("EMP001");
        shortBarcodeOrdering.setBarcode(shortBarcode);
        
        // When & Then: 극값들도 올바르게 처리되어야 함
        assertEquals(leapDay, leapDayOrdering.getOrderingDay());
        assertEquals(longBarcode, leapDayOrdering.getBarcode());
        assertEquals(shortBarcode, shortBarcodeOrdering.getBarcode());
        assertNotEquals(leapDayOrdering, shortBarcodeOrdering);
        assertNotNull(leapDayOrdering.toString());
        assertNotNull(shortBarcodeOrdering.toString());
    }
}