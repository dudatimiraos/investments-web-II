package br.ufop.csi607.investments.repository;

import br.ufop.csi607.investments.model.Investment;
import br.ufop.csi607.investments.model.InvestmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {
    List<Investment> findByType(InvestmentType type);
}