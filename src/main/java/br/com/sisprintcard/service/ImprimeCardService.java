package br.com.sisprintcard.service;

import br.com.sisprintcard.exception.UsuarioNaoEncontradoException;
import br.com.sisprintcard.model.EnImpressora;
import br.com.sisprintcard.model.EnSamBeneficiarioCartaoIdentif;
import br.com.sisprintcard.repository.ImpressoraRepository;
import br.com.sisprintcard.repository.SamBeneficiarioCartaoIdentifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

            System.out.println(impressora);
            System.out.println(usuario);

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
}
