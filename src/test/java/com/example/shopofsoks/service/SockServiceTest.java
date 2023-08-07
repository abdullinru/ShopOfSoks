package com.example.shopofsoks.service;

import com.example.shopofsoks.controller.OperationType;
import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.model.Sock;
import com.example.shopofsoks.model.SockCount;
import com.example.shopofsoks.repository.SockCountRepository;
import com.example.shopofsoks.repository.SockRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class SockServiceTest {
    @Mock
    private SockRepository sockRepository;
    @Mock
    private SockCountRepository sockCountRepository;

    @InjectMocks
    private SockService sockService;

    Sock sock;
    Sock sock2;
    SockCount sockCount;
    SockDto sockDto;
    SockDto newSockDtoAfterIncome;
    SockDto newSockDtoAfterOutcome;
    SockDto wrongSockDto;

    @BeforeEach
    public void setUp(){
        int id = 1;
        String color = "Black";
        int cottonPart = 10;
        int wrongCottonPart = 110;
        int quantity = 50;
        sock = new Sock(id,color, cottonPart);
        sock2 = new Sock(id,color, cottonPart);
        sockCount = new SockCount(1, quantity);
        sock.setSockCount(sockCount);
        sock2.setSockCount(sockCount);
        sockDto = new SockDto(color, cottonPart, quantity);
        newSockDtoAfterIncome = new SockDto(color, cottonPart, 2*quantity);
        newSockDtoAfterOutcome = new SockDto(color, cottonPart, 0);
        wrongSockDto = new SockDto(color, wrongCottonPart, quantity);

    }
// Тесты метода по приходу носков на склад
    @Test
    public void incomeSockPositiveTestWhenFindSockNotNull() {

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(any(String.class), anyInt()))
                .thenReturn(sock);
        Assertions.assertThat(sockService.incomeSocks(sockDto)).isEqualTo(newSockDtoAfterIncome);
    }
    @Test
    public void incomeSockPositiveTestWhenFindSockNull() {

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(any(String.class), anyInt()))
                .thenReturn(null);
        Assertions.assertThat(sockService.incomeSocks(sockDto)).isEqualTo(sockDto);
    }
    @Test
    public void incomeSockNegativeTestWhenWrongParam() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.incomeSocks(wrongSockDto));
    }
    // Тесты метода по отпуску носков со склада
    @Test
    public void outcomeSockPositiveTestWhenFindSockNotNull() {

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(any(String.class), anyInt()))
                .thenReturn(sock);
        Assertions.assertThat(sockService.outcomeSocks(sockDto)).isEqualTo(newSockDtoAfterOutcome);
    }
    @Test
    public void outcomeSockNegativeTestWhenFindSockNull() {

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(any(String.class), anyInt()))
                .thenReturn(null);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.outcomeSocks(sockDto));
    }
    @Test
    public void outcomeSockNegativeTestWhenNotEnoughSocks() {

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(any(String.class), anyInt()))
                .thenReturn(sock);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.outcomeSocks(newSockDtoAfterIncome));
    }
    @Test
    public void outcomeSockNegativeTestWhenWrongParam() {

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.outcomeSocks(wrongSockDto));
    }

    // Тесты метода по получению количества носков на складе по определенным параметрам
    @Test
    public void getCountTestWrongParam() {
        String color = "Black";
        int wrongCottonPart = 110;
        OperationType ravno = OperationType.equal;
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.getCountSocksbyParam(color, ravno, wrongCottonPart ));
    }

    @Test
    public void getCountOperMoreNegativeTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType more = OperationType.moreThan;
        Mockito.when(sockRepository.findSocksByColorAndCottonPartAfter(color, cottonPart))
                .thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.getCountSocksbyParam(color, more, cottonPart ));
    }
    @Test
    public void getCountOperLessNegativeTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType less = OperationType.lessThan;
        Mockito.when(sockRepository.findSocksByColorAndCottonPartBefore(color, cottonPart))
                .thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.getCountSocksbyParam(color, less, cottonPart ));
    }
    @Test
    public void getCountOperEqualNegativeTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType equal = OperationType.equal;
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(color, cottonPart))
                .thenReturn(null);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> sockService.getCountSocksbyParam(color, equal, cottonPart ));
    }

    @Test
    public void getCountOperEqualPositiveTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType equal = OperationType.equal;
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(color, cottonPart))
                .thenReturn(sock);
        Assertions.assertThat(sockService.getCountSocksbyParam(color, equal, cottonPart)).isEqualTo(sock.getSockCount().getCount());
    }
    @Test
    public void getCountOperMorePositiveTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType more = OperationType.moreThan;
        List<Sock> sockList = new ArrayList<>();
        sockList.add(sock);
        sockList.add(sock2);
        Mockito.when(sockRepository.findSocksByColorAndCottonPartAfter(color, cottonPart))
                .thenReturn(sockList);
        Assertions.assertThat(sockService.getCountSocksbyParam(color, more, cottonPart)).isEqualTo(2*sock.getSockCount().getCount());
    }
    @Test
    public void getCountOperLessPositiveTest() {
        String color = "Black";
        int cottonPart = 10;
        OperationType less = OperationType.lessThan;
        List<Sock> sockList = new ArrayList<>();
        sockList.add(sock);
        sockList.add(sock2);
        Mockito.when(sockRepository.findSocksByColorAndCottonPartBefore(color, cottonPart))
                .thenReturn(sockList);
        Assertions.assertThat(sockService.getCountSocksbyParam(color, less, cottonPart)).isEqualTo(2*sock.getSockCount().getCount());
    }

}