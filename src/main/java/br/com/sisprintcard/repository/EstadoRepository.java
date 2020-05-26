package br.com.sisprintcard.repository;

import br.com.sisprintcard.model.EnEstado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<EnEstado, Long> {
}
