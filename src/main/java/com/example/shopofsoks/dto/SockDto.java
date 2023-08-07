package com.example.shopofsoks.dto;

import java.util.Objects;

public class SockDto {
    private String color;
    private int cottonPart;
    private int quantity;

    public SockDto(String color, int cottonPart, int quantity) {
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCottonPart() {
        return cottonPart;
    }

    public void setCottonPart(int cottonPart) {
        this.cottonPart = cottonPart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SockDto sockDto = (SockDto) o;

        if (cottonPart != sockDto.cottonPart) return false;
        if (quantity != sockDto.quantity) return false;
        return Objects.equals(color, sockDto.color);
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + cottonPart;
        result = 31 * result + quantity;
        return result;
    }
}
