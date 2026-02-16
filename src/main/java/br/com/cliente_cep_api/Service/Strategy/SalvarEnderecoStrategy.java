package br.com.cliente_cep_api.Service.Strategy;

import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Repository.EnderecoRepository;
import org.springframework.stereotype.Service;

@Service
public class SalvarEnderecoStrategy implements EnderecoStrategy {

    private final EnderecoRepository repository;

    public SalvarEnderecoStrategy(EnderecoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Endereco salvar(Endereco endereco) {
        return repository.save(endereco);
    }
}
