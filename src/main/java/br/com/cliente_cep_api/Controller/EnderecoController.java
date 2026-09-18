package br.com.cliente_cep_api.Controller;

import br.com.cliente_cep_api.Dto.EnderecoDTO;
import br.com.cliente_cep_api.Dto.EnderecoUpdateRequest;
import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Service.Facade.EnderecoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "Busca, cadastro e gerenciamento de endereços via CEP (ViaCEP)")
public class EnderecoController {

    private final EnderecoFacade facade;

    public EnderecoController(EnderecoFacade facade) {
        this.facade = facade;
    }

    @Operation(summary = "Consulta o ViaCEP e cadastra o endereço localmente")
    @PostMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> criar(@PathVariable String cep,
                                              UriComponentsBuilder uriBuilder) {
        Endereco endereco = facade.buscarNoViaCepESalvar(cep);
        var location = uriBuilder.path("/enderecos/{cep}").buildAndExpand(endereco.getCep()).toUri();
        return ResponseEntity.created(location).body(EnderecoDTO.fromEntity(endereco));
    }

    @Operation(summary = "Busca um endereço já cadastrado pelo CEP")
    @GetMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> buscar(@PathVariable String cep) {
        Endereco endereco = facade.buscarLocal(cep);
        return ResponseEntity.ok(EnderecoDTO.fromEntity(endereco));
    }

    @Operation(summary = "Lista todos os endereços já cadastrados, paginado")
    @GetMapping
    public ResponseEntity<Page<EnderecoDTO>> listar(
            @PageableDefault(size = 20, sort = "cep") Pageable pageable) {
        Page<EnderecoDTO> pagina = facade.listarTodos(pageable).map(EnderecoDTO::fromEntity);
        return ResponseEntity.ok(pagina);
    }

    @Operation(summary = "Atualiza manualmente os dados de um endereço já cadastrado")
    @PutMapping("/{cep}")
    public ResponseEntity<EnderecoDTO> atualizar(@PathVariable String cep,
                                                  @Valid @RequestBody EnderecoUpdateRequest request) {
        Endereco endereco = facade.atualizar(cep, request);
        return ResponseEntity.ok(EnderecoDTO.fromEntity(endereco));
    }

    @Operation(summary = "Remove um endereço cadastrado")
    @DeleteMapping("/{cep}")
    public ResponseEntity<Void> deletar(@PathVariable String cep) {
        facade.deletar(cep);
        return ResponseEntity.noContent().build();
    }
}
