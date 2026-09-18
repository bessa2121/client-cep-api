package br.com.cliente_cep_api.Dto;

import br.com.cliente_cep_api.Model.Endereco;

import java.time.LocalDateTime;

public class EnderecoDTO {

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ibge;
    private String ddd;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static EnderecoDTO fromEntity(Endereco e) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.cep = e.getCep();
        dto.logradouro = e.getLogradouro();
        dto.complemento = e.getComplemento();
        dto.bairro = e.getBairro();
        dto.localidade = e.getLocalidade();
        dto.uf = e.getUf();
        dto.ibge = e.getIbge();
        dto.ddd = e.getDdd();
        dto.criadoEm = e.getCriadoEm();
        dto.atualizadoEm = e.getAtualizadoEm();
        return dto;
    }

    public String getCep() {
        return cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public String getUf() {
        return uf;
    }

    public String getIbge() {
        return ibge;
    }

    public String getDdd() {
        return ddd;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
}
