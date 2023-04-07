package com.example.shopofsoks.model;
import jakarta.persistence.*;

@Entity
@Table(name = "socks")
public class Sock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String color;
    private int cottonPart;

    @OneToOne(mappedBy = "sock")
    private SockCount sockCount;

    public Sock() {

    }

    public Sock(int id, String color, int cottonPart) {
        this.id = id;
        this.color = color;
        this.cottonPart = cottonPart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
//        if (!super.equals(object)) return false;

        Sock sock = (Sock) object;

        if (cottonPart != sock.cottonPart) return false;
        return java.util.Objects.equals(color, sock.color);
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + cottonPart;
        return result;
    }
}
