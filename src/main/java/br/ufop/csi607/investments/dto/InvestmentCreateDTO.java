package br.ufop.csi607.investments.dto;

import br.ufop.csi607.investments.model.InvestmentType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentCreateDTO(
        InvestmentType type,
        String symbol,
        Integer quantity,
        BigDecimal purchasePrice,
        LocalDate purchaseDate
) {}
