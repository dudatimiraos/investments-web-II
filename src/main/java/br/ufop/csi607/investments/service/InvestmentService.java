package br.ufop.csi607.investments.service;

import br.ufop.csi607.investments.dto.InvestmentCreateDTO;
import br.ufop.csi607.investments.dto.InvestmentResponseDTO;
import br.ufop.csi607.investments.dto.PortfolioSummaryDTO;
import br.ufop.csi607.investments.model.Investment;
import br.ufop.csi607.investments.model.InvestmentType;
import br.ufop.csi607.investments.repository.InvestmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Camada de serviço que contém a lógica de negócio para o gerenciamento de investimentos.
 */
@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    /**
     * Cria um novo ativo na carteira.
     * @param createDTO DTO com os dados do novo ativo.
     * @return DTO com os dados do ativo criado.
     */
    public InvestmentResponseDTO createInvestment(InvestmentCreateDTO createDTO) {
        Investment newInvestment = new Investment(
                createDTO.type(),
                createDTO.symbol(),
                createDTO.quantity(),
                createDTO.purchasePrice(),
                createDTO.purchaseDate()
        );
        Investment savedInvestment = investmentRepository.save(newInvestment);
        return new InvestmentResponseDTO(savedInvestment);
    }

    /**
     * Lista todos os ativos da carteira, com filtro opcional por tipo.
     * @param type O tipo de ativo para filtrar (opcional).
     * @return Uma lista de DTOs dos ativos.
     */
    public List<InvestmentResponseDTO> getAllInvestments(Optional<String> type) {
        List<Investment> investments;
        if (type.isPresent()) {
            try {
                InvestmentType investmentType = InvestmentType.valueOf(type.get().toUpperCase());
                investments = investmentRepository.findByType(investmentType);
            } catch (IllegalArgumentException e) {
                // Se o tipo for inválido, retorna uma lista vazia ou lança uma exceção.
                // Aqui, optamos por retornar uma lista vazia.
                return List.of();
            }
        } else {
            investments = investmentRepository.findAll();
        }
        return investments.stream()
                .map(InvestmentResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza um ativo existente.
     * @param id O ID do ativo a ser atualizado.
     * @param createDTO DTO com os novos dados do ativo.
     * @return DTO com os dados do ativo atualizado.
     */
    public InvestmentResponseDTO updateInvestment(Long id, InvestmentCreateDTO createDTO) {
        Investment investment = investmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ativo com ID " + id + " não encontrado."));

        investment.setType(createDTO.type());
        investment.setSymbol(createDTO.symbol());
        investment.setQuantity(createDTO.quantity());
        investment.setPurchasePrice(createDTO.purchasePrice());
        investment.setPurchaseDate(createDTO.purchaseDate());

        Investment updatedInvestment = investmentRepository.save(investment);
        return new InvestmentResponseDTO(updatedInvestment);
    }

    /**
     * Remove um ativo da carteira.
     * @param id O ID do ativo a ser removido.
     */
    public void deleteInvestment(Long id) {
        if (!investmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Ativo com ID " + id + " não encontrado.");
        }
        investmentRepository.deleteById(id);
    }

    /**
     * Gera um resumo da carteira de investimentos.
     * @return DTO com o resumo da carteira.
     */
    public PortfolioSummaryDTO getPortfolioSummary() {
        List<Investment> allInvestments = investmentRepository.findAll();

        BigDecimal totalInvested = allInvestments.stream()
                .map(inv -> inv.getPurchasePrice().multiply(BigDecimal.valueOf(inv.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> totalByType = allInvestments.stream()
                .collect(Collectors.groupingBy(
                        inv -> inv.getType().name(),
                        Collectors.mapping(
                                inv -> inv.getPurchasePrice().multiply(BigDecimal.valueOf(inv.getQuantity())),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        long assetCount = allInvestments.size();

        return new PortfolioSummaryDTO(totalInvested, totalByType, assetCount);
    }
}
