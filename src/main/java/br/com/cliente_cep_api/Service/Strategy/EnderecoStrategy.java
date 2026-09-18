package br.com.cliente_cep_api.Service.Strategy;

import br.com.cliente_cep_api.Model.Endereco;

 // Define o contrato de persistência de um Endereco, permitindo trocar a forma
 // Como ele é salvo/atualizado sem alterar o Facade (padrão Strategy).

public interface EnderecoStrategy {
    Endereco salvar(Endereco endereco);
    Endereco atualizar(Endereco existente, Endereco dadosNovos);
}
