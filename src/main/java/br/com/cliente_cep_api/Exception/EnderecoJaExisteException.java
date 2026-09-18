package br.com.cliente_cep_api.Exception;

public class EnderecoJaExisteException extends RuntimeException {
    public EnderecoJaExisteException(String message) {
        super(message);
    }
}
