package com.example.shopofsoks.repository;

import com.example.shopofsoks.model.Sock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SockRepository extends JpaRepository<Sock, Integer> {

    Sock findSocksByColorAndCottonPart(String color, int cottonPart);

    List<Sock> findSocksByColorAndCottonPartAfter(String color, int cottonPart);
    List<Sock> findSocksByColorAndCottonPartBefore(String color, int cottonPart);

}
