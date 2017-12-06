package jogo.cliente;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import jogo.Personagem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Tela {

	private final JFrame frameJogada = new JFrame("Par ou Ímpar da morte");
	private final JLabel lblSuaJogada = new JLabel("Sua Jogada");
	private final JLabel lblJogadaAdversrio = new JLabel("Jogada advers\u00E1rio");
	private final JTextField tfSuaJogada = new JTextField();
	private final JTextField tfJogadaAdversario = new JTextField();
	private final JLabel lbSeuPersonagem = new JLabel("Seu personagem");
	private final JLabel lbPersonagemAdversario = new JLabel("Personagem advers\u00E1rio");
	private final JTextField tfPersonagemAdversario = new JTextField();
	private final JTextField tfEstado = new JTextField();
	private final JButton btJogar = new JButton("Jogar");
	private final JComboBox cbEscolhaPersonagem = new JComboBox();
	private boolean jogadaRealizada = false;

	/**
	 * Create the application.
	 */
	public Tela() {
		initialize();
	}

	public void setVisible(boolean b) {
		this.frameJogada.setVisible(b);
	}

	public void mostrarMensagemInfo(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	public void mostrarMensagemErro(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Erro", JOptionPane.ERROR_MESSAGE);
	}
	
	public void fechar() {
		this.frameJogada.dispose();
	}
	
	public void setTextoEstado(String txt) {
		this.tfEstado.setText(txt);
	}
	
	public Personagem getPersonagemEscolhido() {
		
		//Quando nenhum indice está selecionado o método getSelectedIndex() retorna -1
		//Quando estiver selecionado o valor " " (Espaço em branco) o método getSelectedIndex() retorna 2
		
		int index = this.cbEscolhaPersonagem.getSelectedIndex();
		if(index < 0 || index > 1)
			return null;
		
		return Personagem.values()[index];
	}
	
	public void limparSelecaoPersonagem() {
		//Mudando pra deixar escolhido o valor " " (Espaço em branco) 
		
		this.cbEscolhaPersonagem.setSelectedIndex(2);
	}
	
	public void configurarCamposInicioJogo() {
		
		this.cbEscolhaPersonagem.setSelectedIndex(2);
		this.cbEscolhaPersonagem.setEnabled(true);
		
		this.tfJogadaAdversario.setText("");

		this.jogadaRealizada = false;
		this.tfSuaJogada.setText("");
		this.tfSuaJogada.setEditable(false);
		
		this.tfPersonagemAdversario.setText("");
		this.tfEstado.setText("");
	}
	
	public void habilitaEscolhaPersonagem(boolean b) {
		this.cbEscolhaPersonagem.setEnabled(b);
	}
	
	public void habilitaEscolhaJogada(boolean b) {
		this.tfSuaJogada.setEnabled(b);
		this.tfSuaJogada.setEditable(b);
	}
	
	public void limparEscolhaJogada() {
		this.jogadaRealizada = false;
		this.tfSuaJogada.setText("");
		this.habilitaEscolhaJogada(true);
	}
	
	public void setJogadaAdversario(int jogada) {
		this.tfJogadaAdversario.setText(""+jogada);
	}
	
	public boolean getJogadaRealizada() {
		return this.jogadaRealizada;
	}
	
	public String getValorJogada() {
		return this.tfSuaJogada.getText();
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		tfEstado.setEditable(false);
		tfEstado.setBounds(0, 241, 434, 20);
		tfEstado.setColumns(10);
		tfPersonagemAdversario.setEditable(false);
		tfPersonagemAdversario.setEnabled(false);
		tfPersonagemAdversario.setBounds(219, 59, 105, 20);
		tfPersonagemAdversario.setColumns(10);
		tfSuaJogada.setEnabled(false);
		tfSuaJogada.setEditable(false);
		tfSuaJogada.setBounds(53, 129, 105, 20);
		tfSuaJogada.setColumns(10);
		
		frameJogada.getContentPane().setEnabled(false);
		frameJogada.setBounds(100, 100, 450, 300);
		frameJogada.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameJogada.getContentPane().setLayout(null);
		lblSuaJogada.setBounds(53, 104, 105, 14);

		frameJogada.getContentPane().add(lblSuaJogada);
		lblJogadaAdversrio.setBounds(219, 104, 105, 14);

		frameJogada.getContentPane().add(lblJogadaAdversrio);

		frameJogada.getContentPane().add(tfSuaJogada);
		tfJogadaAdversario.setEditable(false);
		tfJogadaAdversario.setEnabled(false);
		tfJogadaAdversario.setColumns(10);
		tfJogadaAdversario.setBounds(219, 129, 105, 20);

		frameJogada.getContentPane().add(tfJogadaAdversario);
		lbSeuPersonagem.setBounds(53, 40, 105, 14);

		frameJogada.getContentPane().add(lbSeuPersonagem);
		lbPersonagemAdversario.setEnabled(false);
		lbPersonagemAdversario.setBounds(219, 40, 126, 14);

		frameJogada.getContentPane().add(lbPersonagemAdversario);

		frameJogada.getContentPane().add(tfPersonagemAdversario);

		frameJogada.getContentPane().add(tfEstado);
		btJogar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jogadaRealizada = true;
				tfSuaJogada.setEditable(false);
			}
		});
		btJogar.setBounds(53, 185, 105, 23);

		frameJogada.getContentPane().add(btJogar);
		cbEscolhaPersonagem.setEnabled(false);
		cbEscolhaPersonagem.setModel(new DefaultComboBoxModel(new String[] {"PAR", "\u00CDMPAR", " "}));
		cbEscolhaPersonagem.setSelectedIndex(2);
		cbEscolhaPersonagem.setBounds(53, 59, 105, 20);
		
		frameJogada.getContentPane().add(cbEscolhaPersonagem);
	}
}
