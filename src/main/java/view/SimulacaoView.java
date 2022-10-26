package view;

import controller.SimulacaoController;
import models.SimulacaoParametros;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SimulacaoView extends JFrame {

    //Componentes visuais
    private JPanel containerPanel;
    private JTable malhaTable;
    private JButton encerrarButton;
    private JTextField carrosNaFilaField;
    private JTextField carrosNaMalha;
    //Demais parametros
    private SimulacaoParametros simulacaoParametros;
    private final SimulacaoController simulacaoController;

    public SimulacaoView(SimulacaoParametros simulacaoParametros) {
        //Simulação Parametros
        this.simulacaoParametros = simulacaoParametros;
        //Ajustes componentes visuais
        this.formatarView();
        //Criação tabela da malha de acordo com a malha selecionada (Componente que mostra as estradas)
        this.renderizaMalhaTable();
        //Após tudo criado exibe a tela pronta
        super.setVisible(true);
        this.simulacaoController = new SimulacaoController(simulacaoParametros, this);
        this.simulacaoController.start();
    }

    private void formatarView() {
        super.setExtendedState(JFrame.MAXIMIZED_BOTH);
        super.setUndecorated(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setContentPane(this.containerPanel);
        encerrarButton.addActionListener((ActionEvent e) -> {
            //Encerra a thread
            this.simulacaoController.encerrar();
            //Cria a tela de menu
            new MenuView();
            //Fecha a tela atual de simulacao
            super.dispose();
        });
    }

    private void renderizaMalhaTable() {
        //Cria a tabela com base nos parametros recebisods
        malhaTable.setModel(new MalhaTable(this.simulacaoParametros));
        //Tamanho de cada linha de acordo com o tamanho da imagem
        malhaTable.setRowHeight(25);
        //Frescuras de layout
        malhaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        malhaTable.setIntercellSpacing(new Dimension(0, 0));
        //Define o que é renderizado dentro de cada célula da tabela (A imagem que representa a estrada)
        malhaTable.setDefaultRenderer(Object.class, new MalhaCelulaRenderer());
        //Define o tamanho de cada coluna de acordo com o tamanho da imagem
        TableColumnModel columnModel = malhaTable.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setMaxWidth(25);
        }
    }

    public JTable getMalhaTable() {
        return malhaTable;
    }

    public JTextField getCarrosNaFilaField() {
        return carrosNaFilaField;
    }

    public JTextField getCarrosNaMalha() {
        return carrosNaMalha;
    }

    public JButton getEncerrarButton() {
        return encerrarButton;
    }

}
