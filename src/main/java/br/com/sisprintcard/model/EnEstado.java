package br.com.sisprintcard.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "ESTADOS",schema = "BENNERP")
public class EnEstado implements Serializable {

    @Id
    @Column(name = "HANDLE")
    private Long id;

    @Column(name = "Z_GRUPO")
    private Integer grupoZ;

    @Column(name = "MOSTRARNOPORTAL")
    private String mostraPortal;

    @Column(name = "PAIS")
    private Integer pais;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "GENTILICO")
    private String gentilico;

    @Column(name = "SIGLA")
    private String sigla;

    @Column(name = "IDINTEGRACAOCORPBENNER")
    private Double idIntegracaoBenner;

    @Column(name = "CODIGOIBGE")
    private Integer codigoIbge;

    @Column(name = "MASCARAINSCRICAOESTADUAL")
    private String mascaraInscricao;

}
