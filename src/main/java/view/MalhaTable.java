package view;

import models.Estrada;
import models.SimulacaoParametros;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class MalhaTable extends AbstractTableModel {

    private static final String FILES_PATH =  Paths.get("").toAbsolutePath() +"/src/main/java/files/";

    private int lines;
    private int columns;
    private Estrada malha[][];

    private SimulacaoParametros simulacaoParametros;

    public MalhaTable(SimulacaoParametros simulacaoParametros) {
        this.simulacaoParametros = simulacaoParametros;
        this.criarMatriz();
    }

    public void criarMatriz() {
        Scanner scanner = null;
        try {
            //Cria uma instancia do arquivo pelo nome
            File arquivoMalha = new File(FILES_PATH + this.simulacaoParametros.getNomeArquivoMalha());
            scanner = new Scanner(arquivoMalha);
            //Enquanto tiver valores continua lendo
            while (scanner.hasNextInt()) {
                //Primeira linha é a quantidade de linhas
                this.setLines(scanner.nextInt());
                //Segunda linha é a quantidade de colunas
                this.setColumns(scanner.nextInt());
                //Cria uma matriz que representa a malha com os tamanhos fornecidos
                this.malha = new Estrada[this.columns][this.lines];
                //Percorre cada uma das linhas do arquivo
                for (int linha = 0; linha < this.lines; linha++) {
                    for (int coluna = 0; coluna < this.columns; coluna++) {
                        //Cada valor inteiro do arquivo representa uma celula da malha e é
                        //representada por um valor que indica sua direção
                        int direcao = scanner.nextInt();
                        Estrada estrada = new Estrada(coluna, linha, direcao);
                        if (estrada.isEstrada()) {
                            estrada.definirEntradaOuSaida(this);
                        }
                        this.malha[coluna][linha] = estrada;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    @Override
    public int getRowCount() {
        return this.getLines();
    }

    @Override
    public int getColumnCount() {
        return this.getColumns();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return new ImageIcon(this.malha[columnIndex][rowIndex].getIconeDiretorio());
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Estrada[][] getMalha() {
        return malha;
    }

    public void setMalha(Estrada[][] malha) {
        this.malha = malha;
    }

    public SimulacaoParametros getSimulacaoParametros() {
        return simulacaoParametros;
    }

    public void setSimulacaoParametros(SimulacaoParametros simulacaoParametros) {
        this.simulacaoParametros = simulacaoParametros;
    }
}
