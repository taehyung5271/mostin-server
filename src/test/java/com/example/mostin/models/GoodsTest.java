package com.example.mostin.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Goods 엔티티 테스트")
class GoodsTest {

    private Goods goods1;
    private Goods goods2;
    private Goods goods3;

    @BeforeEach
    void setUp() {
        goods1 = new Goods();
        goods1.setBarcode("8801234567890");
        goods1.setGoodsName("삼성 갤럭시");

        goods2 = new Goods();
        goods2.setBarcode("8801234567890");
        goods2.setGoodsName("삼성 갤럭시");

        goods3 = new Goods();
        goods3.setBarcode("8801234567891");
        goods3.setGoodsName("애플 아이폰");
    }

    @Test
    @DisplayName("should_returnTrue_when_sameCompositeKeyValues")
    void should_returnTrue_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: equals 메서드가 true를 반환해야 함 (복합키 기준)
        assertEquals(goods1, goods2);
        assertTrue(goods1.equals(goods2));
    }

    @Test
    @DisplayName("should_returnFalse_when_differentCompositeKeyValues")
    void should_returnFalse_when_differentCompositeKeyValues() {
        // Given: 다른 복합키 값을 가진 객체들
        
        // When & Then: equals 메서드가 false를 반환해야 함
        assertNotEquals(goods1, goods3);
        assertFalse(goods1.equals(goods3));
    }

    @Test
    @DisplayName("should_returnSameHashCode_when_sameCompositeKeyValues")
    void should_returnSameHashCode_when_sameCompositeKeyValues() {
        // Given: 같은 복합키 값을 가진 두 객체
        
        // When & Then: 같은 hashCode를 반환해야 함
        assertEquals(goods1.hashCode(), goods2.hashCode());
    }

    @Test
    @DisplayName("should_returnProperStringFormat_when_toStringCalled")
    void should_returnProperStringFormat_when_toStringCalled() {
        // Given: Goods 객체
        
        // When: toString 호출
        String result = goods1.toString();
        
        // Then: 적절한 문자열 형식이 반환되어야 함
        assertNotNull(result);
        assertTrue(result.contains("8801234567890"));
        assertTrue(result.contains("삼성 갤럭시"));
        // Lombok toString()은 클래스명 대신 필드명=값 형식을 사용
        assertTrue(result.contains("barcode="));
        assertTrue(result.contains("goodsName="));
    }

    @Test
    @DisplayName("should_setAndGetAllFields_when_lombokDataUsed")
    void should_setAndGetAllFields_when_lombokDataUsed() {
        // Given: 새로운 Goods 객체
        Goods goods = new Goods();
        
        // When: 모든 필드 설정
        goods.setBarcode("1234567890123");
        goods.setGoodsName("테스트 상품");
        
        // Then: 모든 getter가 올바르게 동작해야 함
        assertEquals("1234567890123", goods.getBarcode());
        assertEquals("테스트 상품", goods.getGoodsName());
    }

    @Test
    @DisplayName("should_maintainEqualsSymmetry_when_comparingGoods")
    void should_maintainEqualsSymmetry_when_comparingGoods() {
        // Given: 같은 값을 가진 두 객체
        
        // When & Then: equals 대칭성이 유지되어야 함
        assertTrue(goods1.equals(goods2));
        assertTrue(goods2.equals(goods1));
    }

    @Test
    @DisplayName("should_maintainEqualsReflexivity_when_comparingSameGoods")
    void should_maintainEqualsReflexivity_when_comparingSameGoods() {
        // Given: 동일한 객체
        
        // When & Then: equals 반사성이 유지되어야 함
        assertTrue(goods1.equals(goods1));
    }

    @Test
    @DisplayName("should_maintainEqualsTransitivity_when_comparingThreeGoods")
    void should_maintainEqualsTransitivity_when_comparingThreeGoods() {
        // Given: 세 번째 객체 생성 (첫 번째, 두 번째와 같은 값)
        Goods goods4 = new Goods();
        goods4.setBarcode("8801234567890");
        goods4.setGoodsName("삼성 갤럭시");
        
        // When & Then: equals 이행성이 유지되어야 함
        assertTrue(goods1.equals(goods2));
        assertTrue(goods2.equals(goods4));
        assertTrue(goods1.equals(goods4));
    }

    @Test
    @DisplayName("should_returnConsistentHashCode_when_calledMultipleTimes")
    void should_returnConsistentHashCode_when_calledMultipleTimes() {
        // Given: Goods 객체
        int firstHashCode = goods1.hashCode();
        
        // When: 여러 번 hashCode 호출
        int secondHashCode = goods1.hashCode();
        int thirdHashCode = goods1.hashCode();
        
        // Then: 일관된 hashCode 값이 반환되어야 함
        assertEquals(firstHashCode, secondHashCode);
        assertEquals(secondHashCode, thirdHashCode);
    }

    @Test
    @DisplayName("should_returnFalse_when_comparedWithNull")
    void should_returnFalse_when_comparedWithNull() {
        // Given: 유효한 객체와 null
        
        // When & Then: null과 비교시 false를 반환해야 함
        assertFalse(goods1.equals(null));
    }

    @Test
    @DisplayName("should_handleNullFields_when_equalsAndHashCodeCalled")
    void should_handleNullFields_when_equalsAndHashCodeCalled() {
        // Given: null 필드를 가진 객체들
        Goods nullGoods1 = new Goods();
        nullGoods1.setBarcode(null);
        nullGoods1.setGoodsName(null);
        
        Goods nullGoods2 = new Goods();
        nullGoods2.setBarcode(null);
        nullGoods2.setGoodsName(null);
        
        // When & Then: null 필드를 적절히 처리해야 함
        assertEquals(nullGoods1, nullGoods2);
        assertEquals(nullGoods1.hashCode(), nullGoods2.hashCode());
        assertNotEquals(goods1, nullGoods1);
    }

    @Test
    @DisplayName("should_ensureUniqueCompositeKey_when_differentCombinations")
    void should_ensureUniqueCompositeKey_when_differentCombinations() {
        // Given: 다른 조합의 복합키들
        Goods key1 = new Goods();
        key1.setBarcode("8801234567890");
        key1.setGoodsName("삼성 갤럭시");
        
        Goods key2 = new Goods();
        key2.setBarcode("8801234567890");
        key2.setGoodsName("LG 스마트폰");  // 같은 바코드, 다른 상품명
        
        Goods key3 = new Goods();
        key3.setBarcode("8801234567891");  // 다른 바코드
        key3.setGoodsName("삼성 갤럭시");  // 같은 상품명
        
        // When & Then: 모든 조합이 서로 다른 복합키여야 함
        assertNotEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key2, key3);
    }

    @Test
    @DisplayName("should_handleVariousBarcodeFormats_when_creatingGoods")
    void should_handleVariousBarcodeFormats_when_creatingGoods() {
        // Given: 다양한 바코드 형식들
        Goods upcA = new Goods();
        upcA.setBarcode("012345678905");  // UPC-A (12자리)
        upcA.setGoodsName("UPC-A 상품");
        
        Goods ean13 = new Goods();
        ean13.setBarcode("8801234567890"); // EAN-13 (13자리)
        ean13.setGoodsName("EAN-13 상품");
        
        Goods shortCode = new Goods();
        shortCode.setBarcode("123456");    // 짧은 코드
        shortCode.setGoodsName("짧은 코드 상품");
        
        Goods longCode = new Goods();
        longCode.setBarcode("12345678901234567890"); // 긴 코드
        longCode.setGoodsName("긴 코드 상품");
        
        // When & Then: 다양한 바코드 형식이 올바르게 처리되어야 함
        assertEquals("012345678905", upcA.getBarcode());
        assertEquals("8801234567890", ean13.getBarcode());
        assertEquals("123456", shortCode.getBarcode());
        assertEquals("12345678901234567890", longCode.getBarcode());
        
        assertNotEquals(upcA, ean13);
        assertNotEquals(ean13, shortCode);
        assertNotEquals(shortCode, longCode);
    }

    @Test
    @DisplayName("should_handleSpecialCharactersInGoodsName_when_creatingGoods")
    void should_handleSpecialCharactersInGoodsName_when_creatingGoods() {
        // Given: 특수문자가 포함된 상품명들
        Goods specialChars = new Goods();
        specialChars.setBarcode("8801234567890");
        specialChars.setGoodsName("삼성 갤럭시 S24+ (256GB) - 블랙");
        
        Goods withNumbers = new Goods();
        withNumbers.setBarcode("8801234567891");
        withNumbers.setGoodsName("아이폰 15 Pro Max 512GB");
        
        Goods withSymbols = new Goods();
        withSymbols.setBarcode("8801234567892");
        withSymbols.setGoodsName("LG 스마트TV 55\" OLED C3 & 사운드바");
        
        Goods withEmoji = new Goods();
        withEmoji.setBarcode("8801234567893");
        withEmoji.setGoodsName("스마일 😊 케이스");
        
        // When & Then: 특수문자가 포함된 상품명도 올바르게 처리되어야 함
        assertEquals("삼성 갤럭시 S24+ (256GB) - 블랙", specialChars.getGoodsName());
        assertEquals("아이폰 15 Pro Max 512GB", withNumbers.getGoodsName());
        assertEquals("LG 스마트TV 55\" OLED C3 & 사운드바", withSymbols.getGoodsName());
        assertEquals("스마일 😊 케이스", withEmoji.getGoodsName());
        
        assertNotNull(specialChars.toString());
        assertNotNull(withNumbers.toString());
        assertNotNull(withSymbols.toString());
        assertNotNull(withEmoji.toString());
    }

    @Test
    @DisplayName("should_handleEmptyStrings_when_settingFields")
    void should_handleEmptyStrings_when_settingFields() {
        // Given: 빈 문자열을 가진 객체들
        Goods emptyGoods1 = new Goods();
        emptyGoods1.setBarcode("");
        emptyGoods1.setGoodsName("");
        
        Goods emptyGoods2 = new Goods();
        emptyGoods2.setBarcode("");
        emptyGoods2.setGoodsName("");
        
        // When & Then: 빈 문자열을 적절히 처리해야 함
        assertEquals(emptyGoods1, emptyGoods2);
        assertEquals(emptyGoods1.hashCode(), emptyGoods2.hashCode());
        assertNotEquals(goods1, emptyGoods1);
        
        assertEquals("", emptyGoods1.getBarcode());
        assertEquals("", emptyGoods1.getGoodsName());
    }

    @Test
    @DisplayName("should_maintainConsistentBehavior_when_multipleOperations")
    void should_maintainConsistentBehavior_when_multipleOperations() {
        // Given: Goods 객체
        
        // When: 여러 번의 연속된 연산
        int hashCode1 = goods1.hashCode();
        String toString1 = goods1.toString();
        boolean equals1 = goods1.equals(goods2);
        
        int hashCode2 = goods1.hashCode();
        String toString2 = goods1.toString();
        boolean equals2 = goods1.equals(goods2);
        
        // Then: 일관된 결과를 반환해야 함
        assertEquals(hashCode1, hashCode2);
        assertEquals(toString1, toString2);
        assertEquals(equals1, equals2);
        assertTrue(equals1);
    }

    @Test
    @DisplayName("should_handleLongGoodsNames_when_settingValues")
    void should_handleLongGoodsNames_when_settingValues() {
        // Given: 매우 긴 상품명
        String longGoodsName = "삼성전자 갤럭시 S24 울트라 5G 256GB 티타늄 그레이 언락 버전 - 프리미엄 스마트폰 (Snapdragon 8 Gen 3, 12GB RAM, 6.8인치 Dynamic AMOLED 2X 디스플레이, 200MP 카메라, S펜 포함, 무선충전, 방수방진, 안드로이드 14)";
        
        Goods longNameGoods = new Goods();
        longNameGoods.setBarcode("8801234567890");
        longNameGoods.setGoodsName(longGoodsName);
        
        // When & Then: 긴 상품명도 올바르게 처리되어야 함
        assertEquals(longGoodsName, longNameGoods.getGoodsName());
        assertNotNull(longNameGoods.toString());
        assertTrue(longNameGoods.toString().contains("삼성전자"));
        assertTrue(longNameGoods.toString().contains("안드로이드 14"));
    }

    @Test
    @DisplayName("should_handleMixedLanguageGoodsNames_when_settingValues")
    void should_handleMixedLanguageGoodsNames_when_settingValues() {
        // Given: 다국어가 섞인 상품명들
        Goods koreanEnglish = new Goods();
        koreanEnglish.setBarcode("8801234567890");
        koreanEnglish.setGoodsName("Samsung Galaxy 갤럭시 S24");
        
        Goods koreanJapanese = new Goods();
        koreanJapanese.setBarcode("8801234567891");
        koreanJapanese.setGoodsName("소니 Sony ソニー 헤드폰");
        
        Goods multiLanguage = new Goods();
        multiLanguage.setBarcode("8801234567892");
        multiLanguage.setGoodsName("Apple iPhone アイフォン 苹果手机 애플 아이폰");
        
        // When & Then: 다국어 상품명도 올바르게 처리되어야 함
        assertEquals("Samsung Galaxy 갤럭시 S24", koreanEnglish.getGoodsName());
        assertEquals("소니 Sony ソニー 헤드폰", koreanJapanese.getGoodsName());
        assertEquals("Apple iPhone アイフォン 苹果手机 애플 아이폰", multiLanguage.getGoodsName());
        
        assertNotEquals(koreanEnglish, koreanJapanese);
        assertNotEquals(koreanJapanese, multiLanguage);
        assertNotEquals(koreanEnglish, multiLanguage);
        
        assertNotNull(koreanEnglish.toString());
        assertNotNull(koreanJapanese.toString());
        assertNotNull(multiLanguage.toString());
    }
}