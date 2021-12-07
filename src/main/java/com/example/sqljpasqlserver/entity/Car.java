package com.example.sqljpasqlserver.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_car")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "findByYearProcedure",
                procedureName = "FIND_CAR_BY_YEAR",
                resultClasses={Car.class}

        )
}

)
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    String model;

    @Column
    private Integer year;


}
