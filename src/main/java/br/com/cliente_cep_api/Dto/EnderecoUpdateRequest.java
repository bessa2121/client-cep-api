package br.com.cliente_cep_api.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnderecoUpdateRequest {

    @NotBlank(message = "logradouro é obrigatório")
    private String logradouro;

    private String complemento;

    @NotBlank(message = "bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "localidade é obrigatória")
    private String localidade;

    @NotBlank(message = "uf é obrigatória")
    @Size(min = 2, max = 2, message = "uf deve ter 2 caracteres")
    @Pattern(regexp = "[A-Za-z]{2}", message = "uf deve conter apenas letras")
    private String uf;

    private String ibge;

    private String ddd;

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
