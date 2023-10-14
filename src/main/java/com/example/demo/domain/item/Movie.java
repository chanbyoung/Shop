package com.example.demo.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.sql.rowset.serial.SerialStruct;

@Entity
@DiscriminatorValue("M")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Movie extends Item{
    private String director;
    private String actor;
}
