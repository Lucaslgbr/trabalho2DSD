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
    private Estrada matrix[][];

    private SimulacaoParametros simulacaoParametros;

    public MalhaTable(SimulacaoParametros simulacaoParametros) {
        this.simulacaoParametros = simulacaoParametros;
        this.criarMatriz();
    }

    public void criarMatriz() {
        Scanner meshScanner = null;
        try {
            //Cria uma instancia do arquivo pelo nome
            File arquivoMalha = new File(FILES_PATH + this.simulacaoParametros.getNomeArquivoMalha());
            meshScanner = new Scanner(arquivoMalha);
            //Enquanto tiver valores continua lendo
            while (meshScanner.hasNextInt()) {
                //Primeira linha é a quantidade de linhas
                this.setLines(meshScanner.nextInt());
                //Segunda linha é a quantidade de colunas
                this.setColumns(meshScanner.nextInt());
                //Cria uma matriz que representa a malha com os tamanhos fornecidos
                this.matrix = new Estrada[this.columns][this.lines];
                //Percorre cada uma das linhas do arquivo
                for (int linha = 0; linha < this.lines; linha++) {
                    for (int coluna = 0; coluna < this.columns; coluna++) {
                        //Cada valor inteiro do arquivo representa uma celula da malha e é
                        //representada por um valor que indica sua direção
                        int direcao = meshScanner.nextInt();
                        Estrada cell = new Estrada(coluna, linha, direcao);
                        if (cell.isEstrada()) {
                            cell.definirEntradaOuSaida(this);
                        }
                        this.matrix[coluna][linha] = cell;
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            meshScanner.close();
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
        return new ImageIcon(this.matrix[columnIndex][rowIndex].getIconeDiretorio());
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

    public Estrada[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Estrada[][] matrix) {
        this.matrix = matrix;
    }

    public SimulacaoParametros getSimulacaoParametros() {
        return simulacaoParametros;
    }

    public void setSimulacaoParametros(SimulacaoParametros simulacaoParametros) {
        this.simulacaoParametros = simulacaoParametros;
    }
}
