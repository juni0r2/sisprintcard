package br.com.sisprintcard.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sisprintcard.common.JavaPrint;
import br.com.sisprintcard.common.PrinterStatusXML;
import br.com.sisprintcard.common.XPS_Java_SDK;
import br.com.sisprintcard.common.XPS_Java_SDK.XpsDriverInteropLib;
import br.com.sisprintcard.dto.DadosParaImpressaoDto;
import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.model.EnImpressora;
import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;
import br.com.sisprintcard.repository.ImpressoraRepository;
import br.com.sisprintcard.repository.SamBeneficiarioCartaoIdentifRepository;

@Service
public class ImprimeCardService {

    @Autowired
    ImpressoraRepository impressoraRepository;

    @Autowired
    SamBeneficiarioCartaoIdentifRepository samBeneficiarioCartaoIdentifRepository;

    public void imprimir(Long idUsuario, Long idImpressora) {

        try {
            Optional<EnImpressora> impressora = impressoraRepository.findById(idImpressora);
            Optional<EnSamBeneficiarioCartaoIdentif> usuario = getEnSamBeneficiarioCartaoIdentif(idUsuario);
            
            if (impressora.isPresent() && usuario.isPresent()) {
            	
            	System.out.println("\nDados da Impressora :: ");
            	System.out.println(impressora.get());
            	System.out.println("\nImprimir Dados do Beneficiário :: ");
            	
            	DadosParaImpressaoDto build = DadosParaImpressaoDto.builder()
            	.beneficiarioCartaoIdentif(usuario.get().getKTrilhaCarenciaCartao())
            	.impressora(impressora.get().getNome())
            	.build();
            	Imprimir(build);
            }
            	
            
        } catch (UsuarioNaoEncontradoException w) {
            w.printStackTrace();
        }
    }

    private Optional<EnSamBeneficiarioCartaoIdentif> getEnSamBeneficiarioCartaoIdentif(Long idUsuario) {
        Optional<EnSamBeneficiarioCartaoIdentif> usuario = samBeneficiarioCartaoIdentifRepository.findById(idUsuario);

        if (!usuario.isPresent())
            throw  new UsuarioNaoEncontradoException("Usuário não encontrado");
        return usuario;
    }
    
    public void Imprimir(DadosParaImpressaoDto dadosParaImpressaoDto) {
    	
    	String mPrinterName = dadosParaImpressaoDto.getImpressora();
    	
		final int S_OK = 0;
		byte returnXML[] = new byte[XPS_Java_SDK.BUFFSIZE];
		int sizeOfReturnXML[] = new int[1];
		sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;
		boolean bDisplayError = true;
		boolean bJobStarted = false;
		String returnValue;

		//Atualiza o status do registro atual para em 1-Imprimindo
//		resultado.setFicStatus(1);
//		fila.update(resultado);
		
		String dados[] = dadosParaImpressaoDto.getBeneficiarioCartaoIdentif().split(Pattern.quote(";"));  
		
		
		//Variáveis Auxiliáres para o Armazenamento dos Dados
		String matricula = dados[0].trim().replace("'", "");
		String nome = dados[3].trim().replace("'", "");
		String dataVencimento = dados[4].trim().replace("'", "");
		String dataNascimento = dados[5].trim().replace("'", "");
		String cpf = dados[7].trim().replace("'", "");
		String tipoPlano = dados[9].trim().replace("'", "");
		String municipio = dados[10].trim().replace("'", "");
		String tipoDependencia = dados[11].trim().replace("'", "");
		String orgao = dados[12].trim().replace("'", "");
		
		String carenciaL1 = "";
		String carenciaL2 = "";
		String carenciaL3 = "";
		
		int cont = 14;
		
		if (dados.length > 14){			
			for (int i = 14; i < dados.length - 1; i++){
				String txt = dados[i].trim().replace("'", "");
				cont = i;
				if (txt.length() > 1){
					
					if ((carenciaL1.length() + txt.length()) <= 55)
						carenciaL1 += txt + " ";
					else
						if ((carenciaL2.length() + txt.length()) <= 55)
							carenciaL2 += txt + " ";
						else
							carenciaL3 += txt + " ";
				}				
			}		
		}
		System.out.println(nome);
//		System.out.println(String.valueOf(cont) + ", "+ String.valueOf(dados.length));
		String versao = matricula.substring(matricula.length()-2, matricula.length());
		String matricOrgao = "";
		if(22 < dados.length )
			matricOrgao = dados[22].trim().replace("'", "");
		
//		System.out.println(versao + " " + matricOrgao);
		if (versao.length() > 2){
			//Atualiza o status do registro atual para imprimindo
//			resultado.setFicStatus(4);
//			fila.update(resultado);
			return;
		}
		
		PrinterStatusXML printerStatusXml = new PrinterStatusXML();

		System.setProperty("jna.library.path", "C:\\TEMP\\");	
		
		if (cpf.length() == 0)
			cpf = "0";	
			
		if (cpf.contains("OBRIGAT"))
			cpf="0";
		
		if (cpf.length() != 11)
			cpf = "0";	
		
		if (S_OK == XpsDriverInteropLib.INSTANCE.StartJob(mPrinterName, returnXML, sizeOfReturnXML)) {
			bJobStarted = true;	
			
			//Atualiza o status do registro atual para imprimindo
//			resultado.setFicStatus(S_OK);
//			resultado.setFicDataImpressao(new Date());
//			fila.update(resultado);
			
			// get PrintJobID
			printerStatusXml.Parse(returnXML, sizeOfReturnXML);

			String matriculaFormatada = matricula.replace("-", "").replace(".", "") + "=109=1=" + versao;
			sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;
								
			String nomeTarjaMagnetica = RemoverAcentos.remover(nome);
			System.out.println(nomeTarjaMagnetica + " - " + matriculaFormatada + " - " + cpf);
			
			if (S_OK == XpsDriverInteropLib.INSTANCE.MagstripeEncode(mPrinterName, nomeTarjaMagnetica, nomeTarjaMagnetica.length(), matriculaFormatada, matriculaFormatada.length(), cpf, cpf.length(), returnXML, sizeOfReturnXML)) {
				System.out.format("'%s' Magstripe Encode Succeed\n", mPrinterName);
				bDisplayError = false;

				// reset the buffer:
				sizeOfReturnXML[0] = XPS_Java_SDK.BUFFSIZE;
				if (S_OK == XpsDriverInteropLib.INSTANCE.MagstripeRead(mPrinterName, returnXML, sizeOfReturnXML)) {
					returnValue = PrinterStatusXML.cStringToJavaString(returnXML, sizeOfReturnXML[0]);
					System.out.format("'%s' MagStripe Read return length: %d\n\n%s\n\n", mPrinterName, sizeOfReturnXML[0], returnValue);
					bDisplayError = false;
				} else {
					// Magstripe Read has error
					bDisplayError = true;
				}
			}

			
		}

		// Any error needs to display
		if (bDisplayError) {
			//Atualiza o status do registro atual para em 1-Imprimindo
//			resultado.setFicStatus(1);
//			fila.update(resultado);
			
			printerStatusXml.Parse(returnXML, sizeOfReturnXML);

			// always cancel error condition
			//printerStatusXml.SetCommand(printerStatusXml.PRINTERACTION_CANCEL);

			returnValue = printerStatusXml.GetErrorMessage();
			System.out.format("\nMagstripe operation error. Printer return: %s\n Cancel Operation\n\n", returnValue);
			System.out.println("Erro ao imprimir cartão.");

			//XpsDriverInteropLib.INSTANCE.SendResponseToPrinter(mPrinterName, printerStatusXml.GetCommand(), printerStatusXml.GetPrintJobID(), printerStatusXml.GetErrorCode());
		} else {

					Integer modelo = 0;
					JavaPrint javaPrint = new JavaPrint(mPrinterName, matricula, nome, tipoDependencia, dataVencimento, versao, dataNascimento, municipio, orgao, tipoPlano, carenciaL1, carenciaL2, carenciaL3, modelo, matricOrgao);
//					javaPrint.Print();
					System.out.println("\nImprimindo cartão ...");
					//need to wait for data get spooler before calling EndJob
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
			}

		// this sample always cancel job when it has error
		// thus only call EndJob on succeed job
		if (bJobStarted && !bDisplayError) {
			System.out.println("EndJob called");
			XpsDriverInteropLib.INSTANCE.EndJob(mPrinterName);
		}
		
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
	}
}
