package controller;

import models.Carro;
import models.Estrada;
import models.SimulacaoParametros;
import view.MalhaTable;
import view.SimulacaoView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class SimulacaoController extends Thread {

    private final LinkedList<Carro> carrosEmFila;
    private final ArrayList<Carro> carrosNaMalha;
    private final SimulacaoParametros simulacaoParametros;
    private final SimulacaoView simulacaoView;

    public SimulacaoController(SimulacaoParametros simulacaoParametros, SimulacaoView simulacaoView) {
        this.simulacaoParametros = simulacaoParametros;
        this.simulacaoView = simulacaoView;
        this.carrosNaMalha = new ArrayList<>();
        this.carrosEmFila = this.loadCarros();
    }

    @Override
    public void run() {
        this.executaFila();
    }

    private void executaFila() {
        //Enquanto tiver carro para ser adicionado
        while (!this.carrosEmFila.isEmpty()) {
            //Percorre todas as linhas e colunas e em cada uma que for entrada que ainda não tenha um carro adiciona um
            for (int linhas = 0; linhas < this.getSimulacaoView().getMalhaTable().getRowCount(); linhas++) {
                for (int colunas = 0; colunas < this.getSimulacaoView().getMalhaTable().getColumnCount(); colunas++) {
                    Estrada entrada = this.getMalhaPista()[colunas][linhas];
                    //Se a pista for uma entrada, não tiver nenhum carro, ainda tiver carros na fila e tiver menos carros na malha do que o máximo definido
                    if (entrada.isEntrada() && entrada.isVazio() && !this.carrosEmFila.isEmpty() && this.carrosNaMalha.size() < this.getSimulacaoParametros().getQuantidadeCarrosMalha()) {
                        try {
                            //Tira o carro da fila
                            Carro carro = this.carrosEmFila.remove();
                            //Define o ponto de partida
                            carro.definirPercurso(entrada);
                            //Adiciona ele na malha
                            this.addCarroNaMalha(carro);
                            //Manda ele rodar
                            carro.start();
                            this.sleepProximoCarro();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    private void sleepProximoCarro() throws InterruptedException {
        int tempoSleep = 10;
        if (this.getSimulacaoParametros().getIntervaloInsercao() > 0) {
            tempoSleep = this.getSimulacaoParametros().getIntervaloInsercao() * 1000;
        }
        sleep(tempoSleep);
    }

    /**
     * Atualiza a tela para que seja visual a movimentação do carro
     *
     * @param estrada Estrada que foi atualizada
     */
    public void atualizarCelula(Estrada estrada) {
        this.getSimulacaoView().getCarrosNaFilaField().setText("Carros a serem adicionados: " + this.carrosEmFila.size());
        this.getSimulacaoView().getCarrosNaMalha().setText("Carros rodando: " + this.carrosNaMalha.size());
        this.getMalhaTable().fireTableCellUpdated(estrada.getLinha(), estrada.getColuna());
        this.getMalhaTable().fireTableDataChanged();
    }

    public Estrada[][] getMalhaPista() {
        return this.getMalhaTable().getMatrix();
    }

    public MalhaTable getMalhaTable() {
        return (MalhaTable) this.getSimulacaoView().getMalhaTable().getModel();
    }

    /**
     * @return A lista total de carros que deverá ser adicionado na simulação
     */
    public LinkedList<Carro> loadCarros() {
        LinkedList<Carro> carros = new LinkedList<>();
        for (int i = 0; i < this.getSimulacaoParametros().getQuantidadeCarros(); i++) {
            carros.add(new Carro(this));
        }
        return carros;
    }

    public ArrayList<Carro> getCarrosNaMalha() {
        return carrosNaMalha;
    }

    public LinkedList<Carro> getCarrosEmFila() {
        return carrosEmFila;
    }

    public void addCarroNaMalha(Carro carro) {
        this.carrosNaMalha.add(carro);
    }

    public void removeCarroNaMalha(Carro carro) {
        this.getCarrosNaMalha().remove(carro);
        if (this.getCarrosNaMalha().isEmpty() && this.getCarrosEmFila().isEmpty()) {
            this.getSimulacaoView().getEncerrarButton().setText("Reiniciar");
            JOptionPane.showMessageDialog(this.getSimulacaoView(), "Todos os carros percorreram a malha, simulação finalizada.");
        }
    }

    public SimulacaoParametros getSimulacaoParametros() {
        return simulacaoParametros;
    }

    public SimulacaoView getSimulacaoView() {
        return simulacaoView;
    }

}
