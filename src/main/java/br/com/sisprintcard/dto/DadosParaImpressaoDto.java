package br.com.sisprintcard.dto;

import br.com.sisprintcard.model.EnImpressora;
import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;
import lombok.Data;

@Data
public class DadosParaImpressaoDto {
	
	private EnImpressora impressora;
	private EnSamBeneficiarioCartaoIdentif beneficiarioCartaoIdentif;
	
}
