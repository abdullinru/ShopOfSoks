package com.example.shopofsoks.service;

import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.mapper.SockMapper;
import com.example.shopofsoks.model.Sock;
import com.example.shopofsoks.model.SockCount;
import com.example.shopofsoks.repository.SockCountRepository;
import com.example.shopofsoks.repository.SockRepository;
import org.springframework.stereotype.Service;

@Service
public class SockService {
    private final SockRepository sockRepository;
    private final SockCountRepository sockCountRepository;


    public SockService(SockRepository sockRepository, SockCountRepository sockCountRepository) {
        this.sockRepository = sockRepository;
        this.sockCountRepository = sockCountRepository;
    }

    public SockDto incomeSocks(SockDto sockDto) {
        checkParam(sockDto);
        Sock findSock = sockRepository.findSocksByColorAndCottonPart(sockDto.getColor(), sockDto.getCottonPart());
        if (findSock == null) {
            saveNewSock(sockDto);
        } else {
            increaseCountSock(sockDto, findSock);
        }
        return sockDto;
    }
    private void checkParam(SockDto sockDto) {
        boolean isWrongValueCottonPart = sockDto.getCottonPart() < 0 || sockDto.getCottonPart() > 100;
        boolean isWrongValueQuantity = sockDto.getQuantity() <= 0;
        if (isWrongValueCottonPart || isWrongValueQuantity) {
            throw new IllegalArgumentException();
        }
    }
    private void saveNewSock (SockDto sockDto) {
        Sock sock = SockMapper.INSTANCE.dtoToSock(sockDto);
        SockCount sockCount = SockMapper.INSTANCE.dtoToSockCount(sockDto);
        sockCount.setSock(sock);
        sockRepository.save(sock);
        sockCountRepository.save(sockCount);
    }

    private void increaseCountSock(SockDto sockDto, Sock findSock) {
        SockCount findSockCount = findSock.getSockCount();
        int currentCount = findSockCount.getCount();
        findSockCount.setCount(currentCount + sockDto.getQuantity());
        sockCountRepository.save(findSockCount);
    }

    public SockDto outcomeSocks(SockDto sockDto) {
        checkParam(sockDto);
        Sock findSock = sockRepository.findSocksByColorAndCottonPart(sockDto.getColor(), sockDto.getCottonPart());
        if (findSock == null) {
            throw new IllegalArgumentException("such socks is not found");
        } else {
            decreaseCountSock(sockDto, findSock);
        }
        return sockDto;
    }
    private void decreaseCountSock(SockDto sockDto, Sock findSock) {
        SockCount findSockCount = findSock.getSockCount();
        int currentCount = findSockCount.getCount();
        int requiredCount = sockDto.getQuantity();
        if (currentCount < requiredCount) {
            throw new IllegalArgumentException("such socks is not enough");
        } else {
            findSockCount.setCount(currentCount - requiredCount);
            sockCountRepository.save(findSockCount);
        }

    }
}
