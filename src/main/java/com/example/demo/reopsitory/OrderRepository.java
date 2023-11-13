package com.example.demo.reopsitory;

import com.example.demo.domain.Order;
import com.example.demo.web.OrderSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrderByMemberId(Long memberId);
}
