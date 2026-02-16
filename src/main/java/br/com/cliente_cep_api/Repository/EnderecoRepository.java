package br.com.cliente_cep_api.Repository;

import br.com.cliente_cep_api.Model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
