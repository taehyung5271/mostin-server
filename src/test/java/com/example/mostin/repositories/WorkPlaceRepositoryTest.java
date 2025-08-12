package com.example.mostin.repositories;

import com.example.mostin.models.WorkPlace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DataJpaTest
class WorkPlaceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private WorkPlaceRepository workPlaceRepository;

    private WorkPlace testWorkPlace1;
    private WorkPlace testWorkPlace2;
    private WorkPlace testWorkPlace3;

    @BeforeEach
    void setUp() {
        // Create test workplaces with realistic coordinates
        testWorkPlace1 = new WorkPlace();
        testWorkPlace1.setWorkPlaceName("본사");
        testWorkPlace1.setLatitude(37.5665); // Seoul coordinates
        testWorkPlace1.setLongitude(126.9780);
        entityManager.persistAndFlush(testWorkPlace1);

        testWorkPlace2 = new WorkPlace();
        testWorkPlace2.setWorkPlaceName("강남지점");
        testWorkPlace2.setLatitude(37.4979); // Gangnam coordinates
        testWorkPlace2.setLongitude(127.0276);
        entityManager.persistAndFlush(testWorkPlace2);

        testWorkPlace3 = new WorkPlace();
        testWorkPlace3.setWorkPlaceName("부산지점");
        testWorkPlace3.setLatitude(35.1796); // Busan coordinates
        testWorkPlace3.setLongitude(129.0756);
        entityManager.persistAndFlush(testWorkPlace3);

        entityManager.clear();
    }

    @Test
    void should_saveWorkPlace_when_validWorkPlaceProvided() {
        // Given
        WorkPlace newWorkPlace = new WorkPlace();
        newWorkPlace.setWorkPlaceName("대구지점");
        newWorkPlace.setLatitude(35.8714); // Daegu coordinates
        newWorkPlace.setLongitude(128.6014);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(newWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedWorkPlace).isNotNull();
        assertThat(savedWorkPlace.getWorkPlaceName()).isEqualTo("대구지점");
        assertThat(savedWorkPlace.getLatitude()).isCloseTo(35.8714, within(0.0001));
        assertThat(savedWorkPlace.getLongitude()).isCloseTo(128.6014, within(0.0001));

        // Verify persistence
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("대구지점");
        assertThat(foundWorkPlace).isPresent();
    }

    @Test
    void should_findWorkPlaceById_when_workPlaceExists() {
        // Given
        String existingWorkPlaceName = "본사";

        // When
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(existingWorkPlaceName);

        // Then
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getWorkPlaceName()).isEqualTo("본사");
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(37.5665, within(0.0001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(126.9780, within(0.0001));
    }

    @Test
    void should_returnEmpty_when_workPlaceDoesNotExist() {
        // Given
        String nonExistentWorkPlaceName = "존재하지않는지점";

        // When
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(nonExistentWorkPlaceName);

        // Then
        assertThat(foundWorkPlace).isEmpty();
    }

    @Test
    void should_throwException_when_workPlaceNameIsNull() {
        // Given
        String nullWorkPlaceName = null;

        // When & Then - Spring Data JPA throws InvalidDataAccessApiUsageException for null IDs
        try {
            workPlaceRepository.findById(nullWorkPlaceName);
            assertThat(false).as("Expected InvalidDataAccessApiUsageException was not thrown").isTrue();
        } catch (org.springframework.dao.InvalidDataAccessApiUsageException e) {
            // Expected behavior - Spring wraps IllegalArgumentException
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void should_returnEmpty_when_workPlaceNameIsEmpty() {
        // Given
        String emptyWorkPlaceName = "";

        // When
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(emptyWorkPlaceName);

        // Then
        assertThat(foundWorkPlace).isEmpty();
    }

    @Test
    void should_findAllWorkPlaces_when_multipleWorkPlacesExist() {
        // When
        List<WorkPlace> allWorkPlaces = workPlaceRepository.findAll();

        // Then
        assertThat(allWorkPlaces).hasSize(3);
        assertThat(allWorkPlaces)
                .extracting(WorkPlace::getWorkPlaceName)
                .containsExactlyInAnyOrder("본사", "강남지점", "부산지점");
    }

    @Test
    void should_deleteWorkPlace_when_workPlaceExists() {
        // Given
        String workPlaceNameToDelete = "강남지점";
        Optional<WorkPlace> existingWorkPlace = workPlaceRepository.findById(workPlaceNameToDelete);
        assertThat(existingWorkPlace).isPresent();

        // When
        workPlaceRepository.delete(existingWorkPlace.get());
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> deletedWorkPlace = workPlaceRepository.findById(workPlaceNameToDelete);
        assertThat(deletedWorkPlace).isEmpty();

        // Verify other workplaces still exist
        List<WorkPlace> remainingWorkPlaces = workPlaceRepository.findAll();
        assertThat(remainingWorkPlaces).hasSize(2);
        assertThat(remainingWorkPlaces)
                .extracting(WorkPlace::getWorkPlaceName)
                .containsExactlyInAnyOrder("본사", "부산지점");
    }

    @Test
    void should_deleteWorkPlaceById_when_workPlaceExists() {
        // Given
        String workPlaceNameToDelete = "부산지점";

        // Verify workplace exists
        assertThat(workPlaceRepository.existsById(workPlaceNameToDelete)).isTrue();

        // When
        workPlaceRepository.deleteById(workPlaceNameToDelete);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(workPlaceRepository.existsById(workPlaceNameToDelete)).isFalse();

        // Verify other workplaces still exist
        long remainingCount = workPlaceRepository.count();
        assertThat(remainingCount).isEqualTo(2);
    }

    @Test
    void should_updateWorkPlace_when_existingWorkPlaceModified() {
        // Given
        String workPlaceName = "본사";
        Optional<WorkPlace> existingWorkPlace = workPlaceRepository.findById(workPlaceName);
        assertThat(existingWorkPlace).isPresent();

        // When
        WorkPlace workPlace = existingWorkPlace.get();
        workPlace.setLatitude(37.5700); // Slightly different coordinates
        workPlace.setLongitude(126.9800);

        WorkPlace updatedWorkPlace = workPlaceRepository.save(workPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(workPlaceName);
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(37.5700, within(0.0001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(126.9800, within(0.0001));
        assertThat(foundWorkPlace.get().getWorkPlaceName()).isEqualTo("본사"); // Name unchanged
    }

    @Test
    void should_checkExistence_when_workPlaceExists() {
        // Given
        String existingWorkPlaceName = "강남지점";

        // When
        boolean exists = workPlaceRepository.existsById(existingWorkPlaceName);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void should_checkExistence_when_workPlaceDoesNotExist() {
        // Given
        String nonExistentWorkPlaceName = "인천지점";

        // When
        boolean exists = workPlaceRepository.existsById(nonExistentWorkPlaceName);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void should_countWorkPlaces_when_multipleWorkPlacesExist() {
        // When
        long count = workPlaceRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void should_handleZeroCoordinates_when_savingWorkPlace() {
        // Given
        WorkPlace zeroCoordWorkPlace = new WorkPlace();
        zeroCoordWorkPlace.setWorkPlaceName("영점좌표지점");
        zeroCoordWorkPlace.setLatitude(0.0);
        zeroCoordWorkPlace.setLongitude(0.0);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(zeroCoordWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("영점좌표지점");
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getLatitude()).isEqualTo(0.0);
        assertThat(foundWorkPlace.get().getLongitude()).isEqualTo(0.0);
    }

    @Test
    void should_handleNegativeCoordinates_when_savingWorkPlace() {
        // Given
        WorkPlace negativeCoordWorkPlace = new WorkPlace();
        negativeCoordWorkPlace.setWorkPlaceName("음수좌표지점");
        negativeCoordWorkPlace.setLatitude(-33.8688); // Sydney coordinates
        negativeCoordWorkPlace.setLongitude(151.2093);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(negativeCoordWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("음수좌표지점");
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(-33.8688, within(0.0001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(151.2093, within(0.0001));
    }

    @Test
    void should_handleExtremeCoordinates_when_savingWorkPlace() {
        // Given
        WorkPlace extremeCoordWorkPlace = new WorkPlace();
        extremeCoordWorkPlace.setWorkPlaceName("극한좌표지점");
        extremeCoordWorkPlace.setLatitude(90.0); // North Pole
        extremeCoordWorkPlace.setLongitude(-180.0); // International Date Line

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(extremeCoordWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("극한좌표지점");
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getLatitude()).isEqualTo(90.0);
        assertThat(foundWorkPlace.get().getLongitude()).isEqualTo(-180.0);
    }

    @Test
    void should_deleteAllWorkPlaces_when_requested() {
        // Given
        assertThat(workPlaceRepository.count()).isEqualTo(3);

        // When
        workPlaceRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(workPlaceRepository.count()).isEqualTo(0);
        assertThat(workPlaceRepository.findAll()).isEmpty();
    }

    @Test
    void should_handleVeryLongWorkPlaceName_when_savingWorkPlace() {
        // Given
        String longWorkPlaceName = "매우긴이름을가진지점이름테스트용지점명";
        WorkPlace longNameWorkPlace = new WorkPlace();
        longNameWorkPlace.setWorkPlaceName(longWorkPlaceName);
        longNameWorkPlace.setLatitude(37.4563);
        longNameWorkPlace.setLongitude(126.7052);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(longNameWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(longWorkPlaceName);
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getWorkPlaceName()).isEqualTo(longWorkPlaceName);
    }

    @Test
    void should_overwriteWorkPlace_when_sameNameProvidedWithDifferentCoordinates() {
        // Given
        String existingWorkPlaceName = "본사";
        double newLatitude = 40.7128; // New York coordinates
        double newLongitude = -74.0060;

        WorkPlace updatedWorkPlace = new WorkPlace();
        updatedWorkPlace.setWorkPlaceName(existingWorkPlaceName);
        updatedWorkPlace.setLatitude(newLatitude);
        updatedWorkPlace.setLongitude(newLongitude);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(updatedWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(existingWorkPlaceName);
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(newLatitude, within(0.0001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(newLongitude, within(0.0001));

        // Verify total count remains the same
        long totalCount = workPlaceRepository.count();
        assertThat(totalCount).isEqualTo(3);
    }

    @Test
    void should_handlePreciseCoordinates_when_savingHighPrecisionValues() {
        // Given - WorkPlace with very precise coordinates
        WorkPlace preciseWorkPlace = new WorkPlace();
        preciseWorkPlace.setWorkPlaceName("정밀좌표지점");
        preciseWorkPlace.setLatitude(37.123456789); // High precision latitude
        preciseWorkPlace.setLongitude(127.987654321); // High precision longitude

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(preciseWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("정밀좌표지점");
        assertThat(foundWorkPlace).isPresent();
        // Note: Database precision might limit the actual stored precision
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(37.123456789, within(0.000001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(127.987654321, within(0.000001));
    }

    @Test
    void should_handleInternationalCoordinates_when_savingGlobalLocations() {
        // Given - WorkPlaces from different continents
        WorkPlace tokyoWorkPlace = new WorkPlace();
        tokyoWorkPlace.setWorkPlaceName("도쿠지점");
        tokyoWorkPlace.setLatitude(35.6762); // Tokyo coordinates
        tokyoWorkPlace.setLongitude(139.6503);
        workPlaceRepository.save(tokyoWorkPlace);

        WorkPlace londonWorkPlace = new WorkPlace();
        londonWorkPlace.setWorkPlaceName("런던지점");
        londonWorkPlace.setLatitude(51.5074); // London coordinates
        londonWorkPlace.setLongitude(-0.1278);
        workPlaceRepository.save(londonWorkPlace);

        WorkPlace sydneyWorkPlace = new WorkPlace();
        sydneyWorkPlace.setWorkPlaceName("시드니지점");
        sydneyWorkPlace.setLatitude(-33.8688); // Sydney coordinates
        sydneyWorkPlace.setLongitude(151.2093);
        workPlaceRepository.save(sydneyWorkPlace);

        entityManager.flush();
        entityManager.clear();

        // When
        List<WorkPlace> internationalWorkPlaces = workPlaceRepository.findAll();

        // Then
        assertThat(internationalWorkPlaces).hasSize(6); // 3 original + 3 international
        
        Optional<WorkPlace> tokyo = workPlaceRepository.findById("도쿠지점");
        Optional<WorkPlace> london = workPlaceRepository.findById("런던지점");
        Optional<WorkPlace> sydney = workPlaceRepository.findById("시드니지점");

        assertThat(tokyo).isPresent();
        assertThat(london).isPresent();
        assertThat(sydney).isPresent();
        
        // Verify coordinates are in expected ranges
        assertThat(tokyo.get().getLatitude()).isBetween(30.0, 40.0);
        assertThat(london.get().getLongitude()).isBetween(-1.0, 1.0);
        assertThat(sydney.get().getLatitude()).isNegative(); // Southern hemisphere
    }

    @Test
    void should_handleSpecialCharactersInWorkPlaceName_when_savingInternationalNames() {
        // Given - WorkPlace with special characters and international names
        WorkPlace specialNameWorkPlace = new WorkPlace();
        specialNameWorkPlace.setWorkPlaceName("한국지점 & Co., Ltd. ★★★");
        specialNameWorkPlace.setLatitude(37.5665);
        specialNameWorkPlace.setLongitude(126.9780);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(specialNameWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("한국지점 & Co., Ltd. ★★★");
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getWorkPlaceName()).isEqualTo("한국지점 & Co., Ltd. ★★★");
    }

    @Test
    void should_handleSingleCharacterWorkPlaceName_when_savingMinimalName() {
        // Given - WorkPlace with single character name
        WorkPlace singleCharWorkPlace = new WorkPlace();
        singleCharWorkPlace.setWorkPlaceName("중");
        singleCharWorkPlace.setLatitude(37.4563);
        singleCharWorkPlace.setLongitude(126.7052);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(singleCharWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("중");
        assertThat(foundWorkPlace).isPresent();
        assertThat(foundWorkPlace.get().getWorkPlaceName()).isEqualTo("중");
    }

    @Test
    void should_handleBatchOperations_when_savingMultipleWorkPlaces() {
        // Given - Multiple workplaces for batch testing
        List<WorkPlace> workPlacesToSave = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            WorkPlace batchWorkPlace = new WorkPlace();
            batchWorkPlace.setWorkPlaceName("일괄등록지점" + i);
            batchWorkPlace.setLatitude(35.0 + i * 0.1); // Spread coordinates
            batchWorkPlace.setLongitude(125.0 + i * 0.1);
            workPlacesToSave.add(batchWorkPlace);
        }

        // When
        List<WorkPlace> savedWorkPlaces = workPlaceRepository.saveAll(workPlacesToSave);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedWorkPlaces).hasSize(20);
        
        // Verify each workplace was saved correctly
        for (int i = 1; i <= 20; i++) {
            Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("일괄등록지점" + i);
            assertThat(foundWorkPlace).isPresent();
            assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(35.0 + i * 0.1, within(0.01));
        }
        
        // Verify total count
        long totalCount = workPlaceRepository.count();
        assertThat(totalCount).isEqualTo(23); // 3 original + 20 batch
    }

    @Test
    void should_handleMaxMinCoordinateValues_when_savingExtremeCoordinates() {
        // Given - WorkPlaces with extreme coordinate values
        WorkPlace northPoleWorkPlace = new WorkPlace();
        northPoleWorkPlace.setWorkPlaceName("북극지점");
        northPoleWorkPlace.setLatitude(90.0); // Maximum latitude
        northPoleWorkPlace.setLongitude(0.0);
        workPlaceRepository.save(northPoleWorkPlace);

        WorkPlace southPoleWorkPlace = new WorkPlace();
        southPoleWorkPlace.setWorkPlaceName("남극지점");
        southPoleWorkPlace.setLatitude(-90.0); // Minimum latitude
        southPoleWorkPlace.setLongitude(0.0);
        workPlaceRepository.save(southPoleWorkPlace);

        WorkPlace dateLine180WorkPlace = new WorkPlace();
        dateLine180WorkPlace.setWorkPlaceName("상일경계선지점");
        dateLine180WorkPlace.setLatitude(0.0);
        dateLine180WorkPlace.setLongitude(180.0); // Maximum longitude
        workPlaceRepository.save(dateLine180WorkPlace);

        WorkPlace dateLine180NegWorkPlace = new WorkPlace();
        dateLine180NegWorkPlace.setWorkPlaceName("상일경계선음수지점");
        dateLine180NegWorkPlace.setLatitude(0.0);
        dateLine180NegWorkPlace.setLongitude(-180.0); // Minimum longitude
        workPlaceRepository.save(dateLine180NegWorkPlace);

        entityManager.flush();
        entityManager.clear();

        // When & Then
        Optional<WorkPlace> northPole = workPlaceRepository.findById("북극지점");
        Optional<WorkPlace> southPole = workPlaceRepository.findById("남극지점");
        Optional<WorkPlace> dateLine180 = workPlaceRepository.findById("상일경계선지점");
        Optional<WorkPlace> dateLine180Neg = workPlaceRepository.findById("상일경계선음수지점");

        assertThat(northPole).isPresent();
        assertThat(southPole).isPresent();
        assertThat(dateLine180).isPresent();
        assertThat(dateLine180Neg).isPresent();
        
        assertThat(northPole.get().getLatitude()).isEqualTo(90.0);
        assertThat(southPole.get().getLatitude()).isEqualTo(-90.0);
        assertThat(dateLine180.get().getLongitude()).isEqualTo(180.0);
        assertThat(dateLine180Neg.get().getLongitude()).isEqualTo(-180.0);
    }

    @Test
    void should_handleFloatingPointPrecision_when_comparingCoordinates() {
        // Given - WorkPlace with coordinates that might have floating point precision issues
        double precisionLatitude = 37.123456789012345;
        double precisionLongitude = 127.987654321098765;
        
        WorkPlace precisionWorkPlace = new WorkPlace();
        precisionWorkPlace.setWorkPlaceName("또점정밀도지점");
        precisionWorkPlace.setLatitude(precisionLatitude);
        precisionWorkPlace.setLongitude(precisionLongitude);

        // When
        WorkPlace savedWorkPlace = workPlaceRepository.save(precisionWorkPlace);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById("또점정밀도지점");
        assertThat(foundWorkPlace).isPresent();
        
        // Use appropriate tolerance for floating point comparison
        assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(precisionLatitude, within(0.000001));
        assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(precisionLongitude, within(0.000001));
    }

    @Test
    void should_performCRUDOperations_when_workingWithCompleteLifecycle() {
        // Given - Complete CRUD lifecycle test
        String workPlaceName = "전체라이프사이클테스트지점";
        
        // CREATE
        WorkPlace newWorkPlace = new WorkPlace();
        newWorkPlace.setWorkPlaceName(workPlaceName);
        newWorkPlace.setLatitude(36.5);
        newWorkPlace.setLongitude(128.0);
        workPlaceRepository.save(newWorkPlace);
        entityManager.flush();
        
        // READ
        Optional<WorkPlace> readWorkPlace = workPlaceRepository.findById(workPlaceName);
        assertThat(readWorkPlace).isPresent();
        assertThat(readWorkPlace.get().getLatitude()).isCloseTo(36.5, within(0.01));
        
        // UPDATE
        WorkPlace workPlaceToUpdate = readWorkPlace.get();
        workPlaceToUpdate.setLatitude(36.7); // Updated latitude
        workPlaceToUpdate.setLongitude(128.2); // Updated longitude
        workPlaceRepository.save(workPlaceToUpdate);
        entityManager.flush();
        entityManager.clear();
        
        // Verify UPDATE
        Optional<WorkPlace> updatedWorkPlace = workPlaceRepository.findById(workPlaceName);
        assertThat(updatedWorkPlace).isPresent();
        assertThat(updatedWorkPlace.get().getLatitude()).isCloseTo(36.7, within(0.01));
        assertThat(updatedWorkPlace.get().getLongitude()).isCloseTo(128.2, within(0.01));
        
        // DELETE
        workPlaceRepository.deleteById(workPlaceName);
        entityManager.flush();
        entityManager.clear();
        
        // Verify DELETE
        Optional<WorkPlace> deletedWorkPlace = workPlaceRepository.findById(workPlaceName);
        assertThat(deletedWorkPlace).isEmpty();
        assertThat(workPlaceRepository.existsById(workPlaceName)).isFalse();
    }

    @Test
    void should_handleDeleteOperations_when_removingMultipleWorkPlaces() {
        // Given - Multiple workplaces for deletion testing
        List<WorkPlace> workPlacesToDelete = new ArrayList<>();
        for (int i = 500; i < 505; i++) {
            WorkPlace workPlaceForDeletion = new WorkPlace();
            workPlaceForDeletion.setWorkPlaceName("삭제테스트지점" + i);
            workPlaceForDeletion.setLatitude(35.0 + i * 0.01);
            workPlaceForDeletion.setLongitude(125.0 + i * 0.01);
            workPlacesToDelete.add(workPlaceForDeletion);
        }
        workPlaceRepository.saveAll(workPlacesToDelete);
        entityManager.flush();
        
        // Verify initial state
        assertThat(workPlaceRepository.count()).isEqualTo(8); // 3 original + 5 for deletion

        // When - Delete specific workplaces
        workPlaceRepository.deleteAll(workPlacesToDelete);
        entityManager.flush();
        entityManager.clear();

        // Then - Only original workplaces should remain
        assertThat(workPlaceRepository.count()).isEqualTo(3);
        
        // Verify deleted workplaces are gone
        for (int i = 500; i < 505; i++) {
            assertThat(workPlaceRepository.existsById("삭제테스트지점" + i)).isFalse();
        }
    }

    @Test
    void should_handleDataIntegrity_when_savingAndRetrievingCoordinates() {
        // Given - Various coordinate combinations for data integrity testing
        Map<String, Double[]> coordinateTestCases = new HashMap<>();
        coordinateTestCases.put("데이터무결성테스트1", new Double[]{0.0, 0.0});
        coordinateTestCases.put("데이터무결성테스트2", new Double[]{45.123, -67.890});
        coordinateTestCases.put("데이터무결성테스트3", new Double[]{-12.345, 98.765});
        coordinateTestCases.put("데이터무결성테스트4", new Double[]{89.999, 179.999});

        // When - Save all test cases
        for (Map.Entry<String, Double[]> entry : coordinateTestCases.entrySet()) {
            WorkPlace testWorkPlace = new WorkPlace();
            testWorkPlace.setWorkPlaceName(entry.getKey());
            testWorkPlace.setLatitude(entry.getValue()[0]);
            testWorkPlace.setLongitude(entry.getValue()[1]);
            workPlaceRepository.save(testWorkPlace);
        }
        entityManager.flush();
        entityManager.clear();

        // Then - Verify data integrity
        for (Map.Entry<String, Double[]> entry : coordinateTestCases.entrySet()) {
            Optional<WorkPlace> foundWorkPlace = workPlaceRepository.findById(entry.getKey());
            assertThat(foundWorkPlace).isPresent();
            assertThat(foundWorkPlace.get().getLatitude()).isCloseTo(entry.getValue()[0], within(0.001));
            assertThat(foundWorkPlace.get().getLongitude()).isCloseTo(entry.getValue()[1], within(0.001));
        }
    }
}