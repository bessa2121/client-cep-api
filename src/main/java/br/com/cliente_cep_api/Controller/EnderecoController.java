package br.com.cliente_cep_api.Controller;

import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Service.Facade.EnderecoFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoFacade facade;

    public EnderecoController(EnderecoFacade facade) {
        this.facade = facade;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<Endereco> buscar(@PathVariable String cep) {
        return ResponseEntity.ok(facade.buscarESalvar(cep));
    }
}
