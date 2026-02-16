package br.com.cliente_cep_api.Service.Facade;

import br.com.cliente_cep_api.Client.ViaCepClient;
import br.com.cliente_cep_api.Dto.EnderecoResponse;
import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Service.Strategy.EnderecoStrategy;
import org.springframework.stereotype.Service;

@Service
public class EnderecoFacade {

    private final ViaCepClient viaCepClient;
    private final EnderecoStrategy strategy;

    public EnderecoFacade(ViaCepClient viaCepClient,
                          EnderecoStrategy strategy) {
        this.viaCepClient = viaCepClient;
        this.strategy = strategy;
    }

    public Endereco buscarESalvar(String cep) {

        EnderecoResponse response = viaCepClient.buscarCep(cep);

        Endereco endereco = new Endereco();
        endereco.setCep(response.getCep());
        endereco.setLogradouro(response.getLogradouro());
        endereco.setComplemento(response.getComplemento());
        endereco.setBairro(response.getBairro());
        endereco.setLocalidade(response.getLocalidade());
        endereco.setUf(response.getUf());
        endereco.setIbge(response.getIbge());
        endereco.setDdd(response.getDdd());

        return strategy.salvar(endereco);
    }
}
