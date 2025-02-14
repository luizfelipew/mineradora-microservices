package org.br.mineradora.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "opportunity")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class OpportunityEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Date date;

    @Column(name = "proposal_id")
    private Long proposalId;

    private String customer;

    @Column(name = "price_tonne")
    private BigDecimal priceTonne;

    @Column(name = "last_currency_quotation")
    private BigDecimal lastDollarQuotation;
}
