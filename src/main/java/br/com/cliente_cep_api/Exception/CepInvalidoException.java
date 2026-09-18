package br.com.cliente_cep_api.Exception;

public class CepInvalidoException extends RuntimeException {
    public CepInvalidoException(String message) {
        super(message);
    }
}
