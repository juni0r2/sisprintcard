package br.com.sisprintcard;

import br.com.sisprintcard.model.EnEstado;
import br.com.sisprintcard.repository.EstadoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Optional;

@SpringBootApplication
public class SisPrintCardApplication {

    static EstadoRepository estadoRepository;

    private static ConfigurableApplicationContext run;

    public static void main(String[] args) {
        run = SpringApplication.run(SisPrintCardApplication.class, args);
        estadoRepository = (EstadoRepository) run.getBean("estadoRepository");

        Long id = Long.parseLong("1");
        if (id != null) {
            Optional<EnEstado> byId = estadoRepository.findById(id);
            System.out.println(byId.get());
        }
        run.close();
    }
}
