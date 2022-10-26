package view;

import models.SimulacaoParametros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuView extends JFrame {

    private JPanel containerPanel;
    private JTextField intervaloInsercaoField;
    private JTextField quantidadeCarrosField;
    private JTextField quantidadeMaximaCarrosField;
    private JButton simularButton;
    private JRadioButton malha1RadioButton;
    private JRadioButton malha2RadioButton;
    private JRadioButton malha3RadioButton;

    public MenuView() {
        super("Bem-vindo(a) ao Simulador de Tráfego em Malha Viária");
        super.setSize(new Dimension(800, 400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.containerPanel);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        simularButton.addActionListener((ActionEvent e) -> {
            //Cria a tela de simulação
            new SimulacaoView(new SimulacaoParametros(
                    Integer.parseInt(quantidadeCarrosField.getText()),
                    Integer.parseInt(intervaloInsercaoField.getText()),
                    this.getMalhaSelecionada(),
                    Integer.parseInt(this.quantidadeMaximaCarrosField.getText())
            ));
            //Fecha a tela atual do menu
            super.dispose();
        });
        super.setVisible(true);
    }

    /**
     * @return Retorna o nome do arquivo da malha de acordo com o selecionado no menu
     */
    public String getMalhaSelecionada() {
        if (this.malha1RadioButton.isSelected()) {
            return "malha1.txt";
        }
        if (this.malha2RadioButton.isSelected()) {
            return "malha2.txt";
        }
        if (this.malha3RadioButton.isSelected()) {
            return "malha3.txt";
        }
        return null;
    }

}
