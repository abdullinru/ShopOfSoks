package com.example.shopofsoks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "count")
public class SockCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int count;

    @OneToOne
    @JoinColumn(name = "sock_id")
    private Sock sock;

    public SockCount() {

    }

    public SockCount(int id, int count) {
        this.id = id;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Sock getSock() {
        return sock;
    }

    public void setSock(Sock sock) {
        this.sock = sock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SockCount sockCount = (SockCount) o;

        return id == sockCount.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
