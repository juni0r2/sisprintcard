package br.com.sisprintcard.repository;

import br.com.sisprintcard.model.EnImpressora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpressoraRepository extends JpaRepository<EnImpressora, Long> {
}
