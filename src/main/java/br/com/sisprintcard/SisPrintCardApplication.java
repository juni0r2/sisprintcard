package br.com.sisprintcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import br.com.sisprintcard.common.XPS_Java_SDK;
import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.service.ImprimeCardService;

@SpringBootApplication
public class SisPrintCardApplication {

    private static ImprimeCardService imprimeCardService;

    private static ConfigurableApplicationContext run;

    static {
        try {
            String myLibraryPath = System.getProperty("user.dir");//or another absolute or relative path
            System.setProperty("java.library.path", myLibraryPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new XPS_Java_SDK();

        System.out.println("Carregou DLL");
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
        System.exit(0);
    }

    private static Long[] recebeAtributos(String[] args) {
        String idUsuario = "288258";
        String idImpressora = "241";

        if (StringUtils.isEmpty(idUsuario))
            throw new UsuarioNaoEncontradoException("Usuário não encontrado");

        if (StringUtils.isEmpty(idImpressora))
            throw new UsuarioNaoEncontradoException("Impressora não encontrada");

        Long[] variaveis = {Long.parseLong(idUsuario), Long.parseLong(idImpressora)};
        return variaveis;
    }
}
