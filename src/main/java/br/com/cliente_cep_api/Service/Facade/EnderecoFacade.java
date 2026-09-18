package br.com.cliente_cep_api.Service.Facade;

import br.com.cliente_cep_api.Client.ViaCepClient;
import br.com.cliente_cep_api.Dto.EnderecoResponse;
import br.com.cliente_cep_api.Dto.EnderecoUpdateRequest;
import br.com.cliente_cep_api.Exception.CepInvalidoException;
import br.com.cliente_cep_api.Exception.EnderecoJaExisteException;
import br.com.cliente_cep_api.Exception.RecursoNaoEncontradoException;
import br.com.cliente_cep_api.Exception.ViaCepIndisponivelException;
import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Repository.EnderecoRepository;
import br.com.cliente_cep_api.Service.Strategy.EnderecoStrategy;
import br.com.cliente_cep_api.Util.CepUtils;
import feign.FeignException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Facade: concentra o fluxo entre Controller, Feign Client (ViaCEP), Strategy de
// Persistência e Repository, expondo operações simples para o Controller.
@Service
public class EnderecoFacade {

    private final ViaCepClient viaCepClient;
    private final EnderecoStrategy strategy;
    private final EnderecoRepository repository;

    public EnderecoFacade(ViaCepClient viaCepClient,
                           EnderecoStrategy strategy,
                           EnderecoRepository repository) {
        this.viaCepClient = viaCepClient;
        this.strategy = strategy;
        this.repository = repository;
    }

    // Consulta o ViaCEP e cria (persiste) o endereço localmente. Usado por POST.
    public Endereco buscarNoViaCepESalvar(String cepBruto) {
        String cep = CepUtils.normalizarEValidar(cepBruto);

        if (repository.existsById(cep)) {
            throw new EnderecoJaExisteException(
                    "Endereço para o CEP " + cep + " já está cadastrado. Use PUT para atualizar.");
        }

        Endereco endereco = converter(cep, consultarViaCep(cep));
        return strategy.salvar(endereco);
    }

    // Busca um endereço já persistido localmente. Usado por GET /{cep}. Operação segura/idempotente.
    public Endereco buscarLocal(String cepBruto) {
        String cep = CepUtils.normalizarEValidar(cepBruto);
        return repository.findById(cep)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhum endereço cadastrado para o CEP " + cep));
    }

    // Lista todos os endereços já persistidos, paginado. Usado por GET /enderecos.
    public Page<Endereco> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Atualiza manualmente os dados de um endereço existente. Usado por PUT /{cep}.
    public Endereco atualizar(String cepBruto, EnderecoUpdateRequest request) {
        String cep = CepUtils.normalizarEValidar(cepBruto);
        Endereco existente = repository.findById(cep)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhum endereço cadastrado para o CEP " + cep + ". Use POST para criar."));

        Endereco dadosNovos = new Endereco();
        dadosNovos.setLogradouro(request.getLogradouro());
        dadosNovos.setComplemento(request.getComplemento());
        dadosNovos.setBairro(request.getBairro());
        dadosNovos.setLocalidade(request.getLocalidade());
        dadosNovos.setUf(request.getUf());
        dadosNovos.setIbge(request.getIbge());
        dadosNovos.setDdd(request.getDdd());

        return strategy.atualizar(existente, dadosNovos);
    }

    // Remove um endereço persistido. Usado por DELETE /{cep}.
    public void deletar(String cepBruto) {
        String cep = CepUtils.normalizarEValidar(cepBruto);
        if (!repository.existsById(cep)) {
            throw new RecursoNaoEncontradoException("Nenhum endereço cadastrado para o CEP " + cep);
        }
        repository.deleteById(cep);
    }

    private EnderecoResponse consultarViaCep(String cep) {
        try {
            EnderecoResponse response = viaCepClient.buscarCep(cep);
            if (response == null || Boolean.TRUE.equals(response.getErro())) {
                throw new RecursoNaoEncontradoException("CEP " + cep + " não encontrado no ViaCEP");
            }
            return response;
        } catch (FeignException.BadRequest ex) {
            throw new CepInvalidoException("CEP em formato inválido para o ViaCEP: " + cep);
        } catch (FeignException ex) {
            throw new ViaCepIndisponivelException("Falha ao consultar o ViaCEP", ex);
        }
    }

    private Endereco converter(String cep, EnderecoResponse response) {
        Endereco endereco = new Endereco();
        endereco.setCep(cep);
        endereco.setLogradouro(response.getLogradouro());
        endereco.setComplemento(response.getComplemento());
        endereco.setBairro(response.getBairro());
        endereco.setLocalidade(response.getLocalidade());
        endereco.setUf(response.getUf());
        endereco.setIbge(response.getIbge());
        endereco.setDdd(response.getDdd());
        return endereco;
    }
}
