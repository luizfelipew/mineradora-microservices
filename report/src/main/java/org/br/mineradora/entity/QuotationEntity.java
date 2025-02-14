package org.br.mineradora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "quotation")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuotationEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @Column(name = "currency_price")
    private BigDecimal currencyPrice;

}
