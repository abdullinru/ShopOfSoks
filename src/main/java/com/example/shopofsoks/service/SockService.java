package com.example.shopofsoks.service;

import com.example.shopofsoks.repository.SockRepository;
import org.springframework.stereotype.Service;

@Service
public class SockService {
    private final SockRepository sockRepository;


    public SockService(SockRepository sockRepository) {
        this.sockRepository = sockRepository;

    }
}
