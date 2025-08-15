package com.example.mostin.controllers;

import com.example.mostin.models.Goods;
import com.example.mostin.models.GoodsId;
import com.example.mostin.repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsRepository goodsRepository;

    @GetMapping
    public List<Goods> getAllGoods() {
        return goodsRepository.findAll();
    }

    @PostMapping
    public Goods createGoods(@RequestBody Goods goods) {
        return goodsRepository.save(goods);
    }

    @GetMapping("/count")
    public long getGoodsCount() {
        return goodsRepository.count();
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<?> deleteGoods(@PathVariable String barcode) {
        // Goods는 복합키를 사용하므로 barcode로만 조회해서 삭제
        List<Goods> goodsList = goodsRepository.findAll();
        
        for (Goods goods : goodsList) {
            if (barcode.equals(goods.getBarcode())) {
                goodsRepository.delete(goods);
                return ResponseEntity.ok().build();
            }
        }
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{barcode}")
    public ResponseEntity<Goods> updateGoods(@PathVariable String barcode, @RequestBody Goods goodsDetails) {
        List<Goods> goodsList = goodsRepository.findAll();
        
        for (Goods goods : goodsList) {
            if (barcode.equals(goods.getBarcode())) {
                goods.setGoodsName(goodsDetails.getGoodsName());
                Goods updatedGoods = goodsRepository.save(goods);
                return ResponseEntity.ok(updatedGoods);
            }
        }
        
        return ResponseEntity.notFound().build();
    }
}
