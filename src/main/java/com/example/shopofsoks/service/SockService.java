package com.example.shopofsoks.service;

import com.example.shopofsoks.Operat;
import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.mapper.SockMapper;
import com.example.shopofsoks.model.Sock;
import com.example.shopofsoks.model.SockCount;
import com.example.shopofsoks.repository.SockCountRepository;
import com.example.shopofsoks.repository.SockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Класс осуществляет логику работы склада носков <br>
 * - Приход новых носков<br>
 * - Отпуск носков со склада<br>
 * - получения количества носков по параметрам
 */
@Service
public class SockService {
    private final SockRepository sockRepository;
    private final SockCountRepository sockCountRepository;


    public SockService(SockRepository sockRepository, SockCountRepository sockCountRepository) {
        this.sockRepository = sockRepository;
        this.sockCountRepository = sockCountRepository;
    }

    /**
     * Метод осуществляет приход носков на склад
     * @param sockDto
     * @return
     */
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

    /**
     * Метод проверяет переданные параметры на корректность <br>
     * Если некорректные, то выбрасывает исключение
     * @param sockDto переданные параметры
     * @Throw IllegalArgumentException
     */
    private void checkParam(SockDto sockDto) {
        boolean isWrongValueCottonPart = sockDto.getCottonPart() < 0 || sockDto.getCottonPart() > 100;
        boolean isWrongValueQuantity = sockDto.getQuantity() <= 0;
        if (isWrongValueCottonPart || isWrongValueQuantity) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Метод создает новую группу носков и сохраняет в БД
     * @param sockDto
     */
    private void saveNewSock (SockDto sockDto) {
        Sock sock = SockMapper.INSTANCE.dtoToSock(sockDto);
        SockCount sockCount = SockMapper.INSTANCE.dtoToSockCount(sockDto);
        sockCount.setSock(sock);
        sockRepository.save(sock);
        sockCountRepository.save(sockCount);
    }

    /**
     * Метод увеличивает количество носков на складе
     * @param sockDto переданные параметры
     * @param findSock найденны группа носков, количество которых нужно увеличить
     */
    private void increaseCountSock(SockDto sockDto, Sock findSock) {
        SockCount findSockCount = findSock.getSockCount();
        int currentCount = findSockCount.getCount();
        findSockCount.setCount(currentCount + sockDto.getQuantity());
        sockCountRepository.save(findSockCount);
    }

    /**
     * Метод выполняет отпуск носков со склада
     * @param sockDto - переданные параметры
     * @return
     */
    public SockDto outcomeSocks(SockDto sockDto) {
        checkParam(sockDto);
        Sock findSock = sockRepository.findSocksByColorAndCottonPart(sockDto.getColor(), sockDto.getCottonPart());
        checkSockByNull(findSock);
        decreaseCountSock(sockDto, findSock);
        return sockDto;
    }

    /**
     * Метод уменьшает количество носков на складе <br>
     * Если носков меньше чем требуется, то выбрасывается исключение
     * @param sockDto переданные параметры
     * @param findSock найденная группа носков, кол-во которых нужно уменьшить
     * @Throw IllegalArgumentException
     */
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

    /**
     * Метод находит общее количество носков по параметрам.<br>
     * Находит носки, в которых содержание хлопка меньше, больше или равно заданному параметру<br>
     * Если параметры некорректные или носки по параметрам не найдены, то бросается исключение
     * @param color
     * @param operation (moreThan, lessThan, equal)
     * @param cottonPart
     * @throw IllegalArgumentException
     * @return
     */
    public Integer getCountSocksbyParam(String color, Operat operation, Integer cottonPart) {
        boolean isWrongValueCottonPart = cottonPart < 0 || cottonPart > 100;
        if (isWrongValueCottonPart) {
            throw new IllegalArgumentException("wrone value cottonPart");
        }
        switch (operation) {

            case moreThan:
                List<Sock> listSocks = sockRepository.findSocksByColorAndCottonPartAfter(color, cottonPart);
                checkListSocksByNullOrEmpty(listSocks);
                return getCountSocks(listSocks);
            case lessThan:
                listSocks = sockRepository.findSocksByColorAndCottonPartBefore(color, cottonPart);
                checkListSocksByNullOrEmpty(listSocks);
                return getCountSocks(listSocks);
            case equal:
                Sock findSock = sockRepository.findSocksByColorAndCottonPart(color, cottonPart);
                checkSockByNull(findSock);
                return findSock.getSockCount().getCount();
        }
        throw  new IllegalArgumentException("Sock is not found");
    }

    /**
     * Метод проверяет лист
     * на null
     * не являетсяли он пустым (empty)
     *
     * @param listSocks
     * @Throw IllegalArgumentException - если лист null или пустой
     */
    private void checkListSocksByNullOrEmpty(List<Sock> listSocks) {
        if (listSocks == null || listSocks.isEmpty()) {
            throw new IllegalArgumentException("Socks - is not Found");
        }
    }

    /**
     * Метод проверяет не является ли объект findSock null
     * Если null, то выбрасывается исключение
     * @param findSock
     * @throw IllegalArgumentException если проверяемый объект null
     */
    private void checkSockByNull(Sock findSock) {
        if (findSock == null) {
            throw new IllegalArgumentException("Sock is not found");
        }
    }

    /**
     * Метод считает общее количество носков в листе
     * @param listSocks, не null
     * @return
     */
    private Integer getCountSocks(List<Sock> listSocks) {
        return listSocks.stream()
                .map(Sock::getSockCount)
                .mapToInt(SockCount::getCount)
                .sum();
    }
}
