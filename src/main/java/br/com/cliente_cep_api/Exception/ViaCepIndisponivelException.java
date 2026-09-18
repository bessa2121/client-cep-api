package br.com.cliente_cep_api.Exception;

public class ViaCepIndisponivelException extends RuntimeException {
    public ViaCepIndisponivelException(String message, Throwable cause) {
        super(message, cause);
    }
}
