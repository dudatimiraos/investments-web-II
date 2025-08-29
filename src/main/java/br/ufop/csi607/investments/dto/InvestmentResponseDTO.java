package br.ufop.csi607.investments.dto;

import br.ufop.csi607.investments.model.Investment;
import br.ufop.csi607.investments.model.InvestmentType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestmentResponseDTO(
        Long id,
        InvestmentType type,
        String symbol,
        Integer quantity,
        BigDecimal purchasePrice,
        LocalDate purchaseDate,
        BigDecimal totalValue
) {
    public InvestmentResponseDTO(Investment investment) {
        this(
                investment.getId(),
                investment.getType(),
                investment.getSymbol(),
                investment.getQuantity(),
                investment.getPurchasePrice(),
                investment.getPurchaseDate(),
                investment.getPurchasePrice().multiply(BigDecimal.valueOf(investment.getQuantity()))
        );
    }
}
