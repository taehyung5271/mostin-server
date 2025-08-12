package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("WorkPlace 엔티티 테스트")
class WorkPlaceTest {

    private WorkPlace workPlace1;
    private WorkPlace workPlace2;
    private WorkPlace workPlace3;

    @BeforeEach
    void setUp() {
        workPlace1 = new WorkPlace();
        workPlace1.setWorkPlaceName("본사");
        workPlace1.setLatitude(37.5665);
        workPlace1.setLongitude(126.9780);

        workPlace2 = new WorkPlace();
        workPlace2.setWorkPlaceName("본사");
        workPlace2.setLatitude(37.5665);
        workPlace2.setLongitude(126.9780);

        workPlace3 = new WorkPlace();
        workPlace3.setWorkPlaceName("지점A");
        workPlace3.setLatitude(37.5172);
        workPlace3.setLongitude(127.0473);
    }

    @Test
    @DisplayName("should_returnTrue_when_samePrimaryKeyValues")
    void should_returnTrue_when_samePrimaryKeyValues() {
        // Given: 같은 기본키 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함 (기본키 기준)
        assertEquals(workPlace1, workPlace2);
        assertTrue(workPlace1.equals(workPlace2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentPrimaryKeyValues")
    void should_returnFalse_when_differentPrimaryKeyValues() {
        // Given: 다른 기본키 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(workPlace1, workPlace3);
        assertFalse(workPlace1.equals(workPlace3));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_samePrimaryKeyValues")
    void should_returnSameHashCode_when_samePrimaryKeyValues() {
        // Given: 같은 기본키 값을 가진 두 객체
        
        // When & Then: 같은 hashCode를 반환해야 함
        assertEquals(workPlace1.hashCode(), workPlace2.hashCode());
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: WorkPlace 객체
        
        // When: toString 호출
        String result = workPlace1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("본사"));
        assertTrue(result.contains("37.5665"));
        assertTrue(result.contains("126.978"));
        // Lombok은 클래스명을 포함하지 않고 필드명=값 형식으로 생성함
        assertTrue(result.contains("workPlaceName="));
        assertTrue(result.contains("latitude="));
        assertTrue(result.contains("longitude="));
    }

    @Test
    @DisplayName("should_setAndGetAllFields_when_lombokDataUsed")
    void should_setAndGetAllFields_when_lombokDataUsed() {
        // Given: 새로운 WorkPlace 객체
        WorkPlace workPlace = new WorkPlace();
        
        // When: 모든 필드 설정
        workPlace.setWorkPlaceName("테스트 지점");
        workPlace.setLatitude(35.1796);  // 부산시 위도
        workPlace.setLongitude(129.0756); // 부산시 경도
        
        // Then: 모든 getter가 올바르게 동작해야 함
        assertEquals("테스트 지점", workPlace.getWorkPlaceName());
        assertEquals(35.1796, workPlace.getLatitude(), 0.0001);
        assertEquals(129.0756, workPlace.getLongitude(), 0.0001);
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingWorkPlaces")
    void should_maintainEqualsSymmetry_when_comparingWorkPlaces() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(workPlace1.equals(workPlace2));
        assertTrue(workPlace2.equals(workPlace1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameWorkPlace")
    void should_maintainEqualsReflexivity_when_comparingSameWorkPlace() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함
        assertTrue(workPlace1.equals(workPlace1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingThreeWorkPlaces")
    void should_maintainEqualsTransitivity_when_comparingThreeWorkPlaces() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        WorkPlace workPlace4 = new WorkPlace();
        workPlace4.setWorkPlaceName("본사");
        workPlace4.setLatitude(37.5665);
        workPlace4.setLongitude(126.9780);
        
        // When & Then: equals 이행성이 유지되어야 함
        assertTrue(workPlace1.equals(workPlace2));
        assertTrue(workPlace2.equals(workPlace4));
        assertTrue(workPlace1.equals(workPlace4));
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_calledMultipleTimes")
    void should_returnConsistentHashCode_when_calledMultipleTimes() {
        // Given: WorkPlace 객체
        int firstHashCode = workPlace1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = workPlace1.hashCode();
        int thirdHashCode = workPlace1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_comparedWithNull")
    void should_returnFalse_when_comparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(workPlace1.equals(null));
    }

    @Test
    @DisplayName("should_maintainLombokDataEqualsContract_when_allFieldsConsidered")
    void should_maintainLombokDataEqualsContract_when_allFieldsConsidered() {
        // Given: 같은 이름, 다른 좌표를 가진 객체들 (Lombok @Data는 모든 필드를 비교)
        WorkPlace sameNameDifferentLocation = new WorkPlace();
        sameNameDifferentLocation.setWorkPlaceName("본사");
        sameNameDifferentLocation.setLatitude(35.1796);    // 다른 위도
        sameNameDifferentLocation.setLongitude(129.0756);  // 다른 경도
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로 좌표가 다르면 false여야 함
        assertNotEquals(workPlace1, sameNameDifferentLocation);
        assertNotEquals(workPlace1.hashCode(), sameNameDifferentLocation.hashCode());
        
        // 모든 필드가 같으면 true여야 함
        WorkPlace exactCopy = new WorkPlace();
        exactCopy.setWorkPlaceName("본사");
        exactCopy.setLatitude(37.5665);
        exactCopy.setLongitude(126.9780);
        
        assertEquals(workPlace1, exactCopy);
        assertEquals(workPlace1.hashCode(), exactCopy.hashCode());
    }

    @Test
    @DisplayName("should_handleNullWorkPlaceName_when_equalsAndHashCodeCalled")
    void should_handleNullWorkPlaceName_when_equalsAndHashCodeCalled() {
        // Given: null 이름을 가진 객체들 (Lombok @Data는 모든 필드를 비교)
        WorkPlace nullWorkPlace1 = new WorkPlace();
        nullWorkPlace1.setWorkPlaceName(null);
        nullWorkPlace1.setLatitude(37.5665);
        nullWorkPlace1.setLongitude(126.9780);
        
        WorkPlace nullWorkPlace2 = new WorkPlace();
        nullWorkPlace2.setWorkPlaceName(null);
        nullWorkPlace2.setLatitude(37.5665);  // 같은 위도
        nullWorkPlace2.setLongitude(126.9780); // 같은 경도
        
        WorkPlace nullWorkPlace3 = new WorkPlace();
        nullWorkPlace3.setWorkPlaceName(null);
        nullWorkPlace3.setLatitude(35.1796);  // 다른 위도
        nullWorkPlace3.setLongitude(129.0756); // 다른 경도
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로
        assertEquals(nullWorkPlace1, nullWorkPlace2);  // 모든 필드가 같음
        assertEquals(nullWorkPlace1.hashCode(), nullWorkPlace2.hashCode());
        
        assertNotEquals(nullWorkPlace1, nullWorkPlace3);  // 좌표가 다름
        assertNotEquals(workPlace1, nullWorkPlace1);      // 이름이 다름
    }

    @Test
    @DisplayName("should_handleSpecialCharactersInWorkPlaceName_when_settingValues")
    void should_handleSpecialCharactersInWorkPlaceName_when_settingValues() {
        // Given: 특수문자가 포함된 근무지명들
        WorkPlace specialChars = new WorkPlace();
        specialChars.setWorkPlaceName("본사 1층 (IT부서)");
        specialChars.setLatitude(37.5665);
        specialChars.setLongitude(126.9780);
        
        WorkPlace withNumbers = new WorkPlace();
        withNumbers.setWorkPlaceName("지점 A-1 (2024년 신설)");
        withNumbers.setLatitude(37.5172);
        withNumbers.setLongitude(127.0473);
        
        WorkPlace withSymbols = new WorkPlace();
        withSymbols.setWorkPlaceName("서울지점 & 부산지점");
        withSymbols.setLatitude(37.5665);
        withSymbols.setLongitude(126.9780);
        
        // When & Then: 특수문자가 포함된 근무지명도 올바르게 처리되어야 함
        assertEquals("본사 1층 (IT부서)", specialChars.getWorkPlaceName());
        assertEquals("지점 A-1 (2024년 신설)", withNumbers.getWorkPlaceName());
        assertEquals("서울지점 & 부산지점", withSymbols.getWorkPlaceName());
        
        assertNotEquals(specialChars, withNumbers);
        assertNotEquals(withNumbers, withSymbols);
        assertNotEquals(specialChars, withSymbols);
    }

    @Test
    @DisplayName("should_handleCoordinateEdgeCases_when_settingLatitudeLongitude")
    void should_handleCoordinateEdgeCases_when_settingLatitudeLongitude() {
        // Given: 극값 좌표들
        WorkPlace northPole = new WorkPlace();
        northPole.setWorkPlaceName("북극점");
        northPole.setLatitude(90.0);   // 최대 위도
        northPole.setLongitude(0.0);
        
        WorkPlace southPole = new WorkPlace();
        southPole.setWorkPlaceName("남극점");
        southPole.setLatitude(-90.0);  // 최소 위도
        southPole.setLongitude(0.0);
        
        WorkPlace eastmost = new WorkPlace();
        eastmost.setWorkPlaceName("동쪽 끝");
        eastmost.setLatitude(0.0);
        eastmost.setLongitude(180.0);  // 최대 경도
        
        WorkPlace westmost = new WorkPlace();
        westmost.setWorkPlaceName("서쪽 끝");
        westmost.setLatitude(0.0);
        westmost.setLongitude(-180.0); // 최소 경도
        
        // When & Then: 극값 좌표들도 올바르게 처리되어야 함
        assertEquals(90.0, northPole.getLatitude(), 0.0001);
        assertEquals(-90.0, southPole.getLatitude(), 0.0001);
        assertEquals(180.0, eastmost.getLongitude(), 0.0001);
        assertEquals(-180.0, westmost.getLongitude(), 0.0001);
        
        assertNotEquals(northPole, southPole);
        assertNotEquals(eastmost, westmost);
        
        assertNotNull(northPole.toString());
        assertNotNull(southPole.toString());
        assertNotNull(eastmost.toString());
        assertNotNull(westmost.toString());
    }

    @Test
    @DisplayName("should_handlePreciseCoordinates_when_settingDoubleValues")
    void should_handlePreciseCoordinates_when_settingDoubleValues() {
        // Given: 정밀한 좌표값들
        WorkPlace preciseLocation1 = new WorkPlace();
        preciseLocation1.setWorkPlaceName("정밀위치1");
        preciseLocation1.setLatitude(37.56656789123456);
        preciseLocation1.setLongitude(126.97801234567890);
        
        WorkPlace preciseLocation2 = new WorkPlace();
        preciseLocation2.setWorkPlaceName("정밀위치2");
        preciseLocation2.setLatitude(37.56656789123456);
        preciseLocation2.setLongitude(126.97801234567890);
        
        WorkPlace exactCopy = new WorkPlace();
        exactCopy.setWorkPlaceName("정밀위치1");
        exactCopy.setLatitude(37.56656789123456); // 정확히 같은 위도
        exactCopy.setLongitude(126.97801234567890);
        
        WorkPlace slightlyDifferent = new WorkPlace();
        slightlyDifferent.setWorkPlaceName("정밀위치1");
        slightlyDifferent.setLatitude(37.56656789123457); // 약간 다른 위도
        slightlyDifferent.setLongitude(126.97801234567890);
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로
        assertNotEquals(preciseLocation1, preciseLocation2); // 이름이 다르므로 다름
        assertEquals(preciseLocation1, exactCopy);           // 모든 필드가 같으므로 같음
        assertNotEquals(preciseLocation1, slightlyDifferent); // 위도가 다르므로 다름
        
        assertEquals(37.56656789123456, preciseLocation1.getLatitude(), 0.0);
        assertEquals(126.97801234567890, preciseLocation1.getLongitude(), 0.0);
    }

    @Test
    @DisplayName("should_handleZeroCoordinates_when_settingValues")
    void should_handleZeroCoordinates_when_settingValues() {
        // Given: 0 좌표값들
        WorkPlace zeroCoordinates = new WorkPlace();
        zeroCoordinates.setWorkPlaceName("영점 좌표");
        zeroCoordinates.setLatitude(0.0);
        zeroCoordinates.setLongitude(0.0);
        
        WorkPlace negativeZero = new WorkPlace();
        negativeZero.setWorkPlaceName("음의 영");
        negativeZero.setLatitude(-0.0);
        negativeZero.setLongitude(-0.0);
        
        // When & Then: 0 좌표값들도 올바르게 처리되어야 함
        assertEquals(0.0, zeroCoordinates.getLatitude(), 0.0001);
        assertEquals(0.0, zeroCoordinates.getLongitude(), 0.0001);
        assertEquals(0.0, negativeZero.getLatitude(), 0.0001);
        assertEquals(0.0, negativeZero.getLongitude(), 0.0001);
        
        assertNotEquals(zeroCoordinates, negativeZero); // 이름이 다르므로
    }

    @Test
    @DisplayName("should_maintainConsistentBehavior_when_multipleOperations")
    void should_maintainConsistentBehavior_when_multipleOperations() {
        // Given: WorkPlace 객체
        
        // When: 여러 번의 연속된 연산
        int hashCode1 = workPlace1.hashCode();
        String toString1 = workPlace1.toString();
        boolean equals1 = workPlace1.equals(workPlace2);
        
        int hashCode2 = workPlace1.hashCode();
        String toString2 = workPlace1.toString();
        boolean equals2 = workPlace1.equals(workPlace2);
        
        // Then: 일관된 결과를 반환해야 함
        assertEquals(hashCode1, hashCode2);
        assertEquals(toString1, toString2);
        assertEquals(equals1, equals2);
        assertTrue(equals1);
    }

    @Test
    @DisplayName("should_handleEmptyStringWorkPlaceName_when_settingValues")
    void should_handleEmptyStringWorkPlaceName_when_settingValues() {
        // Given: 빈 문자열 근무지명을 가진 객체들
        WorkPlace emptyName1 = new WorkPlace();
        emptyName1.setWorkPlaceName("");
        emptyName1.setLatitude(37.5665);
        emptyName1.setLongitude(126.9780);
        
        WorkPlace emptyName2 = new WorkPlace();
        emptyName2.setWorkPlaceName("");
        emptyName2.setLatitude(37.5665);  // 같은 좌표
        emptyName2.setLongitude(126.9780);
        
        WorkPlace emptyNameDifferentLocation = new WorkPlace();
        emptyNameDifferentLocation.setWorkPlaceName("");
        emptyNameDifferentLocation.setLatitude(35.1796);  // 다른 좌표
        emptyNameDifferentLocation.setLongitude(129.0756);
        
        // When & Then: Lombok @Data는 모든 필드를 비교하므로
        assertEquals(emptyName1, emptyName2);  // 모든 필드가 같으므로 같음
        assertEquals(emptyName1.hashCode(), emptyName2.hashCode());
        
        assertNotEquals(emptyName1, emptyNameDifferentLocation);  // 좌표가 다르므로 다름
        
        assertEquals("", emptyName1.getWorkPlaceName());
        assertEquals("", emptyName2.getWorkPlaceName());
    }

    @Test
    @DisplayName("should_handleKoreanWorkPlaceNames_when_settingValues")
    void should_handleKoreanWorkPlaceNames_when_settingValues() {
        // Given: 한국어 근무지명들
        WorkPlace seoul = new WorkPlace();
        seoul.setWorkPlaceName("서울본사");
        seoul.setLatitude(37.5665);
        seoul.setLongitude(126.9780);
        
        WorkPlace busan = new WorkPlace();
        busan.setWorkPlaceName("부산지점");
        busan.setLatitude(35.1796);
        busan.setLongitude(129.0756);
        
        WorkPlace longName = new WorkPlace();
        longName.setWorkPlaceName("대한민국 서울특별시 강남구 테헤란로 123번길 45 우리회사 본사 건물 15층");
        longName.setLatitude(37.5172);
        longName.setLongitude(127.0473);
        
        // When & Then: 한국어 근무지명들도 올바르게 처리되어야 함
        assertEquals("서울본사", seoul.getWorkPlaceName());
        assertEquals("부산지점", busan.getWorkPlaceName());
        assertEquals("대한민국 서울특별시 강남구 테헤란로 123번길 45 우리회사 본사 건물 15층", longName.getWorkPlaceName());
        
        assertNotEquals(seoul, busan);
        assertNotEquals(busan, longName);
        assertNotEquals(seoul, longName);
        
        assertNotNull(seoul.toString());
        assertNotNull(busan.toString());
        assertNotNull(longName.toString());
    }
}