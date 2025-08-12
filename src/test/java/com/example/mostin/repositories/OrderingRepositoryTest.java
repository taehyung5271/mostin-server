package com.example.mostin.repositories;

import com.example.mostin.models.Employee;
import com.example.mostin.models.Goods;
import com.example.mostin.models.Ordering;
import com.example.mostin.models.OrderingId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderingRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderingRepository orderingRepository;

    private Employee testEmployee;
    private Goods testGoods1;
    private Goods testGoods2;
    private Ordering testOrdering1;
    private Ordering testOrdering2;
    private Ordering testOrdering3;

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

        // Create test goods
        testGoods1 = new Goods();
        testGoods1.setBarcode("1234567890123");
        testGoods1.setGoodsName("아메리카노");
        entityManager.persistAndFlush(testGoods1);

        testGoods2 = new Goods();
        testGoods2.setBarcode("1234567890124");
        testGoods2.setGoodsName("라떼");
        entityManager.persistAndFlush(testGoods2);

        // Create test ordering records
        testOrdering1 = new Ordering();
        testOrdering1.setOrderingDay(LocalDate.of(2024, 1, 15));
        testOrdering1.setEmployeeId("EMP001");
        testOrdering1.setBarcode("1234567890123");
        testOrdering1.setEmployeeName("김테스트");
        testOrdering1.setBoxNum(2);
        testOrdering1.setGoodsName("아메리카노");
        entityManager.persistAndFlush(testOrdering1);

        testOrdering2 = new Ordering();
        testOrdering2.setOrderingDay(LocalDate.of(2024, 1, 20));
        testOrdering2.setEmployeeId("EMP001");
        testOrdering2.setBarcode("1234567890124");
        testOrdering2.setEmployeeName("김테스트");
        testOrdering2.setBoxNum(1);
        testOrdering2.setGoodsName("라떼");
        entityManager.persistAndFlush(testOrdering2);

        testOrdering3 = new Ordering();
        testOrdering3.setOrderingDay(LocalDate.of(2024, 1, 16));
        testOrdering3.setEmployeeId("EMP001");
        testOrdering3.setBarcode("1234567890123");
        testOrdering3.setEmployeeName("김테스트");
        testOrdering3.setBoxNum(3);
        testOrdering3.setGoodsName("아메리카노");
        entityManager.persistAndFlush(testOrdering3);

        entityManager.clear();
    }

    @Test
    void should_findAllOrderingsByEmployeeId_when_employeeHasOrders() {
        // Given
        String employeeId = "EMP001";

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeId(employeeId);

        // Then
        assertThat(orderings).hasSize(3);
        assertThat(orderings)
                .extracting(Ordering::getEmployeeId)
                .containsOnly("EMP001");
    }

    @Test
    void should_returnEmptyList_when_employeeHasNoOrders() {
        // Given
        String nonExistentEmployeeId = "NONEXISTENT";

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeId(nonExistentEmployeeId);

        // Then
        assertThat(orderings).isEmpty();
    }

    @Test
    void should_findOrderingsSortedByDateDesc_when_employeeHasMultipleOrders() {
        // Given
        String employeeId = "EMP001";

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdOrderByOrderingDayDesc(employeeId);

        // Then
        assertThat(orderings).hasSize(3);
        assertThat(orderings)
                .extracting(Ordering::getOrderingDay)
                .containsExactly(
                        LocalDate.of(2024, 1, 20),
                        LocalDate.of(2024, 1, 16),
                        LocalDate.of(2024, 1, 15)
                );
    }

    @Test
    void should_returnEmptyListSorted_when_employeeHasNoOrdersForSorting() {
        // Given
        String nonExistentEmployeeId = "NONEXISTENT";

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdOrderByOrderingDayDesc(nonExistentEmployeeId);

        // Then
        assertThat(orderings).isEmpty();
    }

    @Test
    void should_findOrderingsBySpecificDate_when_employeeAndDateProvided() {
        // Given
        String employeeId = "EMP001";
        LocalDate specificDate = LocalDate.of(2024, 1, 15);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, specificDate);

        // Then
        assertThat(orderings).hasSize(1);
        assertThat(orderings.get(0).getOrderingDay()).isEqualTo(specificDate);
        assertThat(orderings.get(0).getEmployeeId()).isEqualTo(employeeId);
        assertThat(orderings.get(0).getGoodsName()).isEqualTo("아메리카노");
        assertThat(orderings.get(0).getBoxNum()).isEqualTo(2);
    }

    @Test
    void should_returnEmptyList_when_noOrderingForSpecificDate() {
        // Given
        String employeeId = "EMP001";
        LocalDate dateWithNoOrders = LocalDate.of(2024, 1, 18);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, dateWithNoOrders);

        // Then
        assertThat(orderings).isEmpty();
    }

    @Test
    void should_findMultipleOrderingsForSameDate_when_multipleDifferentGoodsOrdered() {
        // Given - Add another ordering for the same date but different goods
        Ordering additionalOrdering = new Ordering();
        additionalOrdering.setOrderingDay(LocalDate.of(2024, 1, 15));
        additionalOrdering.setEmployeeId("EMP001");
        additionalOrdering.setBarcode("1234567890124");
        additionalOrdering.setEmployeeName("김테스트");
        additionalOrdering.setBoxNum(1);
        additionalOrdering.setGoodsName("라떼");
        entityManager.persistAndFlush(additionalOrdering);

        String employeeId = "EMP001";
        LocalDate specificDate = LocalDate.of(2024, 1, 15);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, specificDate);

        // Then
        assertThat(orderings).hasSize(2);
        assertThat(orderings)
                .extracting(Ordering::getGoodsName)
                .containsExactlyInAnyOrder("아메리카노", "라떼");
    }

    @Test
    @Transactional
    void should_deleteOrderingsByEmployeeIdAndDate_when_orderingsExist() {
        // Given
        String employeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // Verify ordering exists before deletion
        List<Ordering> orderingsBeforeDelete = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, targetDate);
        assertThat(orderingsBeforeDelete).hasSize(1);

        // When
        orderingRepository.deleteByEmployeeIdAndOrderingDay(employeeId, targetDate);
        entityManager.flush();
        entityManager.clear();

        // Then
        List<Ordering> orderingsAfterDelete = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, targetDate);
        assertThat(orderingsAfterDelete).isEmpty();

        // Verify other orderings are not affected
        List<Ordering> remainingOrderings = orderingRepository.findByEmployeeId(employeeId);
        assertThat(remainingOrderings).hasSize(2);
    }

    @Test
    @Transactional
    void should_deleteMultipleOrderings_when_multiplOrderingsExistForSameDate() {
        // Given - Add another ordering for the same date
        Ordering additionalOrdering = new Ordering();
        additionalOrdering.setOrderingDay(LocalDate.of(2024, 1, 15));
        additionalOrdering.setEmployeeId("EMP001");
        additionalOrdering.setBarcode("1234567890124");
        additionalOrdering.setEmployeeName("김테스트");
        additionalOrdering.setBoxNum(1);
        additionalOrdering.setGoodsName("라떼");
        entityManager.persistAndFlush(additionalOrdering);

        String employeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // Verify multiple orderings exist
        List<Ordering> orderingsBeforeDelete = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, targetDate);
        assertThat(orderingsBeforeDelete).hasSize(2);

        // When
        orderingRepository.deleteByEmployeeIdAndOrderingDay(employeeId, targetDate);
        entityManager.flush();
        entityManager.clear();

        // Then
        List<Ordering> orderingsAfterDelete = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, targetDate);
        assertThat(orderingsAfterDelete).isEmpty();

        // Verify other orderings remain
        List<Ordering> remainingOrderings = orderingRepository.findByEmployeeId(employeeId);
        assertThat(remainingOrderings).hasSize(2);
    }

    @Test
    @Transactional
    void should_notDeleteAnything_when_noOrderingExistsForDate() {
        // Given
        String employeeId = "EMP001";
        LocalDate nonExistentDate = LocalDate.of(2024, 1, 18);

        // Verify no orderings exist for the date
        List<Ordering> orderingsBeforeDelete = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, nonExistentDate);
        assertThat(orderingsBeforeDelete).isEmpty();

        // When
        orderingRepository.deleteByEmployeeIdAndOrderingDay(employeeId, nonExistentDate);
        entityManager.flush();
        entityManager.clear();

        // Then - All original orderings should remain
        List<Ordering> allOrderings = orderingRepository.findByEmployeeId(employeeId);
        assertThat(allOrderings).hasSize(3);
    }

    @Test
    @Transactional
    void should_notDeleteOtherEmployeeOrderings_when_deletingByEmployeeIdAndDate() {
        // Given - Create ordering for another employee on the same date
        Employee anotherEmployee = new Employee();
        anotherEmployee.setEmployeeId("EMP002");
        anotherEmployee.setEmployeeName("박테스트");
        anotherEmployee.setEmployeePwd("password456");
        anotherEmployee.setPhoneNum("010-9876-5432");
        anotherEmployee.setEmployeeType("정규직");
        anotherEmployee.setAddress("서울시 서초구");
        anotherEmployee.setWorkPlaceName("본사");
        entityManager.persistAndFlush(anotherEmployee);

        Ordering anotherEmployeeOrdering = new Ordering();
        anotherEmployeeOrdering.setOrderingDay(LocalDate.of(2024, 1, 15));
        anotherEmployeeOrdering.setEmployeeId("EMP002");
        anotherEmployeeOrdering.setBarcode("1234567890123");
        anotherEmployeeOrdering.setEmployeeName("박테스트");
        anotherEmployeeOrdering.setBoxNum(1);
        anotherEmployeeOrdering.setGoodsName("아메리카노");
        entityManager.persistAndFlush(anotherEmployeeOrdering);

        String targetEmployeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 15);

        // When
        orderingRepository.deleteByEmployeeIdAndOrderingDay(targetEmployeeId, targetDate);
        entityManager.flush();
        entityManager.clear();

        // Then
        List<Ordering> emp001Orderings = orderingRepository.findByEmployeeIdAndOrderingDay("EMP001", targetDate);
        List<Ordering> emp002Orderings = orderingRepository.findByEmployeeIdAndOrderingDay("EMP002", targetDate);

        assertThat(emp001Orderings).isEmpty();
        assertThat(emp002Orderings).hasSize(1);
        assertThat(emp002Orderings.get(0).getEmployeeId()).isEqualTo("EMP002");
    }

    @Test
    void should_handleNullValues_when_queryingWithNullEmployeeId() {
        // Given
        String nullEmployeeId = null;

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeId(nullEmployeeId);

        // Then
        assertThat(orderings).isEmpty();
    }

    @Test
    void should_handleNullValues_when_queryingWithNullDate() {
        // Given
        String employeeId = "EMP001";
        LocalDate nullDate = null;

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, nullDate);

        // Then
        assertThat(orderings).isEmpty();
    }

    @Test
    void should_findOrderingsWithDifferentBoxNumbers_when_sameGoodsDifferentQuantities() {
        // Given - Add more orderings with different box numbers
        Ordering largeOrder = new Ordering();
        largeOrder.setOrderingDay(LocalDate.of(2024, 1, 25));
        largeOrder.setEmployeeId("EMP001");
        largeOrder.setBarcode("1234567890123");
        largeOrder.setEmployeeName("김테스트");
        largeOrder.setBoxNum(10); // Large quantity
        largeOrder.setGoodsName("아메리카노");
        entityManager.persistAndFlush(largeOrder);

        Ordering smallOrder = new Ordering();
        smallOrder.setOrderingDay(LocalDate.of(2024, 1, 25));
        smallOrder.setEmployeeId("EMP001");
        smallOrder.setBarcode("1234567890124");
        smallOrder.setEmployeeName("김테스트");
        smallOrder.setBoxNum(1); // Small quantity
        smallOrder.setGoodsName("라떼");
        entityManager.persistAndFlush(smallOrder);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay("EMP001", LocalDate.of(2024, 1, 25));

        // Then
        assertThat(orderings).hasSize(2);
        assertThat(orderings)
                .extracting(Ordering::getBoxNum)
                .containsExactlyInAnyOrder(10, 1);
    }

    @Test
    @Transactional
    void should_maintainConsistency_when_deletingAndQuerying() {
        // Given
        String employeeId = "EMP001";
        LocalDate targetDate = LocalDate.of(2024, 1, 16);
        
        // Verify initial state
        List<Ordering> initialOrderings = orderingRepository.findByEmployeeId(employeeId);
        assertThat(initialOrderings).hasSize(3);

        // When - Delete and immediately query
        orderingRepository.deleteByEmployeeIdAndOrderingDay(employeeId, targetDate);
        entityManager.flush();
        
        List<Ordering> remainingOrderings = orderingRepository.findByEmployeeId(employeeId);
        List<Ordering> deletedDateOrderings = orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, targetDate);

        // Then
        assertThat(remainingOrderings).hasSize(2);
        assertThat(deletedDateOrderings).isEmpty();
        assertThat(remainingOrderings)
                .extracting(Ordering::getOrderingDay)
                .doesNotContain(targetDate);
    }

    @Test
    void should_handleLargeDataset_when_sortingManyOrderings() {
        // Given - Create employee for bulk test
        Employee bulkEmployee = new Employee();
        bulkEmployee.setEmployeeId("EMP_BULK");
        bulkEmployee.setEmployeeName("대량주문테스트");
        bulkEmployee.setEmployeePwd("bulkPassword");
        bulkEmployee.setPhoneNum("010-0000-0000");
        bulkEmployee.setEmployeeType("테스트");
        bulkEmployee.setAddress("테스트주소");
        bulkEmployee.setWorkPlaceName("테스트지점");
        entityManager.persistAndFlush(bulkEmployee);

        // Create many orderings with different dates
        LocalDate baseDate = LocalDate.of(2024, 1, 1);
        for (int i = 0; i < 50; i++) {
            Ordering ordering = new Ordering();
            ordering.setOrderingDay(baseDate.plusDays(i));
            ordering.setEmployeeId("EMP_BULK");
            ordering.setBarcode("BULK" + String.format("%013d", i));
            ordering.setEmployeeName("대량주문테스트");
            ordering.setBoxNum(i + 1);
            ordering.setGoodsName("대량상품" + i);
            entityManager.persist(ordering);
        }
        entityManager.flush();
        entityManager.clear();

        // When - Query sorted orderings
        List<Ordering> sortedOrderings = orderingRepository.findByEmployeeIdOrderByOrderingDayDesc("EMP_BULK");

        // Then - Verify sorting and size
        assertThat(sortedOrderings).hasSize(50);
        
        // Verify descending order
        LocalDate previousDate = LocalDate.MAX;
        for (Ordering ordering : sortedOrderings) {
            assertThat(ordering.getOrderingDay()).isBeforeOrEqualTo(previousDate);
            previousDate = ordering.getOrderingDay();
        }
        
        // Verify first and last elements
        assertThat(sortedOrderings.get(0).getOrderingDay()).isEqualTo(baseDate.plusDays(49));
        assertThat(sortedOrderings.get(49).getOrderingDay()).isEqualTo(baseDate);
    }

    @Test
    void should_handleSpecialCharactersInGoodsName_when_queryingOrderings() {
        // Given - Ordering with special characters in goods name
        Ordering specialGoodsOrdering = new Ordering();
        specialGoodsOrdering.setOrderingDay(LocalDate.of(2024, 1, 28));
        specialGoodsOrdering.setEmployeeId("EMP001");
        specialGoodsOrdering.setBarcode("SPECIAL123456789");
        specialGoodsOrdering.setEmployeeName("김테스트");
        specialGoodsOrdering.setBoxNum(1);
        specialGoodsOrdering.setGoodsName("특수상품 & ★★★");
        entityManager.persistAndFlush(specialGoodsOrdering);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(
                "EMP001", LocalDate.of(2024, 1, 28));

        // Then
        assertThat(orderings).hasSize(1);
        assertThat(orderings.get(0).getGoodsName()).isEqualTo("특수상품 & ★★★");
    }

    @Test
    void should_handleZeroBoxNumber_when_orderingWithZeroQuantity() {
        // Given - Ordering with zero box number
        Ordering zeroBoxOrdering = new Ordering();
        zeroBoxOrdering.setOrderingDay(LocalDate.of(2024, 1, 29));
        zeroBoxOrdering.setEmployeeId("EMP001");
        zeroBoxOrdering.setBarcode("ZERO123456789");
        zeroBoxOrdering.setEmployeeName("김테스트");
        zeroBoxOrdering.setBoxNum(0); // Zero quantity
        zeroBoxOrdering.setGoodsName("제로수량상품");
        entityManager.persistAndFlush(zeroBoxOrdering);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(
                "EMP001", LocalDate.of(2024, 1, 29));

        // Then
        assertThat(orderings).hasSize(1);
        assertThat(orderings.get(0).getBoxNum()).isEqualTo(0);
        assertThat(orderings.get(0).getGoodsName()).isEqualTo("제로수량상품");
    }

    @Test
    void should_handleNegativeBoxNumber_when_orderingWithNegativeQuantity() {
        // Given - Ordering with negative box number (cancellation or return)
        Ordering negativeBoxOrdering = new Ordering();
        negativeBoxOrdering.setOrderingDay(LocalDate.of(2024, 1, 30));
        negativeBoxOrdering.setEmployeeId("EMP001");
        negativeBoxOrdering.setBarcode("NEG123456789");
        negativeBoxOrdering.setEmployeeName("김테스트");
        negativeBoxOrdering.setBoxNum(-2); // Negative quantity (return/cancellation)
        negativeBoxOrdering.setGoodsName("반품상품");
        entityManager.persistAndFlush(negativeBoxOrdering);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(
                "EMP001", LocalDate.of(2024, 1, 30));

        // Then
        assertThat(orderings).hasSize(1);
        assertThat(orderings.get(0).getBoxNum()).isEqualTo(-2);
        assertThat(orderings.get(0).getGoodsName()).isEqualTo("반품상품");
    }

    @Test
    void should_sortCorrectly_when_sameDatesWithDifferentOrderings() {
        // Given - Add orderings on same dates as existing ones
        Ordering sameDate1 = new Ordering();
        sameDate1.setOrderingDay(LocalDate.of(2024, 1, 20)); // Same as testOrdering2
        sameDate1.setEmployeeId("EMP001");
        sameDate1.setBarcode("SAME123456789");
        sameDate1.setEmployeeName("김테스트");
        sameDate1.setBoxNum(5);
        sameDate1.setGoodsName("동일날짜상품");
        entityManager.persistAndFlush(sameDate1);

        Ordering sameDate2 = new Ordering();
        sameDate2.setOrderingDay(LocalDate.of(2024, 1, 15)); // Same as testOrdering1
        sameDate2.setEmployeeId("EMP001");
        sameDate2.setBarcode("SAME987654321");
        sameDate2.setEmployeeName("김테스트");
        sameDate2.setBoxNum(3);
        sameDate2.setGoodsName("동일날짜상품다른버전");
        entityManager.persistAndFlush(sameDate2);

        // When
        List<Ordering> sortedOrderings = orderingRepository.findByEmployeeIdOrderByOrderingDayDesc("EMP001");

        // Then - Should still be sorted by date descending
        assertThat(sortedOrderings).hasSize(5);
        
        // Verify date order (multiple orderings per date are allowed)
        LocalDate previousDate = LocalDate.MAX;
        for (Ordering ordering : sortedOrderings) {
            assertThat(ordering.getOrderingDay()).isBeforeOrEqualTo(previousDate);
            previousDate = ordering.getOrderingDay();
        }
        
        // Verify that orderings with same date are grouped together
        long jan20Count = sortedOrderings.stream()
                .filter(o -> o.getOrderingDay().equals(LocalDate.of(2024, 1, 20)))
                .count();
        long jan15Count = sortedOrderings.stream()
                .filter(o -> o.getOrderingDay().equals(LocalDate.of(2024, 1, 15)))
                .count();
        
        assertThat(jan20Count).isEqualTo(2);
        assertThat(jan15Count).isEqualTo(2);
    }

    @Test
    @Transactional
    void should_handleCascadeOperations_when_deletingWithRelatedData() {
        // Given - Add more complex ordering scenario
        Ordering complexOrdering = new Ordering();
        complexOrdering.setOrderingDay(LocalDate.of(2024, 1, 31));
        complexOrdering.setEmployeeId("EMP001");
        complexOrdering.setBarcode("COMPLEX123456789");
        complexOrdering.setEmployeeName("김테스트");
        complexOrdering.setBoxNum(100); // Large quantity
        complexOrdering.setGoodsName("복잡주문상품");
        entityManager.persistAndFlush(complexOrdering);

        // Verify initial state
        List<Ordering> initialOrderings = orderingRepository.findByEmployeeId("EMP001");
        assertThat(initialOrderings).hasSize(4); // 3 original + 1 complex

        // When - Delete specific date
        orderingRepository.deleteByEmployeeIdAndOrderingDay("EMP001", LocalDate.of(2024, 1, 31));
        entityManager.flush();
        entityManager.clear();

        // Then - Verify deletion integrity
        List<Ordering> remainingOrderings = orderingRepository.findByEmployeeId("EMP001");
        assertThat(remainingOrderings).hasSize(3);
        assertThat(remainingOrderings)
                .extracting(Ordering::getOrderingDay)
                .doesNotContain(LocalDate.of(2024, 1, 31));
        assertThat(remainingOrderings)
                .extracting(Ordering::getGoodsName)
                .doesNotContain("복잡주문상품");
    }

    @Test
    void should_handleLongBarcode_when_orderingWithExtendedBarcode() {
        // Given - Ordering with very long barcode
        String longBarcode = "123456789012345678901234567890";
        Ordering longBarcodeOrdering = new Ordering();
        longBarcodeOrdering.setOrderingDay(LocalDate.of(2024, 2, 1));
        longBarcodeOrdering.setEmployeeId("EMP001");
        longBarcodeOrdering.setBarcode(longBarcode);
        longBarcodeOrdering.setEmployeeName("김테스트");
        longBarcodeOrdering.setBoxNum(1);
        longBarcodeOrdering.setGoodsName("긴바코드상품");
        entityManager.persistAndFlush(longBarcodeOrdering);

        // When
        List<Ordering> orderings = orderingRepository.findByEmployeeIdAndOrderingDay(
                "EMP001", LocalDate.of(2024, 2, 1));

        // Then
        assertThat(orderings).hasSize(1);
        assertThat(orderings.get(0).getBarcode()).isEqualTo(longBarcode);
        assertThat(orderings.get(0).getGoodsName()).isEqualTo("긴바코드상품");
    }

    @Test
    @Transactional
    void should_performBatchDeletion_when_deletingMultipleDates() {
        // Given - Add orderings for multiple dates
        LocalDate[] datesToDelete = {
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 2, 2),
                LocalDate.of(2024, 2, 3)
        };

        for (int i = 0; i < datesToDelete.length; i++) {
            Ordering ordering = new Ordering();
            ordering.setOrderingDay(datesToDelete[i]);
            ordering.setEmployeeId("EMP001");
            ordering.setBarcode("BATCH" + String.format("%010d", i));
            ordering.setEmployeeName("김테스트");
            ordering.setBoxNum(i + 1);
            ordering.setGoodsName("일괄삭제상품" + i);
            entityManager.persistAndFlush(ordering);
        }

        // Verify initial state
        List<Ordering> initialOrderings = orderingRepository.findByEmployeeId("EMP001");
        assertThat(initialOrderings).hasSize(6); // 3 original + 3 batch

        // When - Delete each date
        for (LocalDate date : datesToDelete) {
            orderingRepository.deleteByEmployeeIdAndOrderingDay("EMP001", date);
        }
        entityManager.flush();
        entityManager.clear();

        // Then - Verify all batch orderings are deleted
        List<Ordering> remainingOrderings = orderingRepository.findByEmployeeId("EMP001");
        assertThat(remainingOrderings).hasSize(3); // Only original orderings remain
        
        for (LocalDate date : datesToDelete) {
            List<Ordering> dateOrderings = orderingRepository.findByEmployeeIdAndOrderingDay("EMP001", date);
            assertThat(dateOrderings).isEmpty();
        }
    }
}