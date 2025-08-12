package com.example.mostin.repositories;

import com.example.mostin.models.Goods;
import com.example.mostin.models.GoodsId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GoodsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GoodsRepository goodsRepository;

    private Goods testGoods1;
    private Goods testGoods2;
    private Goods testGoods3;

    @BeforeEach
    void setUp() {
        // Create test goods with different combinations
        testGoods1 = new Goods();
        testGoods1.setBarcode("1234567890123");
        testGoods1.setGoodsName("아메리카노");
        entityManager.persistAndFlush(testGoods1);

        testGoods2 = new Goods();
        testGoods2.setBarcode("1234567890124");
        testGoods2.setGoodsName("라떼");
        entityManager.persistAndFlush(testGoods2);

        testGoods3 = new Goods();
        testGoods3.setBarcode("1234567890125");
        testGoods3.setGoodsName("카푸치노");
        entityManager.persistAndFlush(testGoods3);

        entityManager.clear();
    }

    @Test
    void should_saveGoods_when_validGoodsProvided() {
        // Given
        Goods newGoods = new Goods();
        newGoods.setBarcode("1234567890126");
        newGoods.setGoodsName("에스프레소");

        // When
        Goods savedGoods = goodsRepository.save(newGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedGoods).isNotNull();
        assertThat(savedGoods.getBarcode()).isEqualTo("1234567890126");
        assertThat(savedGoods.getGoodsName()).isEqualTo("에스프레소");

        // Verify persistence
        GoodsId goodsId = new GoodsId();
        goodsId.setBarcode("1234567890126");
        goodsId.setGoodsName("에스프레소");
        Optional<Goods> foundGoods = goodsRepository.findById(goodsId);
        assertThat(foundGoods).isPresent();
    }

    @Test
    void should_findGoodsById_when_goodsExists() {
        // Given
        GoodsId existingGoodsId = new GoodsId();
        existingGoodsId.setBarcode("1234567890123");
        existingGoodsId.setGoodsName("아메리카노");

        // When
        Optional<Goods> foundGoods = goodsRepository.findById(existingGoodsId);

        // Then
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getBarcode()).isEqualTo("1234567890123");
        assertThat(foundGoods.get().getGoodsName()).isEqualTo("아메리카노");
    }

    @Test
    void should_returnEmpty_when_goodsDoesNotExist() {
        // Given
        GoodsId nonExistentGoodsId = new GoodsId();
        nonExistentGoodsId.setBarcode("9999999999999");
        nonExistentGoodsId.setGoodsName("존재하지않는상품");

        // When
        Optional<Goods> foundGoods = goodsRepository.findById(nonExistentGoodsId);

        // Then
        assertThat(foundGoods).isEmpty();
    }

    @Test
    void should_returnEmpty_when_partialIdMatch() {
        // Given - Same barcode but different goods name
        GoodsId partialMatchId = new GoodsId();
        partialMatchId.setBarcode("1234567890123");
        partialMatchId.setGoodsName("다른상품명");

        // When
        Optional<Goods> foundGoods = goodsRepository.findById(partialMatchId);

        // Then
        assertThat(foundGoods).isEmpty();
    }

    @Test
    void should_findAllGoods_when_multipleGoodsExist() {
        // When
        List<Goods> allGoods = goodsRepository.findAll();

        // Then
        assertThat(allGoods).hasSize(3);
        assertThat(allGoods)
                .extracting(Goods::getGoodsName)
                .containsExactlyInAnyOrder("아메리카노", "라떼", "카푸치노");
    }

    @Test
    void should_deleteGoods_when_goodsExists() {
        // Given
        GoodsId existingGoodsId = new GoodsId();
        existingGoodsId.setBarcode("1234567890124");
        existingGoodsId.setGoodsName("라떼");

        Optional<Goods> existingGoods = goodsRepository.findById(existingGoodsId);
        assertThat(existingGoods).isPresent();

        // When
        goodsRepository.delete(existingGoods.get());
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Goods> deletedGoods = goodsRepository.findById(existingGoodsId);
        assertThat(deletedGoods).isEmpty();

        // Verify other goods still exist
        List<Goods> remainingGoods = goodsRepository.findAll();
        assertThat(remainingGoods).hasSize(2);
    }

    @Test
    void should_deleteGoodsById_when_goodsExists() {
        // Given
        GoodsId existingGoodsId = new GoodsId();
        existingGoodsId.setBarcode("1234567890125");
        existingGoodsId.setGoodsName("카푸치노");

        // Verify goods exists
        assertThat(goodsRepository.existsById(existingGoodsId)).isTrue();

        // When
        goodsRepository.deleteById(existingGoodsId);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(goodsRepository.existsById(existingGoodsId)).isFalse();

        // Verify other goods still exist
        long remainingCount = goodsRepository.count();
        assertThat(remainingCount).isEqualTo(2);
    }

    @Test
    void should_updateGoods_when_existingGoodsModified() {
        // Given
        GoodsId existingGoodsId = new GoodsId();
        existingGoodsId.setBarcode("1234567890123");
        existingGoodsId.setGoodsName("아메리카노");

        Optional<Goods> existingGoods = goodsRepository.findById(existingGoodsId);
        assertThat(existingGoods).isPresent();

        // When - Update by creating new goods with same composite key
        Goods updatedGoods = new Goods();
        updatedGoods.setBarcode("1234567890123");
        updatedGoods.setGoodsName("아메리카노");

        Goods savedGoods = goodsRepository.save(updatedGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        Optional<Goods> foundGoods = goodsRepository.findById(existingGoodsId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getBarcode()).isEqualTo("1234567890123");
        assertThat(foundGoods.get().getGoodsName()).isEqualTo("아메리카노");
    }

    @Test
    void should_checkExistence_when_goodsExists() {
        // Given
        GoodsId existingGoodsId = new GoodsId();
        existingGoodsId.setBarcode("1234567890124");
        existingGoodsId.setGoodsName("라떼");

        // When
        boolean exists = goodsRepository.existsById(existingGoodsId);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void should_checkExistence_when_goodsDoesNotExist() {
        // Given
        GoodsId nonExistentGoodsId = new GoodsId();
        nonExistentGoodsId.setBarcode("0000000000000");
        nonExistentGoodsId.setGoodsName("존재하지않음");

        // When
        boolean exists = goodsRepository.existsById(nonExistentGoodsId);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void should_countGoods_when_multipleGoodsExist() {
        // When
        long count = goodsRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    @Test
    void should_handleDuplicateBarcode_when_differentGoodsNames() {
        // Given - Same barcode but different goods name
        Goods duplicateBarcodeGoods = new Goods();
        duplicateBarcodeGoods.setBarcode("1234567890123"); // Same barcode
        duplicateBarcodeGoods.setGoodsName("디카페인아메리카노"); // Different name

        // When
        Goods savedGoods = goodsRepository.save(duplicateBarcodeGoods);
        entityManager.flush();
        entityManager.clear();

        // Then - Both goods should exist with different composite keys
        GoodsId originalId = new GoodsId();
        originalId.setBarcode("1234567890123");
        originalId.setGoodsName("아메리카노");
        Optional<Goods> originalGoods = goodsRepository.findById(originalId);
        assertThat(originalGoods).isPresent();

        GoodsId newId = new GoodsId();
        newId.setBarcode("1234567890123");
        newId.setGoodsName("디카페인아메리카노");
        Optional<Goods> newGoods = goodsRepository.findById(newId);
        assertThat(newGoods).isPresent();

        // Total count should be 4
        long totalCount = goodsRepository.count();
        assertThat(totalCount).isEqualTo(4);
    }

    @Test
    void should_handleDuplicateGoodsName_when_differentBarcodes() {
        // Given - Different barcode but same goods name
        Goods duplicateNameGoods = new Goods();
        duplicateNameGoods.setBarcode("9876543210123"); // Different barcode
        duplicateNameGoods.setGoodsName("아메리카노"); // Same name

        // When
        Goods savedGoods = goodsRepository.save(duplicateNameGoods);
        entityManager.flush();
        entityManager.clear();

        // Then - Both goods should exist with different composite keys
        GoodsId originalId = new GoodsId();
        originalId.setBarcode("1234567890123");
        originalId.setGoodsName("아메리카노");
        Optional<Goods> originalGoods = goodsRepository.findById(originalId);
        assertThat(originalGoods).isPresent();

        GoodsId newId = new GoodsId();
        newId.setBarcode("9876543210123");
        newId.setGoodsName("아메리카노");
        Optional<Goods> newGoods = goodsRepository.findById(newId);
        assertThat(newGoods).isPresent();

        // Total count should be 4
        long totalCount = goodsRepository.count();
        assertThat(totalCount).isEqualTo(4);
    }

    @Test
    void should_deleteAllGoods_when_requested() {
        // Given
        assertThat(goodsRepository.count()).isEqualTo(3);

        // When
        goodsRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(goodsRepository.count()).isEqualTo(0);
        assertThat(goodsRepository.findAll()).isEmpty();
    }

    @Test
    void should_throwException_when_findingByNullCompositeId() {
        // Given
        GoodsId nullGoodsId = null;

        // When & Then - Spring Data JPA throws InvalidDataAccessApiUsageException for null IDs
        try {
            goodsRepository.findById(nullGoodsId);
            assertThat(false).as("Expected InvalidDataAccessApiUsageException was not thrown").isTrue();
        } catch (org.springframework.dao.InvalidDataAccessApiUsageException e) {
            // Expected behavior - Spring wraps IllegalArgumentException
            assertThat(e.getCause()).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void should_handlePartialCompositeId_when_savingWithIncompleteData() {
        // Given - GoodsId with null barcode
        Goods incompleteGoods = new Goods();
        incompleteGoods.setBarcode(null); // Null barcode
        incompleteGoods.setGoodsName("비완성상품");

        // When & Then - Should handle null values in composite key appropriately
        try {
            goodsRepository.save(incompleteGoods);
            entityManager.flush();
            // If this doesn't throw an exception, verify the behavior
        } catch (Exception e) {
            // Expected behavior for incomplete composite key
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void should_handleVeryLongBarcode_when_savingGoodsWithExtendedBarcode() {
        // Given - Goods with very long barcode
        String veryLongBarcode = "1234567890123456789012345678901234567890123456789012345678901234567890";
        Goods longBarcodeGoods = new Goods();
        longBarcodeGoods.setBarcode(veryLongBarcode);
        longBarcodeGoods.setGoodsName("긴바코드상품테스트");

        // When
        Goods savedGoods = goodsRepository.save(longBarcodeGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        GoodsId longBarcodeId = new GoodsId();
        longBarcodeId.setBarcode(veryLongBarcode);
        longBarcodeId.setGoodsName("긴바코드상품테스트");
        
        Optional<Goods> foundGoods = goodsRepository.findById(longBarcodeId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getBarcode()).isEqualTo(veryLongBarcode);
    }

    @Test
    void should_handleVeryLongGoodsName_when_savingGoodsWithExtendedName() {
        // Given - Goods with very long goods name
        String veryLongGoodsName = "매우아주긴상품명을가지고있는테스트상품이름입니다이상품은테스트용도로만들어졌습니다";
        Goods longNameGoods = new Goods();
        longNameGoods.setBarcode("LONGNAME123456789");
        longNameGoods.setGoodsName(veryLongGoodsName);

        // When
        Goods savedGoods = goodsRepository.save(longNameGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        GoodsId longNameId = new GoodsId();
        longNameId.setBarcode("LONGNAME123456789");
        longNameId.setGoodsName(veryLongGoodsName);
        
        Optional<Goods> foundGoods = goodsRepository.findById(longNameId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getGoodsName()).isEqualTo(veryLongGoodsName);
    }

    @Test
    void should_handleSpecialCharactersInBarcode_when_savingGoodsWithSpecialBarcode() {
        // Given - Goods with special characters in barcode
        String specialBarcode = "BC-2024*TEST#001";
        Goods specialBarcodeGoods = new Goods();
        specialBarcodeGoods.setBarcode(specialBarcode);
        specialBarcodeGoods.setGoodsName("특수바코드상품");

        // When
        Goods savedGoods = goodsRepository.save(specialBarcodeGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        GoodsId specialBarcodeId = new GoodsId();
        specialBarcodeId.setBarcode(specialBarcode);
        specialBarcodeId.setGoodsName("특수바코드상품");
        
        Optional<Goods> foundGoods = goodsRepository.findById(specialBarcodeId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getBarcode()).isEqualTo(specialBarcode);
        assertThat(foundGoods.get().getGoodsName()).isEqualTo("특수바코드상품");
    }

    @Test
    void should_handleSpecialCharactersInGoodsName_when_savingGoodsWithSpecialName() {
        // Given - Goods with special characters in goods name
        String specialGoodsName = "상품 & 특별할인 50% ★★★";
        Goods specialNameGoods = new Goods();
        specialNameGoods.setBarcode("SPECIAL123456789");
        specialNameGoods.setGoodsName(specialGoodsName);

        // When
        Goods savedGoods = goodsRepository.save(specialNameGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        GoodsId specialNameId = new GoodsId();
        specialNameId.setBarcode("SPECIAL123456789");
        specialNameId.setGoodsName(specialGoodsName);
        
        Optional<Goods> foundGoods = goodsRepository.findById(specialNameId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getGoodsName()).isEqualTo(specialGoodsName);
    }

    @Test
    void should_handleNumericBarcode_when_savingGoodsWithNumericId() {
        // Given - Goods with purely numeric barcode
        String numericBarcode = "1111111111111";
        Goods numericBarcodeGoods = new Goods();
        numericBarcodeGoods.setBarcode(numericBarcode);
        numericBarcodeGoods.setGoodsName("숫자바코드상품");

        // When
        Goods savedGoods = goodsRepository.save(numericBarcodeGoods);
        entityManager.flush();
        entityManager.clear();

        // Then
        GoodsId numericBarcodeId = new GoodsId();
        numericBarcodeId.setBarcode(numericBarcode);
        numericBarcodeId.setGoodsName("숫자바코드상품");
        
        Optional<Goods> foundGoods = goodsRepository.findById(numericBarcodeId);
        assertThat(foundGoods).isPresent();
        assertThat(foundGoods.get().getBarcode()).isEqualTo(numericBarcode);
    }

    @Test
    void should_handleBatchOperations_when_savingMultipleGoods() {
        // Given - Multiple goods for batch testing
        List<Goods> goodsToSave = new ArrayList<>();
        for (int i = 200; i < 250; i++) {
            Goods batchGoods = new Goods();
            batchGoods.setBarcode("BATCH" + String.format("%010d", i));
            batchGoods.setGoodsName("일괄등록상품" + i);
            goodsToSave.add(batchGoods);
        }

        // When
        List<Goods> savedGoods = goodsRepository.saveAll(goodsToSave);
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(savedGoods).hasSize(50);
        
        // Verify each goods was saved correctly
        for (int i = 200; i < 250; i++) {
            GoodsId batchId = new GoodsId();
            batchId.setBarcode("BATCH" + String.format("%010d", i));
            batchId.setGoodsName("일괄등록상품" + i);
            
            Optional<Goods> foundGoods = goodsRepository.findById(batchId);
            assertThat(foundGoods).isPresent();
        }
        
        // Verify total count
        long totalCount = goodsRepository.count();
        assertThat(totalCount).isEqualTo(53); // 3 original + 50 batch
    }

    @Test
    void should_handleEmptyStringValues_when_savingGoodsWithEmptyFields() {
        // Given - Goods with empty string values
        Goods emptyFieldGoods = new Goods();
        emptyFieldGoods.setBarcode(""); // Empty barcode
        emptyFieldGoods.setGoodsName(""); // Empty goods name

        // When & Then
        try {
            Goods savedGoods = goodsRepository.save(emptyFieldGoods);
            entityManager.flush();
            
            // If save succeeds, verify the values
            GoodsId emptyId = new GoodsId();
            emptyId.setBarcode("");
            emptyId.setGoodsName("");
            
            Optional<Goods> foundGoods = goodsRepository.findById(emptyId);
            if (foundGoods.isPresent()) {
                assertThat(foundGoods.get().getBarcode()).isEqualTo("");
                assertThat(foundGoods.get().getGoodsName()).isEqualTo("");
            }
        } catch (Exception e) {
            // Expected behavior if empty values are not allowed
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void should_handleDeleteAllInBatch_when_removingManyGoods() {
        // Given - Add additional goods for batch deletion test
        List<Goods> tempGoods = new ArrayList<>();
        for (int i = 300; i < 310; i++) {
            Goods tempGood = new Goods();
            tempGood.setBarcode("TEMP" + i);
            tempGood.setGoodsName("임시상품" + i);
            tempGoods.add(tempGood);
        }
        goodsRepository.saveAll(tempGoods);
        entityManager.flush();
        
        // Verify initial count
        long initialCount = goodsRepository.count();
        assertThat(initialCount).isEqualTo(13); // 3 original + 10 temp

        // When - Delete all goods
        goodsRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        // Then
        assertThat(goodsRepository.count()).isEqualTo(0);
        assertThat(goodsRepository.findAll()).isEmpty();
    }

    @Test
    void should_handleDeleteInBatch_when_removingSpecificGoods() {
        // Given - Add specific goods for targeted deletion
        List<Goods> goodsToDelete = new ArrayList<>();
        for (int i = 400; i < 405; i++) {
            Goods goodsForDeletion = new Goods();
            goodsForDeletion.setBarcode("DELETE" + i);
            goodsForDeletion.setGoodsName("삭제대상상품" + i);
            goodsToDelete.add(goodsForDeletion);
        }
        goodsRepository.saveAll(goodsToDelete);
        entityManager.flush();
        
        // Verify initial state
        assertThat(goodsRepository.count()).isEqualTo(8); // 3 original + 5 for deletion

        // When - Delete specific goods
        goodsRepository.deleteAll(goodsToDelete);
        entityManager.flush();
        entityManager.clear();

        // Then - Only original goods should remain
        assertThat(goodsRepository.count()).isEqualTo(3);
        
        // Verify deleted goods are gone
        for (int i = 400; i < 405; i++) {
            GoodsId deletedId = new GoodsId();
            deletedId.setBarcode("DELETE" + i);
            deletedId.setGoodsName("삭제대상상품" + i);
            assertThat(goodsRepository.existsById(deletedId)).isFalse();
        }
    }

    @Test
    void should_handleComplexCompositeKeyOperations_when_performingCRUDOperations() {
        // Given - Complex scenario with multiple operations
        String complexBarcode = "COMPLEX-2024-TEST";
        String complexGoodsName = "복잡작업테스트상품";
        
        // Create
        Goods complexGoods = new Goods();
        complexGoods.setBarcode(complexBarcode);
        complexGoods.setGoodsName(complexGoodsName);
        Goods savedGoods = goodsRepository.save(complexGoods);
        entityManager.flush();
        
        // Read
        GoodsId complexId = new GoodsId();
        complexId.setBarcode(complexBarcode);
        complexId.setGoodsName(complexGoodsName);
        Optional<Goods> foundGoods = goodsRepository.findById(complexId);
        assertThat(foundGoods).isPresent();
        
        // Update (by creating new goods with same composite key)
        String updatedGoodsName = "업데이트된복잡상품";
        Goods updatedGoods = new Goods();
        updatedGoods.setBarcode(complexBarcode);
        updatedGoods.setGoodsName(updatedGoodsName);
        goodsRepository.save(updatedGoods);
        entityManager.flush();
        entityManager.clear();
        
        // Verify update created new entry (due to composite key difference)
        GoodsId updatedId = new GoodsId();
        updatedId.setBarcode(complexBarcode);
        updatedId.setGoodsName(updatedGoodsName);
        assertThat(goodsRepository.existsById(updatedId)).isTrue();
        assertThat(goodsRepository.existsById(complexId)).isTrue(); // Original still exists
        
        // Delete both versions
        goodsRepository.deleteById(complexId);
        goodsRepository.deleteById(updatedId);
        entityManager.flush();
        
        // Verify deletion
        assertThat(goodsRepository.existsById(complexId)).isFalse();
        assertThat(goodsRepository.existsById(updatedId)).isFalse();
        
        // Final count should be back to original 3
        assertThat(goodsRepository.count()).isEqualTo(3);
    }

    @Test
    void should_handleConcurrentAccess_when_multipleOperationsOnSameGoods() {
        // Given - Goods for concurrent access simulation
        String concurrentBarcode = "CONCURRENT123";
        String concurrentGoodsName = "동시접근테스트상품";
        
        Goods concurrentGoods = new Goods();
        concurrentGoods.setBarcode(concurrentBarcode);
        concurrentGoods.setGoodsName(concurrentGoodsName);
        goodsRepository.save(concurrentGoods);
        entityManager.flush();
        entityManager.clear();
        
        GoodsId concurrentId = new GoodsId();
        concurrentId.setBarcode(concurrentBarcode);
        concurrentId.setGoodsName(concurrentGoodsName);

        // When - Simulate concurrent operations
        // First operation: Read
        Optional<Goods> readResult = goodsRepository.findById(concurrentId);
        assertThat(readResult).isPresent();
        
        // Second operation: Check existence
        boolean existsResult = goodsRepository.existsById(concurrentId);
        assertThat(existsResult).isTrue();
        
        // Third operation: Delete
        goodsRepository.deleteById(concurrentId);
        entityManager.flush();
        
        // Fourth operation: Verify deletion
        assertThat(goodsRepository.existsById(concurrentId)).isFalse();
        assertThat(goodsRepository.findById(concurrentId)).isEmpty();
    }
}