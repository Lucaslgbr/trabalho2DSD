package models;

import controller.SimulacaoController;

import java.util.ArrayList;
import java.util.Random;

public class Carro extends Thread {

    private final SimulacaoController simulacaoController;
    private final ArrayList<Estrada> percurso;
    private final Estrada[][] malhaPista;
    private final Random random = new Random();
    private final int velocidade;
    private Estrada estradaAtual;

    public Carro(SimulacaoController simulacaoController) {
        this.simulacaoController = simulacaoController;
        this.percurso = new ArrayList<>();
        this.malhaPista = simulacaoController.getMalhaPista();
        //Define uma velocidade aleatoria
        this.velocidade = random.nextInt(100) + 200;
        this.estradaAtual = null;
    }

    @Override
    public void run() {
        while (!percurso.isEmpty()) {
            int proximaExtradaIndex = 0;
            if (percurso.get(proximaExtradaIndex).isCruzamento()) {
                //Se for um cruzamento precisamos saber para que lado ir
                resolverCruzamento();
            } else {
                //Se for sóu ma estrada apenas move o veiculo
                Estrada estrada = this.percurso.remove(proximaExtradaIndex);
                this.mover(estrada, true);
            }
        }
        //Chegou ao fim do percurso?
        // - Remove o carro
        this.getEstradaAtual().removerCarro();
        // - Libera a estrada
        this.getEstradaAtual().release();
        // - Tira da malha pois saiu da tela
        this.simulacaoController.removeCarroNaMalha(this);
        // - Atualização gráfica
        this.simulacaoController.atualizarCelula(this.getEstradaAtual());
        // - Fim da thread
        this.interrupt();
    }

    private void resolverCruzamento() {
        this.delay();
        ArrayList<Estrada> cruzamentosReservar = new ArrayList<>();
        ArrayList<Estrada> cruzamentosReservados = new ArrayList<>();
        for (int i = 0; i < this.percurso.size(); i++) {
            Estrada estrada = this.percurso.get(i);
            cruzamentosReservar.add(estrada);
            if (!estrada.isCruzamento()) {
                break;
            }
        }
        for (Estrada crossroadToAcquire : cruzamentosReservar) {
            if (crossroadToAcquire.tryAcquire()) {
                cruzamentosReservados.add(crossroadToAcquire);
            } else {
                //Não conseguiu reserver todos os cruzamentos para passar?
                for (Estrada acquiredEstrada : cruzamentosReservados) {
                    //Libera todos
                    acquiredEstrada.release();
                }
                break;
            }
        }
        //Tem todos os cruzamentos para passar?
        if (cruzamentosReservados.size() == cruzamentosReservar.size()) {
            //Move pelo cruzamento
            for (Estrada acquiredCrossroad : cruzamentosReservados) {
                this.percurso.remove(acquiredCrossroad);
                this.mover(acquiredCrossroad, false);
            }
        }
    }

    private void mover(Estrada proximaEstrada, Boolean needsAcquire) {
        if (proximaEstrada.isVazio()) {
            boolean acquired = false;
            if (needsAcquire) {
                do {
                    //Tenta "reservar/adquirir" a estrada
                    if (proximaEstrada.tryAcquire()) {
                        acquired = true;
                    }
                } while (!acquired);
            }
            //Somente quando conseguiu a estrada, adiciona o carro na posição
            proximaEstrada.adicionarCarro(this);
            Estrada estradaAnterior = this.getEstradaAtual();
            if (estradaAnterior != null) {
                //Tira o carro da estrada que ele estava
                estradaAnterior.removerCarro();
                //Libera a estrada
                estradaAnterior.release();
            }
            //Diz em qual estrada o carro está agora
            this.setEstradaAtual(proximaEstrada);
            //Atualização gráfica
            this.simulacaoController.atualizarCelula(proximaEstrada);
            this.delay();
        }
    }

    public void definirPercurso(Estrada entrada) throws Exception {
        boolean saidaEncontrada = false;
        Estrada proximaEstrada = entrada;
        percurso.add(proximaEstrada);
        //Controla os cruzamentos encontrados para não ocorrer de andar em circulos
        int cruzamentosEncontrados = 0;
        while (!saidaEncontrada) {
            int direcao = proximaEstrada.getTipo();
            boolean isRoad = direcao <= 4;
            if (isRoad) {
                proximaEstrada = this.escolherEstradaPorDirecao(direcao, proximaEstrada.getLinha(), proximaEstrada.getColuna());
            } else {
                proximaEstrada = this.escolherCruzamentoPorDirecao(direcao, proximaEstrada.getLinha(), proximaEstrada.getColuna(), cruzamentosEncontrados);
                if (proximaEstrada.isCruzamento()) {
                    cruzamentosEncontrados++;
                } else {
                    cruzamentosEncontrados = 0;
                }
            }
            percurso.add(proximaEstrada);
            saidaEncontrada = proximaEstrada.isSaida();
        }
    }

    public Estrada escolherEstradaPorDirecao(int direcao, int linhaAtual, int colunaAtual) throws Exception {
        switch (direcao) {
            case 1:
                return this.malhaPista[linhaAtual][colunaAtual - 1];
            case 2:
                return this.malhaPista[linhaAtual + 1][colunaAtual];
            case 3:
                return this.malhaPista[linhaAtual][colunaAtual + 1];
            case 4:
                return this.malhaPista[linhaAtual - 1][colunaAtual];
            default:
                throw new Exception("Erro na montagem da malha");
        }
    }

    private Estrada escolherCruzamentoPorDirecao(int direcao, int linhaAtual, int colunaAtual, int cruzamentosEncontrados) throws Exception {
        int lado = random.nextInt(2);
        switch (direcao) {
            case 5: {
                return this.malhaPista[linhaAtual][colunaAtual - 1];
            }
            case 6: {
                return this.malhaPista[linhaAtual + 1][colunaAtual];
            }
            case 7: {
                return this.malhaPista[linhaAtual][colunaAtual + 1];
            }
            case 8: {
                return this.malhaPista[linhaAtual - 1][colunaAtual];
            }
            case 9: {
                if (cruzamentosEncontrados == 3) {
                    return this.malhaPista[linhaAtual + 1][colunaAtual];
                } else {
                    if (lado == 0) {
                        return this.malhaPista[linhaAtual][colunaAtual - 1];
                    } else {
                        return this.malhaPista[linhaAtual + 1][colunaAtual];
                    }
                }
            }
            case 10: {
                if (cruzamentosEncontrados == 3) {
                    return this.malhaPista[linhaAtual][colunaAtual - 1];
                } else {
                    if (lado == 0) {
                        return this.malhaPista[linhaAtual][colunaAtual - 1];
                    } else {
                        return this.malhaPista[linhaAtual - 1][colunaAtual];
                    }
                }
            }
            case 11: {
                if (cruzamentosEncontrados == 3) {
                    return this.malhaPista[linhaAtual][colunaAtual + 1];
                } else {
                    if (lado == 0) {
                        return this.malhaPista[linhaAtual + 1][colunaAtual];
                    } else {
                        return this.malhaPista[linhaAtual][colunaAtual + 1];
                    }
                }
            }
            case 12: {
                if (cruzamentosEncontrados == 3) {
                    return this.malhaPista[linhaAtual - 1][colunaAtual];
                } else {
                    if (lado == 0) {
                        return this.malhaPista[linhaAtual][colunaAtual + 1];
                    } else {
                        return this.malhaPista[linhaAtual - 1][colunaAtual];
                    }
                }
            }
            default:
                throw new Exception("Erro na montagem da malha");
        }
    }

    public Estrada getEstradaAtual() {
        return estradaAtual;
    }

    public void setEstradaAtual(Estrada currentEstrada) {
        this.estradaAtual = currentEstrada;
    }

    public void delay() {
        try {
            //Tempo de espera a cada movimento para definir a velocidade de cada carro
            Thread.sleep(this.velocidade);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}