package com.example.shopofsoks.repository;

import com.example.shopofsoks.model.SockCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SockCountRepository extends JpaRepository<SockCount, Integer> {
}
