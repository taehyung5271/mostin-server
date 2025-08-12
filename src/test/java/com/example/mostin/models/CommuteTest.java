package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commute 엔티티 테스트")
class CommuteTest {

    private Commute commute1;
    private Commute commute2;
    private Commute commute3;
    private LocalDate testDate;
    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.of(2024, 1, 15);
        startTime = LocalTime.of(9, 0, 0);
        endTime = LocalTime.of(18, 0, 0);

        commute1 = new Commute();
        commute1.setCommuteDay(testDate);
        commute1.setEmployeeId("EMP001");
        commute1.setEmployeeName("김철수");
        commute1.setStartTime(startTime);
        commute1.setEndTime(endTime);
        commute1.setWorkPlaceName("본사");

        commute2 = new Commute();
        commute2.setCommuteDay(testDate);
        commute2.setEmployeeId("EMP001");
        commute2.setEmployeeName("김철수");
        commute2.setStartTime(startTime);
        commute2.setEndTime(endTime);
        commute2.setWorkPlaceName("본사");

        commute3 = new Commute();
        commute3.setCommuteDay(LocalDate.of(2024, 1, 16));
        commute3.setEmployeeId("EMP002");
        commute3.setEmployeeName("이영희");
        commute3.setStartTime(LocalTime.of(8, 30, 0));
        commute3.setEndTime(LocalTime.of(17, 30, 0));
        commute3.setWorkPlaceName("지점A");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameCompositeKeyValues")
    void should_returnTrue_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함 (복합키 기준)
        assertEquals(commute1, commute2);
        assertTrue(commute1.equals(commute2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentCompositeKeyValues")
    void should_returnFalse_when_differentCompositeKeyValues() {
        // Given: 다른 복합키 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(commute1, commute3);
        assertFalse(commute1.equals(commute3));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_sameCompositeKeyValues")
    void should_returnSameHashCode_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: 같은 hashCode를 반환해야 함
        assertEquals(commute1.hashCode(), commute2.hashCode());
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: Commute 객체
        
        // When: toString 호출
        String result = commute1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("2024-01-15"));
        assertTrue(result.contains("EMP001"));
        assertTrue(result.contains("김철수"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("commuteDay="));
        assertTrue(result.contains("employeeId="));
    }

    @Test
    @DisplayName("should_setAndGetAllFields_when_lombokDataUsed")
    void should_setAndGetAllFields_when_lombokDataUsed() {
        // Given: 새로운 Commute 객체
        Commute commute = new Commute();
        LocalDate newDate = LocalDate.of(2024, 12, 25);
        LocalTime newStartTime = LocalTime.of(10, 30, 15);
        LocalTime newEndTime = LocalTime.of(19, 45, 30);
        
        // When: 모든 필드 설정
        commute.setCommuteDay(newDate);
        commute.setEmployeeId("EMP999");
        commute.setEmployeeName("테스트사용자");
        commute.setStartTime(newStartTime);
        commute.setEndTime(newEndTime);
        commute.setWorkPlaceName("테스트근무지");
        
        // Then: 모든 getter가 올바르게 동작해야 함
        assertEquals(newDate, commute.getCommuteDay());
        assertEquals("EMP999", commute.getEmployeeId());
        assertEquals("테스트사용자", commute.getEmployeeName());
        assertEquals(newStartTime, commute.getStartTime());
        assertEquals(newEndTime, commute.getEndTime());
        assertEquals("테스트근무지", commute.getWorkPlaceName());
    }

    @Test
    @DisplayName("should_handleLocalDateTimeFields_when_equalsAndHashCode")
    void should_handleLocalDateTimeFields_when_equalsAndHashCode() {
        // Given: 다른 인스턴스지만 같은 시간을 나타내는 LocalDate/LocalTime 객체들
        LocalDate date1 = LocalDate.of(2024, 1, 15);
        LocalDate date2 = LocalDate.parse("2024-01-15");
        LocalTime time1 = LocalTime.of(9, 0, 0);
        LocalTime time2 = LocalTime.parse("09:00:00");
        
        Commute c1 = new Commute();
        c1.setCommuteDay(date1);
        c1.setEmployeeId("EMP001");
        c1.setStartTime(time1);
        c1.setEndTime(LocalTime.of(18, 0, 0));
        
        Commute c2 = new Commute();
        c2.setCommuteDay(date2);
        c2.setEmployeeId("EMP001");
        c2.setStartTime(time2);
        c2.setEndTime(LocalTime.of(18, 0, 0));
        
        // When & Then: LocalDate/LocalTime의 equals가 올바르게 동작해야 함
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    @DisplayName("should_handleNullTimeFields_when_settingValues")
    void should_handleNullTimeFields_when_settingValues() {
        // Given: null 시간 필드를 가진 Commute 객체
        Commute nullTimeCommute = new Commute();
        nullTimeCommute.setCommuteDay(testDate);
        nullTimeCommute.setEmployeeId("EMP001");
        nullTimeCommute.setEmployeeName("김철수");
        nullTimeCommute.setStartTime(null);  // null 시작시간
        nullTimeCommute.setEndTime(null);    // null 종료시간
        nullTimeCommute.setWorkPlaceName("본사");
        
        // When & Then: null 시간 필드도 적절히 처리되어야 함
        assertNotNull(nullTimeCommute.toString());
        assertNull(nullTimeCommute.getStartTime());
        assertNull(nullTimeCommute.getEndTime());
        assertEquals(testDate, nullTimeCommute.getCommuteDay());
    }

    @Test
    @DisplayName("should_maintainLombokDataEqualsContract_when_allFieldsConsidered")
    void should_maintainLombokDataEqualsContract_when_allFieldsConsidered() {
        // Given: 같은 복합키, 다른 비키 필드를 가진 객체들 (Lombok @Data는 모든 필드를 비교)
        Commute sameKeyDifferentFields = new Commute();
        sameKeyDifferentFields.setCommuteDay(testDate);
        sameKeyDifferentFields.setEmployeeId("EMP001");
        sameKeyDifferentFields.setEmployeeName("다른이름");           // 다른 직원명
        sameKeyDifferentFields.setStartTime(LocalTime.of(8, 0, 0)); // 다른 시작시간
        sameKeyDifferentFields.setEndTime(LocalTime.of(17, 0, 0));  // 다른 종료시간
        sameKeyDifferentFields.setWorkPlaceName("다른근무지");        // 다른 근무지
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로 비키 필드가 다르면 false여야 함
        assertNotEquals(commute1, sameKeyDifferentFields);
        assertNotEquals(commute1.hashCode(), sameKeyDifferentFields.hashCode());
        
        // 모든 필드가 같으면 true여야 함
        Commute exactCopy = new Commute();
        exactCopy.setCommuteDay(testDate);
        exactCopy.setEmployeeId("EMP001");
        exactCopy.setEmployeeName("김철수");
        exactCopy.setStartTime(startTime);
        exactCopy.setEndTime(endTime);
        exactCopy.setWorkPlaceName("본사");
        
        assertEquals(commute1, exactCopy);
        assertEquals(commute1.hashCode(), exactCopy.hashCode());
    }

    @Test
    @DisplayName("should_handleTimeEdgeCases_when_settingTimeValues")
    void should_handleTimeEdgeCases_when_settingTimeValues() {
        // Given: 경계값 시간들
        LocalTime midnight = LocalTime.MIDNIGHT;       // 00:00:00
        LocalTime noon = LocalTime.NOON;              // 12:00:00
        LocalTime maxTime = LocalTime.MAX;            // 23:59:59.999999999
        LocalTime minTime = LocalTime.MIN;            // 00:00:00
        
        Commute midnightCommute = new Commute();
        midnightCommute.setCommuteDay(testDate);
        midnightCommute.setEmployeeId("EMP001");
        midnightCommute.setStartTime(midnight);
        midnightCommute.setEndTime(noon);
        
        Commute maxTimeCommute = new Commute();
        maxTimeCommute.setCommuteDay(testDate);
        maxTimeCommute.setEmployeeId("EMP002");
        maxTimeCommute.setStartTime(minTime);
        maxTimeCommute.setEndTime(maxTime);
        
        // When & Then: 극값 시간들도 올바르게 처리되어야 함
        assertNotNull(midnightCommute.toString());
        assertNotNull(maxTimeCommute.toString());
        assertEquals(midnight, midnightCommute.getStartTime());
        assertEquals(noon, midnightCommute.getEndTime());
        assertEquals(minTime, maxTimeCommute.getStartTime());
        assertEquals(maxTime, maxTimeCommute.getEndTime());
    }

    @Test
    @DisplayName("should_handleDateEdgeCases_when_settingDateValues")
    void should_handleDateEdgeCases_when_settingDateValues() {
        // Given: 경계값 날짜들
        LocalDate leapDay = LocalDate.of(2024, 2, 29);  // 윤년
        LocalDate yearEnd = LocalDate.of(2024, 12, 31); // 연말
        LocalDate yearStart = LocalDate.of(2024, 1, 1); // 연초
        
        Commute leapDayCommute = new Commute();
        leapDayCommute.setCommuteDay(leapDay);
        leapDayCommute.setEmployeeId("EMP001");
        leapDayCommute.setStartTime(startTime);
        leapDayCommute.setEndTime(endTime);
        
        // When & Then: 특수한 날짜들도 올바르게 처리되어야 함
        assertNotNull(leapDayCommute.toString());
        assertEquals(leapDay, leapDayCommute.getCommuteDay());
        assertEquals(29, leapDayCommute.getCommuteDay().getDayOfMonth());
        assertEquals(2, leapDayCommute.getCommuteDay().getMonthValue());
    }

    @Test
    @DisplayName("should_handleManyToOneRelationship_when_settingEmployee")
    void should_handleManyToOneRelationship_when_settingEmployee() {
        // Given: Employee 객체와 연관관계
        Employee employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setEmployeeName("김철수");
        
        // When: 연관관계 설정
        commute1.setEmployee(employee);
        
        // Then: 연관관계가 올바르게 설정되어야 함
        assertNotNull(commute1.getEmployee());
        assertEquals(employee, commute1.getEmployee());
        assertEquals("EMP001", commute1.getEmployee().getEmployeeId());
        assertEquals("김철수", commute1.getEmployee().getEmployeeName());
    }

    @Test
    @DisplayName("should_maintainConsistentBehavior_when_multipleOperations")
    void should_maintainConsistentBehavior_when_multipleOperations() {
        // Given: Commute 객체
        
        // When: 여러 번의 연속된 연산
        int hashCode1 = commute1.hashCode();
        String toString1 = commute1.toString();
        boolean equals1 = commute1.equals(commute2);
        
        int hashCode2 = commute1.hashCode();
        String toString2 = commute1.toString();
        boolean equals2 = commute1.equals(commute2);
        
        // Then: 일관된 결과를 반환해야 함
        assertEquals(hashCode1, hashCode2);
        assertEquals(toString1, toString2);
        assertEquals(equals1, equals2);
        assertTrue(equals1);
    }

    @Test
    @DisplayName("should_handleNullCompositeKeyFields_when_equalsAndHashCode")
    void should_handleNullCompositeKeyFields_when_equalsAndHashCode() {
        // Given: null 복합키 필드를 가진 객체들
        Commute nullCommute1 = new Commute();
        nullCommute1.setCommuteDay(null);
        nullCommute1.setEmployeeId(null);
        
        Commute nullCommute2 = new Commute();
        nullCommute2.setCommuteDay(null);
        nullCommute2.setEmployeeId(null);
        
        // When & Then: null 복합키 필드를 적절히 처리해야 함
        assertEquals(nullCommute1, nullCommute2);
        assertEquals(nullCommute1.hashCode(), nullCommute2.hashCode());
        assertNotEquals(commute1, nullCommute1);
    }
}