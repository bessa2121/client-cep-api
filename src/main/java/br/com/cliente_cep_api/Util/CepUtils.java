package br.com.cliente_cep_api.Util;

import br.com.cliente_cep_api.Exception.CepInvalidoException;

import java.util.regex.Pattern;

public final class CepUtils {

    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    private CepUtils() {
    }

     // Normaliza (remove hífen) e valida o formato do CEP.
     // Lança CepInvalidoException (-> 400) em vez de deixar o erro estourar mais adiante.
    public static String normalizarEValidar(String cep) {
        if (cep == null || !CEP_PATTERN.matcher(cep).matches()) {
            throw new CepInvalidoException(
                    "CEP inválido: '" + cep + "'. Formato esperado: 00000000 ou 00000-000");
        }
        return cep.replace("-", "");
    }
}
