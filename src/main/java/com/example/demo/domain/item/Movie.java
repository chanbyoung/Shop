package com.example.demo.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import javax.sql.rowset.serial.SerialStruct;

@Entity
@DiscriminatorValue("M")
@Getter
public class Movie extends Item{
    private String director;
    private String actor;
}
