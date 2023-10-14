package com.example.demo.reopsitory;

import com.example.demo.domain.Member;
import com.example.demo.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select i from Item i",
            countQuery = "select count(i.id) from Item i")
    Page<Item> findByItems(Pageable pageable);
}
