package com.example.mostin.repositories;

import com.example.mostin.models.Goods;
import com.example.mostin.models.GoodsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, GoodsId> {
}
