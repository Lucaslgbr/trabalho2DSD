package models;

public class SimulacaoParametros {

    private final int quantidadeCarros;
    private final int intervaloInsercao;
    private final String nomeArquivoMalha;
    private final int quantidadeCarrosMalha;

    public SimulacaoParametros(int quantidadeCarros, int intervaloInsercao, String nomeArquivoMalha, int quantidadeCarrosMalha) {
        this.quantidadeCarros = quantidadeCarros;
        this.intervaloInsercao = intervaloInsercao;
        this.nomeArquivoMalha = nomeArquivoMalha;
        this.quantidadeCarrosMalha = quantidadeCarrosMalha;
    }

    public int getQuantidadeCarros() {
        return quantidadeCarros;
    }

    public int getIntervaloInsercao() {
        return intervaloInsercao;
    }

    public String getNomeArquivoMalha() {
        return nomeArquivoMalha;
    }

    public int getQuantidadeCarrosMalha() {
        return quantidadeCarrosMalha;
    }

}
