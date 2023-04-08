package com.example.shopofsoks.controller;

import com.example.shopofsoks.Operat;
import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.model.Sock;
import com.example.shopofsoks.model.SockCount;
import com.example.shopofsoks.repository.SockCountRepository;
import com.example.shopofsoks.repository.SockRepository;
import com.example.shopofsoks.service.SockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = SockController.class)
class SockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private SockService sockService;

    @MockBean
    private SockRepository sockRepository;
    @MockBean
    private SockCountRepository sockCountRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private SockController sockController;

    Sock sock;
    Sock sock2;
    SockCount sockCount;
    SockDto sockDto;
    SockDto newSockDtoAfterIncome;
    SockDto newSockDtoAfterOutcome;
    SockDto wrongSockDto;
    int id = 1;
    String color = "Black";
    int cottonPart = 10;
    int wrongCottonPart = 110;
    int quantity = 50;

    @BeforeEach
    public void setUp() {

        sock = new Sock(id, color, cottonPart);
        sock2 = new Sock(id, color, cottonPart);
        sockCount = new SockCount(1, quantity);
        sock.setSockCount(sockCount);
        sock2.setSockCount(sockCount);
        sockDto = new SockDto(color, cottonPart, quantity);
        newSockDtoAfterIncome = new SockDto(color, cottonPart, 2 * quantity);
        newSockDtoAfterOutcome = new SockDto(color, cottonPart, 0);
        wrongSockDto = new SockDto(color, wrongCottonPart, quantity);
    }

    @Test
    public void incomeSockPositiveTestWhenSockIsFound() throws Exception{

        String jsonSockDto = objectMapper.writeValueAsString(sockDto);

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(sock);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.cottonPart").value(cottonPart))
                .andExpect(jsonPath("$.quantity").value(quantity*2));
    }
    @Test
    public void incomeSockPositiveTestWhenSockIsNotFound() throws Exception{

        String jsonSockDto = objectMapper.writeValueAsString(sockDto);

        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.cottonPart").value(cottonPart))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }
    @Test
    public void incomeSockNegativeTestWrongParam() throws Exception{

        String jsonSockDto = objectMapper.writeValueAsString(wrongSockDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // тесты для метода outcomeSock
    @Test
    public void outcomeSockNegativeTestWrongParam() throws Exception {
        String jsonSockDto = objectMapper.writeValueAsString(wrongSockDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void outcomeSockNegativeTestSocksIsNull() throws Exception {
        String jsonSockDto = objectMapper.writeValueAsString(sockDto);
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void outcomeSockNegativeTestSocksIsNotEnough() throws Exception {
        String jsonSockDto = objectMapper.writeValueAsString(newSockDtoAfterIncome);
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(sock);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void outcomeSockPositiveTest() throws Exception {
        String jsonSockDto = objectMapper.writeValueAsString(sockDto);
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(sock);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(jsonSockDto)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.cottonPart").value(cottonPart))
                .andExpect(jsonPath("$.quantity").value(newSockDtoAfterOutcome.getQuantity()));;
    }

    // Тесты для метода получения количества носков на складе
    @Test
    public void getCountSocksbyParam() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.equal.toString(), wrongCottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void getCountSocksNegativeTestWhenEmptyList() throws Exception {
        Mockito.when(sockRepository.findSocksByColorAndCottonPartAfter(anyString(), anyInt())).thenReturn(Collections.emptyList());
        Mockito.when(sockRepository.findSocksByColorAndCottonPartBefore(anyString(), anyInt())).thenReturn(Collections.emptyList());
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.equal.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.lessThan.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.moreThan.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getCountSocksPositiveTest() throws Exception {
        List<Sock> sockList = new ArrayList<Sock>();
        sockList.add(sock);
        sockList.add(sock2);
        Mockito.when(sockRepository.findSocksByColorAndCottonPartAfter(anyString(), anyInt())).thenReturn(sockList);
        Mockito.when(sockRepository.findSocksByColorAndCottonPartBefore(anyString(), anyInt())).thenReturn(sockList);
        Mockito.when(sockRepository.findSocksByColorAndCottonPart(anyString(), anyInt())).thenReturn(sock);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.equal.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(quantity));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.lessThan.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(quantity*2));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks?color={color}&operation={oper}&cottonPart={cottonPart}",
                                color, Operat.moreThan.toString(), cottonPart)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(quantity*2));
    }

}