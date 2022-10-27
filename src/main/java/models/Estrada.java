package models;

import view.MalhaTable;

import java.nio.file.Paths;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Estrada {

    private static final String ICONS_PATH = Paths.get("").toAbsolutePath() + "/src/main/java/icons/";

    protected Semaphore semaphore;
    protected String iconeDiretorio;
    protected boolean entrada;
    protected boolean saida;
    protected int tipo;
    protected Carro carro;
    protected int linha;
    protected int coluna;


    public Estrada(int linha, int coluna, int tipo) {
        this.carro = null;
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
        this.semaphore = new Semaphore(1);
        this.defineIconeAtual();
    }

    public boolean tryAcquire() {
        boolean acquired = false;
        try {
            acquired = this.semaphore.tryAcquire(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return acquired;
    }

    public void release() {
        this.semaphore.release();
    }

    public void adicionarCarro(Carro carro) {
        this.carro = carro;
        this.defineIconeAtual();
    }

    public void removerCarro() {
        this.carro = null;
        this.defineIconeAtual();
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    public String getIconeDiretorio() {
        return iconeDiretorio;
    }

    public void setIconeDiretorio(String iconeDiretorio) {
        this.iconeDiretorio = iconeDiretorio;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isEntrada() {
        return entrada;
    }

    public void setEntrada(boolean isEntryCell) {
        this.entrada = isEntryCell;
    }

    public boolean isSaida() {
        return saida;
    }

    public void setSaida(boolean isExitCell) {
        this.saida = isExitCell;
    }

    public Carro getCarro() {
        return carro;
    }

    public boolean isEntradaSuperior() {
        return this.getColuna() - 1 < 0 && this.getTipo() == 3;
    }

    public boolean isSaidaSuperior() {
        return this.getColuna() - 1 < 0 && this.getTipo() == 1;
    }

    public boolean isEntradaInferior(MalhaTable malhaTable) {
        return this.getColuna() + 1 >= malhaTable.getLines() && this.getTipo() == 1;
    }

    public boolean isSaidaInferior(MalhaTable malhaTable) {
        return this.getColuna() + 1 >= malhaTable.getLines() && this.getTipo() == 3;
    }

    public boolean isEntradaEsquerda() {
        return this.getLinha() - 1 < 0 && this.getTipo() == 2;
    }

    public boolean isSaidaEsquerda() {
        return this.getLinha() - 1 < 0 && this.getTipo() == 4;
    }

    public boolean isEntradaDireita(MalhaTable malhaTable) {
        return this.getLinha() + 1 >= malhaTable.getColumns() && this.getTipo() == 4;
    }

    public boolean isSaidaDireita(MalhaTable malhaTable) {
        return this.getLinha() + 1 >= malhaTable.getColumns() && this.getTipo() == 2;
    }

    public boolean isCruzamento() {
        return this.tipo > 4;
    }

    public boolean isVazio() {
        return this.carro == null;
    }

    public boolean isEstrada() {
        return this.getTipo() > 0;
    }

    public void definirEntradaOuSaida(MalhaTable malhaTable) {
        this.setEntrada((this.isEntradaSuperior() || this.isEntradaInferior(malhaTable) || this.isEntradaEsquerda() || this.isEntradaDireita(malhaTable)));
        this.setSaida((this.isSaidaSuperior() || this.isSaidaInferior(malhaTable) || this.isSaidaEsquerda() || this.isSaidaDireita(malhaTable)));
    }

    /**
     * Define o icone correto dependendo do tipo ou se tem um carro na estrada
     */
    public void defineIconeAtual() {
        if (this.getCarro() != null) {
            this.setIconeDiretorioCarro();
        } else {
            this.setIconeDiretorioPorTipo();
        }
    }

    public void setIconeDiretorioPorTipo() {
        this.setIconeDiretorio(ICONS_PATH + "malha" + this.tipo + ".png");
    }

    public void setIconeDiretorioCarro() {
        this.setIconeDiretorio(ICONS_PATH + "veiculo" + this.carro.getTipo() + ".png");
    }

}
