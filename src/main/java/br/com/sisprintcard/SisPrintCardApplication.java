package br.com.sisprintcard;

import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.model.EnEstado;
import br.com.sisprintcard.model.EnImpressora;
import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;
import br.com.sisprintcard.repository.EstadoRepository;
import br.com.sisprintcard.repository.ImpressoraRepository;
import br.com.sisprintcard.repository.SamBeneficiarioCartaoIdentifRepository;
import br.com.sisprintcard.service.ImprimeCardService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Optional;

@SpringBootApplication
public class SisPrintCardApplication {

    private static ImprimeCardService imprimeCardService;

    private static ConfigurableApplicationContext run;

    public static void main(String[] args) {
        run = SpringApplication.run(SisPrintCardApplication.class, args);

        try {
            Long[] atributo = recebeAtributos(args);
            imprimeCardService = (ImprimeCardService) run.getBean("imprimeCardService");
            imprimeCardService.imprimir(atributo[0], atributo[1]);
        } catch (UsuarioNaoEncontradoException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("Usuario ou Impressora não informado.");
        } finally {
            run.close();
        }
    }

    private static Long[] recebeAtributos(String[] args) {
        String idUsuario = args[0];
        String idImpressora = args[1];

        if (StringUtils.isEmpty(idUsuario))
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");

        if (StringUtils.isEmpty(idImpressora))
            throw new UsuarioNaoEncontradoException("Impressora não encontrada");

        Long[] variaveis = {Long.parseLong(idUsuario), Long.parseLong(idImpressora)};
        return variaveis;
    }
}
