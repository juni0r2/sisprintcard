package br.com.sisprintcard.repository;

import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SamBeneficiarioCartaoIdentifRepository extends JpaRepository<EnSamBeneficiarioCartaoIdentif, Long> {
}
