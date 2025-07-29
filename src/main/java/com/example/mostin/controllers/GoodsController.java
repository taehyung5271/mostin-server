package com.example.mostin.controllers;

import com.example.mostin.models.Goods;
import com.example.mostin.repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
