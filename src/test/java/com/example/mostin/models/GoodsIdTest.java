package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GoodsId 복합키 테스트")
class GoodsIdTest {

    private GoodsId goodsId1;
    private GoodsId goodsId2;
    private GoodsId goodsId3;

    @BeforeEach
    void setUp() {
        goodsId1 = new GoodsId();
        goodsId1.setBarcode("8801234567890");
        goodsId1.setGoodsName("삼성 갤럭시");

        goodsId2 = new GoodsId();
        goodsId2.setBarcode("8801234567890");
        goodsId2.setGoodsName("삼성 갤럭시");

        goodsId3 = new GoodsId();
        goodsId3.setBarcode("8801234567891");
        goodsId3.setGoodsName("애플 아이폰");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameFieldValues")
    void should_returnTrue_when_sameFieldValues() {
        // Given: 같은 필드 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함
        assertEquals(goodsId1, goodsId2);
        assertTrue(goodsId1.equals(goodsId2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentFieldValues")
    void should_returnFalse_when_differentFieldValues() {
        // Given: 다른 필드 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(goodsId1, goodsId3);
        assertFalse(goodsId1.equals(goodsId3));
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingObjects")
    void should_maintainEqualsSymmetry_when_comparingObjects() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(goodsId1.equals(goodsId2));
        assertTrue(goodsId2.equals(goodsId1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameObject")
    void should_maintainEqualsReflexivity_when_comparingSameObject() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함
        assertTrue(goodsId1.equals(goodsId1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingThreeObjects")
    void should_maintainEqualsTransitivity_when_comparingThreeObjects() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        GoodsId goodsId4 = new GoodsId();
        goodsId4.setBarcode("8801234567890");
        goodsId4.setGoodsName("삼성 갤럭시");
        
        // When & Then: equals 이행성이 유지되어야 함
        assertTrue(goodsId1.equals(goodsId2));
        assertTrue(goodsId2.equals(goodsId4));
        assertTrue(goodsId1.equals(goodsId4));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_objectsAreEqual")
    void should_returnSameHashCode_when_objectsAreEqual() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: 같은 객체는 같은 hashCode를 가져야 함
        assertEquals(goodsId1.hashCode(), goodsId2.hashCode());
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_calledMultipleTimes")
    void should_returnConsistentHashCode_when_calledMultipleTimes() {
        // Given: 객체
        int firstHashCode = goodsId1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = goodsId1.hashCode();
        int thirdHashCode = goodsId1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_comparedWithNull")
    void should_returnFalse_when_comparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(goodsId1.equals(null));
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: 객체
        
        // When: toString 호출
        String result = goodsId1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("8801234567890"));
        assertTrue(result.contains("삼성 갤럭시"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("barcode="));
        assertTrue(result.contains("goodsName="));
    }

    @Test
    @DisplayName("should_handleNullFields_when_equalsAndHashCodeCalled")
    void should_handleNullFields_when_equalsAndHashCodeCalled() {
        // Given: null 필드를 가진 객체들
        GoodsId nullGoodsId1 = new GoodsId();
        nullGoodsId1.setBarcode(null);
        nullGoodsId1.setGoodsName(null);
        
        GoodsId nullGoodsId2 = new GoodsId();
        nullGoodsId2.setBarcode(null);
        nullGoodsId2.setGoodsName(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullGoodsId1, nullGoodsId2);
        assertEquals(nullGoodsId1.hashCode(), nullGoodsId2.hashCode());
        assertNotEquals(goodsId1, nullGoodsId1);
    }

    @Test
    @DisplayName("should_ensureUniqueCompositeKey_when_differentCombinations")
    void should_ensureUniqueCompositeKey_when_differentCombinations() {
        // Given: 다른 조합의 복합키들
        GoodsId key1 = new GoodsId();
        key1.setBarcode("8801234567890");
        key1.setGoodsName("삼성 갤럭시");
        
        GoodsId key2 = new GoodsId();
        key2.setBarcode("8801234567890");
        key2.setGoodsName("LG 스마트폰");  // 같은 바코드, 다른 상품명
        
        GoodsId key3 = new GoodsId();
        key3.setBarcode("8801234567891");  // 다른 바코드
        key3.setGoodsName("삼성 갤럭시");  // 같은 상품명
        
        // When & Then: 모든 조합이 서로 다른 복합키여야 함
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key2, key3);
    }

    @Test
    @DisplayName("should_handleBarcodeFormats_when_differentBarcodeTypes")
    void should_handleBarcodeFormats_when_differentBarcodeTypes() {
        // Given: 다양한 바코드 형식들
        GoodsId upcA = new GoodsId();
        upcA.setBarcode("012345678905");  // UPC-A (12자리)
        upcA.setGoodsName("상품A");
        
        GoodsId ean13 = new GoodsId();
        ean13.setBarcode("8801234567890"); // EAN-13 (13자리)
        ean13.setGoodsName("상품B");
        
        GoodsId shortBarcode = new GoodsId();
        shortBarcode.setBarcode("123456");  // 짧은 바코드
        shortBarcode.setGoodsName("상품C");
        
        // When & Then: 다양한 바코드 형식이 올바르게 처리되어야 함
        assertNotEquals(upcA, ean13);
        assertNotEquals(upcA, shortBarcode);
        assertNotEquals(ean13, shortBarcode);
        
        // toString도 올바르게 처리되어야 함
        assertNotNull(upcA.toString());
        assertNotNull(ean13.toString());
        assertNotNull(shortBarcode.toString());
    }

    @Test
    @DisplayName("should_handleSpecialCharactersInGoodsName_when_creatingGoodsId")
    void should_handleSpecialCharactersInGoodsName_when_creatingGoodsId() {
        // Given: 특수문자가 포함된 상품명들
        GoodsId specialChars1 = new GoodsId();
        specialChars1.setBarcode("8801234567890");
        specialChars1.setGoodsName("삼성 갤럭시 S24+ (256GB)");
        
        GoodsId specialChars2 = new GoodsId();
        specialChars2.setBarcode("8801234567891");
        specialChars2.setGoodsName("애플 아이폰 15 Pro Max - 512GB");
        
        GoodsId emoji = new GoodsId();
        emoji.setBarcode("8801234567892");
        emoji.setGoodsName("스마일 😊 스마트폰");
        
        GoodsId englishKorean = new GoodsId();
        englishKorean.setBarcode("8801234567893");
        englishKorean.setGoodsName("Samsung Galaxy 갤럭시");
        
        // When & Then: 특수문자가 포함된 상품명도 올바르게 처리되어야 함
        assertNotNull(specialChars1.toString());
        assertNotNull(specialChars2.toString());
        assertNotNull(emoji.toString());
        assertNotNull(englishKorean.toString());
        
        assertNotEquals(specialChars1, specialChars2);
        assertNotEquals(specialChars1, emoji);
        assertNotEquals(specialChars1, englishKorean);
    }

    @Test
    @DisplayName("should_handleEmptyStrings_when_equalsAndHashCodeCalled")
    void should_handleEmptyStrings_when_equalsAndHashCodeCalled() {
        // Given: 빈 문자열을 가진 객체들
        GoodsId emptyGoodsId1 = new GoodsId();
        emptyGoodsId1.setBarcode("");
        emptyGoodsId1.setGoodsName("");
        
        GoodsId emptyGoodsId2 = new GoodsId();
        emptyGoodsId2.setBarcode("");
        emptyGoodsId2.setGoodsName("");
        
        // When & Then: 빈 문자열을 적절히 처리해야 함
        assertEquals(emptyGoodsId1, emptyGoodsId2);
        assertEquals(emptyGoodsId1.hashCode(), emptyGoodsId2.hashCode());
        assertNotEquals(goodsId1, emptyGoodsId1);
    }
}