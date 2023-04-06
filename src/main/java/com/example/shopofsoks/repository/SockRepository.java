package com.example.shopofsoks.repository;

import com.example.shopofsoks.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SockRepository extends JpaRepository<Sock, Integer> {

}
