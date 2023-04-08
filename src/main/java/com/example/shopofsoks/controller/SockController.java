package com.example.shopofsoks.controller;

import com.example.shopofsoks.dto.SockDto;
import com.example.shopofsoks.service.SockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping ("/api/socks")
public class SockController {
    private final SockService sockService;

    public SockController(SockService sockService) {
        this.sockService = sockService;
    }

//    @Operation(summary = "Получить все объявления",
//            description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "OK",
//                    content = @Content(mediaType = "*/*",
//                            schema = @Schema(implementation = ResponseWrapperAds.class)))})

//    @GetMapping
//    public ResponseEntity<ResponseWrapperAds> getAllAds() {
//        log.info("Was invoked getAllAds method from {}", AdsController.class.getSimpleName());
//        return ResponseEntity.ok(adsService.getAllAds());
//    }

//    @Operation(summary = "Создать новое объявление",
//            description = "")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Created",
//                    content = @Content(mediaType = "*/*",
//                            schema = @Schema(implementation = AdsDto.class))),
//            @ApiResponse(responseCode = "401", description = "Unauthorized"),
//            @ApiResponse(responseCode = "403", description = "Forbidden"),
//            @ApiResponse(responseCode = "404", description = "Not Found")})


    @PostMapping("/income")
    public ResponseEntity<SockDto> incomeSocks(@RequestBody SockDto sockDto) {
//        log.info("Was invoked addAds method from {}", AdsController.class.getSimpleName());
        SockDto result = null;
        try {
            result = sockService.incomeSocks(sockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/outcome")
    public ResponseEntity<SockDto> outcomeSocks(@RequestBody SockDto sockDto) {
//        log.info("Was invoked addAds method from {}", AdsController.class.getSimpleName());
        SockDto result = null;
        try {
            result = sockService.outcomeSocks(sockDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
