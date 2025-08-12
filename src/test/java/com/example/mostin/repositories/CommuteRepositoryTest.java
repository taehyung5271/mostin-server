package com.example.mostin.repositories;

import com.example.mostin.models.Commute;
import com.example.mostin.models.CommuteId;
import com.example.mostin.models.Employee;
import com.example.mostin.models.EmployeeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommuteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommuteRepository commuteRepository;

    private Employee testEmployee;
    private Commute testCommute1;
    private Commute testCommute2;
    private Commute testCommute3;

    @BeforeEach
    void setUp() {
        // Create test employee
        testEmployee = new Employee();
        testEmployee.setEmployeeId("EMP001");
        testEmployee.setEmployeeName("김테스트");
        testEmployee.setEmployeePwd("password123");
        testEmployee.setPhoneNum("010-1234-5678");
        testEmployee.setEmployeeType("정규직");
        testEmployee.setAddress("서울시 강남구");
        testEmployee.setWorkPlaceName("본사");
        entityManager.persistAndFlush(testEmployee);

        // Create test commute records
        testCommute1 = new Commute();
        testCommute1.setCommuteDay(LocalDate.of(2024, 1, 15));
        testCommute1.setEmployeeId("EMP001");
        testCommute1.setEmployeeName("김테스트");
        testCommute1.setStartTime(LocalTime.of(9, 0));
        testCommute1.setEndTime(LocalTime.of(18, 0));
        testCommute1.setWorkPlaceName("본사");
        entityManager.persistAndFlush(testCommute1);

        testCommute2 = new Commute();
        testCommute2.setCommuteDay(LocalDate.of(2024, 1, 16));
        testCommute2.setEmployeeId("EMP001");
        testCommute2.setEmployeeName("김테스트");
        testCommute2.setStartTime(LocalTime.of(8, 30));
        testCommute2.setEndTime(LocalTime.of(17, 30));
        testCommute2.setWorkPlaceName("본사");
        entityManager.persistAndFlush(testCommute2);

        testCommute3 = new Commute();
        testCommute3.setCommuteDay(LocalDate.of(2024, 1, 20));
        testCommute3.setEmployeeId("EMP001");
        testCommute3.setEmployeeName("김테스트");
        testCommute3.setStartTime(LocalTime.of(9, 15));
        testCommute3.setEndTime(LocalTime.of(18, 15));
        testCommute3.setWorkPlaceName("본사");
        entityManager.persistAndFlush(testCommute3);

        entityManager.clear();
    }

    @Test
    void should_findCommutesByDateRange_when_employeeIdAndDateRangeProvided() {
        // Given
        String employeeId = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 14);
        LocalDate endDate = LocalDate.of(2024, 1, 17);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                employeeId, startDate, endDate);

        // Then
        assertThat(commutes).hasSize(2);
        assertThat(commutes)
                .extracting(Commute::getCommuteDay)
                .containsExactlyInAnyOrder(
                        LocalDate.of(2024, 1, 15),
                        LocalDate.of(2024, 1, 16)
                );
    }

    @Test
    void should_returnEmptyList_when_dateRangeHasNoCommutes() {
        // Given
        String employeeId = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 5);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                employeeId, startDate, endDate);

        // Then
        assertThat(commutes).isEmpty();
    }

    @Test
    void should_returnEmptyList_when_employeeNotFound() {
        // Given
        String nonExistentEmployeeId = "NONEXISTENT";
        LocalDate startDate = LocalDate.of(2024, 1, 15);
        LocalDate endDate = LocalDate.of(2024, 1, 20);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                nonExistentEmployeeId, startDate, endDate);

        // Then
        assertThat(commutes).isEmpty();
    }

    @Test
    void should_findCommuteBySpecificDate_when_employeeIdAndDateProvided() {
        // Given
        String employeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                employeeId, targetDate);

        // Then
        assertThat(commutes).hasSize(1);
        assertThat(commutes.get(0).getCommuteDay()).isEqualTo(targetDate);
        assertThat(commutes.get(0).getEmployeeId()).isEqualTo(employeeId);
        assertThat(commutes.get(0).getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(commutes.get(0).getEndTime()).isEqualTo(LocalTime.of(18, 0));
    }

    @Test
    void should_returnEmptyList_when_specificDateHasNoCommute() {
        // Given
        String employeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 17);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                employeeId, targetDate);

        // Then
        assertThat(commutes).isEmpty();
    }

    @Test
    void should_findLatestCommute_when_employeeIdAndNameProvided() {
        // Given
        String employeeId = "EMP001";
        String employeeName = "김테스트";

        // When
        Optional<Commute> latestCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        employeeId, employeeName);

        // Then
        assertThat(latestCommute).isPresent();
        assertThat(latestCommute.get().getCommuteDay()).isEqualTo(LocalDate.of(2024, 1, 20));
        assertThat(latestCommute.get().getStartTime()).isEqualTo(LocalTime.of(9, 15));
    }

    @Test
    void should_returnEmpty_when_noCommuteForEmployeeName() {
        // Given
        String employeeId = "EMP001";
        String wrongEmployeeName = "다른이름";

        // When
        Optional<Commute> latestCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        employeeId, wrongEmployeeName);

        // Then
        assertThat(latestCommute).isEmpty();
    }

    @Test
    void should_handleNullParameters_when_queryingWithNullEmployeeId() {
        // Given
        String nullEmployeeId = null;
        LocalDate startDate = LocalDate.of(2024, 1, 15);
        LocalDate endDate = LocalDate.of(2024, 1, 20);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                nullEmployeeId, startDate, endDate);

        // Then
        assertThat(commutes).isEmpty();
    }

    @Test
    void should_handleBoundaryDates_when_exactDateMatches() {
        // Given
        String employeeId = "EMP001";
        LocalDate exactStartDate = LocalDate.of(2024, 1, 15);
        LocalDate exactEndDate = LocalDate.of(2024, 1, 15);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                employeeId, exactStartDate, exactEndDate);

        // Then
        assertThat(commutes).hasSize(1);
        assertThat(commutes.get(0).getCommuteDay()).isEqualTo(exactStartDate);
    }

    @Test
    void should_findMultipleCommutesForSameDay_when_multipleEntriesExist() {
        // Given - Add another commute for the same day
        Commute additionalCommute = new Commute();
        additionalCommute.setCommuteDay(LocalDate.of(2024, 1, 15));
        additionalCommute.setEmployeeId("EMP002");
        additionalCommute.setEmployeeName("박테스트");
        additionalCommute.setStartTime(LocalTime.of(8, 0));
        additionalCommute.setEndTime(LocalTime.of(17, 0));
        additionalCommute.setWorkPlaceName("본사");

        Employee anotherEmployee = new Employee();
        anotherEmployee.setEmployeeId("EMP002");
        anotherEmployee.setEmployeeName("박테스트");
        anotherEmployee.setEmployeePwd("password456");
        anotherEmployee.setPhoneNum("010-9876-5432");
        anotherEmployee.setEmployeeType("정규직");
        anotherEmployee.setAddress("서울시 서초구");
        anotherEmployee.setWorkPlaceName("본사");

        entityManager.persistAndFlush(anotherEmployee);
        entityManager.persistAndFlush(additionalCommute);

        // When
        List<Commute> emp001Commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                "EMP001", LocalDate.of(2024, 1, 15));
        List<Commute> emp002Commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                "EMP002", LocalDate.of(2024, 1, 15));

        // Then
        assertThat(emp001Commutes).hasSize(1);
        assertThat(emp002Commutes).hasSize(1);
        assertThat(emp001Commutes.get(0).getEmployeeId()).isEqualTo("EMP001");
        assertThat(emp002Commutes.get(0).getEmployeeId()).isEqualTo("EMP002");
    }

    @Test
    void should_orderCorrectly_when_multipleDatesWithSameStartTime() {
        // Given - Add commute with same start time but different date
        Commute sameTimeCommute = new Commute();
        sameTimeCommute.setCommuteDay(LocalDate.of(2024, 1, 21));
        sameTimeCommute.setEmployeeId("EMP001");
        sameTimeCommute.setEmployeeName("김테스트");
        sameTimeCommute.setStartTime(LocalTime.of(9, 15)); // Same as testCommute3
        sameTimeCommute.setEndTime(LocalTime.of(18, 15));
        sameTimeCommute.setWorkPlaceName("본사");

        entityManager.persistAndFlush(sameTimeCommute);

        // When
        Optional<Commute> latestCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        "EMP001", "김테스트");

        // Then
        assertThat(latestCommute).isPresent();
        assertThat(latestCommute.get().getCommuteDay()).isEqualTo(LocalDate.of(2024, 1, 21));
    }

    @Test
    void should_orderCorrectly_when_sameDayWithDifferentStartTimes() {
        // Given - Add another commute for a different day
        Commute laterStartCommute = new Commute();
        laterStartCommute.setCommuteDay(LocalDate.of(2024, 1, 25)); // Different date to avoid constraint violation
        laterStartCommute.setEmployeeId("EMP001");
        laterStartCommute.setEmployeeName("김테스트");
        laterStartCommute.setStartTime(LocalTime.of(9, 30)); // Later start time
        laterStartCommute.setEndTime(LocalTime.of(18, 30));
        laterStartCommute.setWorkPlaceName("본사");

        entityManager.persistAndFlush(laterStartCommute);

        // When
        Optional<Commute> latestCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        "EMP001", "김테스트");

        // Then - Should return the commute with latest date
        assertThat(latestCommute).isPresent();
        assertThat(latestCommute.get().getCommuteDay()).isEqualTo(LocalDate.of(2024, 1, 25));
        assertThat(latestCommute.get().getStartTime()).isEqualTo(LocalTime.of(9, 30));
    }

    @Test
    void should_handleDateRangeAtYearBoundary_when_crossingYears() {
        // Given - Add commutes at year boundary
        Commute yearEndCommute = new Commute();
        yearEndCommute.setCommuteDay(LocalDate.of(2023, 12, 31));
        yearEndCommute.setEmployeeId("EMP001");
        yearEndCommute.setEmployeeName("김테스트");
        yearEndCommute.setStartTime(LocalTime.of(9, 0));
        yearEndCommute.setEndTime(LocalTime.of(18, 0));
        yearEndCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(yearEndCommute);

        Commute yearStartCommute = new Commute();
        yearStartCommute.setCommuteDay(LocalDate.of(2024, 1, 1));
        yearStartCommute.setEmployeeId("EMP001");
        yearStartCommute.setEmployeeName("김테스트");
        yearStartCommute.setStartTime(LocalTime.of(9, 0));
        yearStartCommute.setEndTime(LocalTime.of(18, 0));
        yearStartCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(yearStartCommute);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                "EMP001", LocalDate.of(2023, 12, 30), LocalDate.of(2024, 1, 2));

        // Then
        assertThat(commutes).hasSize(2);
        assertThat(commutes)
                .extracting(Commute::getCommuteDay)
                .containsExactlyInAnyOrder(
                        LocalDate.of(2023, 12, 31),
                        LocalDate.of(2024, 1, 1)
                );
    }

    @Test
    void should_handleLeapYear_when_queryingFebruary29() {
        // Given - Add commute on leap year date
        Commute leapYearCommute = new Commute();
        leapYearCommute.setCommuteDay(LocalDate.of(2024, 2, 29)); // 2024 is leap year
        leapYearCommute.setEmployeeId("EMP001");
        leapYearCommute.setEmployeeName("김테스트");
        leapYearCommute.setStartTime(LocalTime.of(9, 0));
        leapYearCommute.setEndTime(LocalTime.of(18, 0));
        leapYearCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(leapYearCommute);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                "EMP001", LocalDate.of(2024, 2, 29));

        // Then
        assertThat(commutes).hasSize(1);
        assertThat(commutes.get(0).getCommuteDay()).isEqualTo(LocalDate.of(2024, 2, 29));
    }

    @Test
    void should_handleMidnightTimes_when_commuteSpansMidnight() {
        // Given - Add commute with midnight times
        Commute midnightCommute = new Commute();
        midnightCommute.setCommuteDay(LocalDate.of(2024, 1, 25));
        midnightCommute.setEmployeeId("EMP001");
        midnightCommute.setEmployeeName("김테스트");
        midnightCommute.setStartTime(LocalTime.of(23, 59)); // Just before midnight
        midnightCommute.setEndTime(LocalTime.of(0, 0)); // Midnight
        midnightCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(midnightCommute);

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDay(
                "EMP001", LocalDate.of(2024, 1, 25));

        // Then
        assertThat(commutes).hasSize(1);
        assertThat(commutes.get(0).getStartTime()).isEqualTo(LocalTime.of(23, 59));
        assertThat(commutes.get(0).getEndTime()).isEqualTo(LocalTime.of(0, 0));
    }

    @Test
    void should_handleLargeDataset_when_queryingWithManyCommutes() {
        // Given - Add many commutes for performance testing
        LocalDate baseDate = LocalDate.of(2024, 1, 1);
        String employeeId = "EMP999";
        String employeeName = "대량테스트";
        
        Employee bulkEmployee = new Employee();
        bulkEmployee.setEmployeeId(employeeId);
        bulkEmployee.setEmployeeName(employeeName);
        bulkEmployee.setEmployeePwd("bulkPassword");
        bulkEmployee.setPhoneNum("010-0000-0000");
        bulkEmployee.setEmployeeType("테스트");
        bulkEmployee.setAddress("테스트주소");
        bulkEmployee.setWorkPlaceName("테스트지점");
        entityManager.persistAndFlush(bulkEmployee);

        // Create 100 commute records
        for (int i = 0; i < 100; i++) {
            Commute commute = new Commute();
            commute.setCommuteDay(baseDate.plusDays(i));
            commute.setEmployeeId(employeeId);
            commute.setEmployeeName(employeeName);
            commute.setStartTime(LocalTime.of(9, 0));
            commute.setEndTime(LocalTime.of(18, 0));
            commute.setWorkPlaceName("테스트지점");
            entityManager.persist(commute);
        }
        entityManager.flush();
        entityManager.clear();

        // When - Query a large date range
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                employeeId, baseDate, baseDate.plusDays(99));

        // Then
        assertThat(commutes).hasSize(100);
        assertThat(commutes)
                .extracting(Commute::getEmployeeId)
                .containsOnly(employeeId);
    }

    @Test
    void should_handleSpecialCharactersInNames_when_queryingByEmployeeName() {
        // Given - Employee with special characters in name
        String specialEmployeeId = "EMP_SPECIAL";
        String specialEmployeeName = "김-테스트★★★";
        
        Employee specialEmployee = new Employee();
        specialEmployee.setEmployeeId(specialEmployeeId);
        specialEmployee.setEmployeeName(specialEmployeeName);
        specialEmployee.setEmployeePwd("specialPassword");
        specialEmployee.setPhoneNum("010-1111-2222");
        specialEmployee.setEmployeeType("특수문자테스트");
        specialEmployee.setAddress("특수문자주소");
        specialEmployee.setWorkPlaceName("특수문자지점");
        entityManager.persistAndFlush(specialEmployee);

        Commute specialCommute = new Commute();
        specialCommute.setCommuteDay(LocalDate.of(2024, 1, 30));
        specialCommute.setEmployeeId(specialEmployeeId);
        specialCommute.setEmployeeName(specialEmployeeName);
        specialCommute.setStartTime(LocalTime.of(9, 0));
        specialCommute.setEndTime(LocalTime.of(18, 0));
        specialCommute.setWorkPlaceName("특수문자지점");
        entityManager.persistAndFlush(specialCommute);

        // When
        Optional<Commute> foundCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        specialEmployeeId, specialEmployeeName);

        // Then
        assertThat(foundCommute).isPresent();
        assertThat(foundCommute.get().getEmployeeName()).isEqualTo(specialEmployeeName);
        assertThat(foundCommute.get().getEmployeeId()).isEqualTo(specialEmployeeId);
    }

    @Test
    void should_returnCorrectOrder_when_multipleEmployeesHaveSameName() {
        // Given - Multiple employees with same name but different IDs
        String commonName = "김테스트";
        
        Employee duplicateNameEmployee = new Employee();
        duplicateNameEmployee.setEmployeeId("EMP_DUP");
        duplicateNameEmployee.setEmployeeName(commonName);
        duplicateNameEmployee.setEmployeePwd("dupPassword");
        duplicateNameEmployee.setPhoneNum("010-3333-4444");
        duplicateNameEmployee.setEmployeeType("중복이름테스트");
        duplicateNameEmployee.setAddress("중복이름주소");
        duplicateNameEmployee.setWorkPlaceName("중복이름지점");
        entityManager.persistAndFlush(duplicateNameEmployee);

        Commute recentCommute = new Commute();
        recentCommute.setCommuteDay(LocalDate.of(2024, 1, 31));
        recentCommute.setEmployeeId("EMP_DUP");
        recentCommute.setEmployeeName(commonName);
        recentCommute.setStartTime(LocalTime.of(10, 0));
        recentCommute.setEndTime(LocalTime.of(19, 0));
        recentCommute.setWorkPlaceName("중복이름지점");
        entityManager.persistAndFlush(recentCommute);

        // When - Query for different employee with same name
        Optional<Commute> emp001Commute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        "EMP001", commonName);
        Optional<Commute> empDupCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        "EMP_DUP", commonName);

        // Then - Should return different commutes for different employee IDs
        assertThat(emp001Commute).isPresent();
        assertThat(empDupCommute).isPresent();
        assertThat(emp001Commute.get().getEmployeeId()).isEqualTo("EMP001");
        assertThat(empDupCommute.get().getEmployeeId()).isEqualTo("EMP_DUP");
        assertThat(empDupCommute.get().getCommuteDay()).isEqualTo(LocalDate.of(2024, 1, 31));
        assertThat(empDupCommute.get().getStartTime()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    void should_handleEmptyDateRange_when_startDateAfterEndDate() {
        // Given - Invalid date range (start after end)
        String employeeId = "EMP001";
        LocalDate startDate = LocalDate.of(2024, 1, 20);
        LocalDate endDate = LocalDate.of(2024, 1, 15); // Before start date

        // When
        List<Commute> commutes = commuteRepository.findByEmployeeIdAndCommuteDayBetween(
                employeeId, startDate, endDate);

        // Then - Should return empty list for invalid range
        assertThat(commutes).isEmpty();
    }

    @Test
    void should_handleVeryEarlyAndLateStartTimes_when_orderingByTime() {
        // Given - Commutes with very early and very late start times
        Commute veryEarlyCommute = new Commute();
        veryEarlyCommute.setCommuteDay(LocalDate.of(2024, 2, 1));
        veryEarlyCommute.setEmployeeId("EMP001");
        veryEarlyCommute.setEmployeeName("김테스트");
        veryEarlyCommute.setStartTime(LocalTime.of(4, 0)); // Very early
        veryEarlyCommute.setEndTime(LocalTime.of(12, 0));
        veryEarlyCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(veryEarlyCommute);

        Commute veryLateCommute = new Commute();
        veryLateCommute.setCommuteDay(LocalDate.of(2024, 3, 1)); // Different date to avoid constraint violation
        veryLateCommute.setEmployeeId("EMP001");
        veryLateCommute.setEmployeeName("김테스트");
        veryLateCommute.setStartTime(LocalTime.of(22, 0)); // Very late
        veryLateCommute.setEndTime(LocalTime.of(23, 59));
        veryLateCommute.setWorkPlaceName("본사");
        entityManager.persistAndFlush(veryLateCommute);

        // When
        Optional<Commute> latestCommute = commuteRepository
                .findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(
                        "EMP001", "김테스트");

        // Then - Should return the commute with the latest start time
        assertThat(latestCommute).isPresent();
        assertThat(latestCommute.get().getCommuteDay()).isEqualTo(LocalDate.of(2024, 3, 1));
        assertThat(latestCommute.get().getStartTime()).isEqualTo(LocalTime.of(22, 0));
    }
}