package com.example.shopofsoks.controller;

import com.example.shopofsoks.Operat;
import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/socks")
public class SockController {
    private final SockService sockService;

    public SockController(SockService sockService) {
        this.sockService = sockService;
    }

    @Operation(summary = "Сделать приход носков на склад",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "*/*",
                            schema = @Schema(implementation = SockDto.class))),
            @ApiResponse(responseCode = "400", description = "badRequest"),
            @ApiResponse(responseCode = "500", description = "error on server")})
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/income")
    public ResponseEntity<SockDto> incomeSocks(@RequestBody SockDto sockDto) {

        SockDto result = null;
        try {
            result = sockService.incomeSocks(sockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Сделать отпуск носков со склада",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "*/*",
                            schema = @Schema(implementation = SockDto.class))),
            @ApiResponse(responseCode = "400", description = "badRequest"),
            @ApiResponse(responseCode = "500", description = "error on server")})
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/outcome")
    public ResponseEntity<SockDto> outcomeSocks(@RequestBody SockDto sockDto) {

        SockDto result = null;
        try {
            result = sockService.outcomeSocks(sockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Получить общее количество носков на складе, соответствующее введенным параметрам",
            description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "*/*",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "badRequest"),
            @ApiResponse(responseCode = "500", description = "error on server")})
    @GetMapping
    public ResponseEntity<String> getCountSocksbyParam(@RequestParam(name = "color") String color,
                                                        @RequestParam(name = "operation") String operation,
                                                        @RequestParam(name = "cottonPart") Integer cottonPart) {

        Integer result = null;
        try {
            Operat operat = Operat.valueOf(operation);
            result = sockService.getCountSocksbyParam(color, operat, cottonPart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result.toString());
    }
}
