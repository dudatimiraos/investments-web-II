package br.ufop.csi607.investments.controller;

import br.ufop.csi607.investments.dto.InvestmentCreateDTO;
import br.ufop.csi607.investments.dto.InvestmentResponseDTO;
import br.ufop.csi607.investments.dto.PortfolioSummaryDTO;
import br.ufop.csi607.investments.service.InvestmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Controller REST para gerenciar os endpoints da API de investimentos.
 */
@RestController
@RequestMapping("/investments")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @PostMapping
    public ResponseEntity<InvestmentResponseDTO> createInvestment(@RequestBody InvestmentCreateDTO createDTO) {
        InvestmentResponseDTO createdInvestment = investmentService.createInvestment(createDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdInvestment.id())
                .toUri();
        return ResponseEntity.created(location).body(createdInvestment);
    }

    @GetMapping
    public ResponseEntity<List<InvestmentResponseDTO>> getAllInvestments(@RequestParam(required = false) Optional<String> type) {
        List<InvestmentResponseDTO> investments = investmentService.getAllInvestments(type);
        return ResponseEntity.ok(investments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestmentResponseDTO> updateInvestment(@PathVariable Long id, @RequestBody InvestmentCreateDTO createDTO) {
        try {
            InvestmentResponseDTO updatedInvestment = investmentService.updateInvestment(id, createDTO);
            return ResponseEntity.ok(updatedInvestment);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) {
        try {
            investmentService.deleteInvestment(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<PortfolioSummaryDTO> getPortfolioSummary() {
        PortfolioSummaryDTO summary = investmentService.getPortfolioSummary();
        return ResponseEntity.ok(summary);
    }
}
