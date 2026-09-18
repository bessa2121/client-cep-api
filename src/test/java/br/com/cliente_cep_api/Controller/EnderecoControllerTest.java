package br.com.cliente_cep_api.Controller;

import br.com.cliente_cep_api.Dto.EnderecoUpdateRequest;
import br.com.cliente_cep_api.Exception.RecursoNaoEncontradoException;
import br.com.cliente_cep_api.Model.Endereco;
import br.com.cliente_cep_api.Service.Facade.EnderecoFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnderecoController.class)
class EnderecoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnderecoFacade facade;

    private Endereco criarEndereco() {
        Endereco e = new Endereco();
        e.setCep("01001000");
        e.setLogradouro("Praça da Sé");
        e.setBairro("Sé");
        e.setLocalidade("São Paulo");
        e.setUf("SP");
        e.setIbge("3550308");
        e.setDdd("11");
        return e;
    }

    @Test
    void criar_deveRetornar201ComLocation() throws Exception {
        when(facade.buscarNoViaCepESalvar("01001000")).thenReturn(criarEndereco());

        mockMvc.perform(post("/enderecos/01001000"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.cep").value("01001000"))
                .andExpect(jsonPath("$.localidade").value("São Paulo"));
    }

    @Test
    void buscar_existente_deveRetornar200() throws Exception {
        when(facade.buscarLocal("01001000")).thenReturn(criarEndereco());

        mockMvc.perform(get("/enderecos/01001000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01001000"));
    }

    @Test
    void buscar_inexistente_deveRetornar404ComCorpoDeErro() throws Exception {
        when(facade.buscarLocal("99999999"))
                .thenThrow(new RecursoNaoEncontradoException("Nenhum endereço cadastrado para o CEP 99999999"));

        mockMvc.perform(get("/enderecos/99999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void listar_deveRetornar200ComPagina() throws Exception {
        Page<Endereco> pagina = new PageImpl<>(List.of(criarEndereco()));
        when(facade.listarTodos(any())).thenReturn(pagina);

        mockMvc.perform(get("/enderecos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cep").value("01001000"));
    }

    @Test
    void atualizar_comDadosInvalidos_deveRetornar400() throws Exception {
        EnderecoUpdateRequest request = new EnderecoUpdateRequest();
        // logradouro, bairro, localidade e uf ficam em branco de propósito

        mockMvc.perform(put("/enderecos/01001000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void atualizar_comDadosValidos_deveRetornar200() throws Exception {
        EnderecoUpdateRequest request = new EnderecoUpdateRequest();
        request.setLogradouro("Praça da Sé");
        request.setBairro("Sé");
        request.setLocalidade("São Paulo");
        request.setUf("SP");

        when(facade.atualizar(anyString(), any())).thenReturn(criarEndereco());

        mockMvc.perform(put("/enderecos/01001000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01001000"));
    }

    @Test
    void deletar_deveRetornar204() throws Exception {
        mockMvc.perform(delete("/enderecos/01001000"))
                .andExpect(status().isNoContent());
    }
}
