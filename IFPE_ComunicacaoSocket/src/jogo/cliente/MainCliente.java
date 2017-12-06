package jogo.cliente;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.Socket;

import jogo.Conexao;
import jogo.EstadoJogo;
import jogo.Personagem;
import jogo.mensagem.FabricMensagem;
import jogo.mensagem.Mensagem;
import jogo.mensagem.TipoErro;
import threaded.Config;

public class MainCliente {

	public static void main(String[] args) throws IOException {

		final Tela tela = new Tela();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					tela.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});


		try {

			EstadoJogo estado = EstadoJogo.CLIENTE_CONECTANDO_AO_SERVIDOR;
			Conexao conexao = null;
			Personagem personagemEscolhido = null;

			while(true) {

				/**
				 * M�quina de estados
				 */
				switch (estado) {
				case CLIENTE_CONECTANDO_AO_SERVIDOR:
				{

					tela.configurarCamposInicioJogo();
					tela.setTextoEstado("Conectando ao servidor...");

					conexao = new Conexao(new Socket(Config.HOST, Config.PORT));

					System.out.println("C#: Jogador conectado. IP:porta={" + conexao.getSocket().getInetAddress().getHostAddress() + ":" + conexao.getSocket().getPort()+ "}");

					estado = EstadoJogo.CLIENTE_ESCOLHENDO_PERSONAGENS;

					tela.setTextoEstado("Conectado!");
					Thread.sleep(300);

					break;
				}
				case CLIENTE_ESCOLHENDO_PERSONAGENS:
				{
					/**
					 * Habilitar a tela para escolher os poss�veis personagens.
					 */

					tela.setTextoEstado("Escolha seu personagem");
					tela.habilitaEscolhaPersonagem(true);
					tela.habilitaEscolhaJogada(false);

					//Neste caso como � um jogo de par ou impar, os poss�veis personagens s�o PAR e �MPAR

					while(true) {
						personagemEscolhido = tela.getPersonagemEscolhido();

						if(personagemEscolhido == null)
							Thread.sleep(100);
						else
							break;
					}

					while(true) {

						conexao.enviarMensagem(FabricMensagem.criaMensagemEscolhaPersonagem(personagemEscolhido));

						Mensagem msg = conexao.lerMensagem(1000 /*espera m�xima em milisegundos*/);

						if(msg != null) {

							if(msg.isMensagemEscolhaPersonagem() && (msg.getCampoPersonagem() == personagemEscolhido)) {

								estado = EstadoJogo.CLIENTE_ESCOLHENDO_JOGADA;

								break; //sai do while(true)

							} else if(msg.isMensagemErro() && (msg.getCampoTipoErro() == TipoErro.PERSONAGEM_JA_ESCOLHIDO)){
								tela.mostrarMensagemErro("Personagem j� foi escolhido por outro jogador!");
								tela.limparSelecaoPersonagem();

								break; //sai do while(true). Vai escolher o personagem novamente.

							} else {
								conexao.limparEntrada();
								//								System.out.println("Aconteceu algum erro. msg="+msg+", ESTADO="+estado);
							}

						} else {
							Thread.sleep(100);
						}

					}

					break;
				}
				case CLIENTE_ESCOLHENDO_JOGADA:
				{
					/**
					 * Habilita a tela para que o jogador fa�a sua jogada
					 */ 
					tela.setTextoEstado("Sua vez! Jogue!");
					tela.habilitaEscolhaPersonagem(false);
					tela.habilitaEscolhaJogada(true);

					//Neste caso como � um jogo de par ou �mpar, ent�o o jogador escolhe um n�mero inteiro n�o negativo.

					int valorJogada = -1;

					while(true) {
						while(!tela.getJogadaRealizada()) {
							Thread.sleep(100);
						}
						
						try {
							valorJogada = Integer.parseInt(tela.getValorJogada());	
						}catch (Exception e) {
							tela.mostrarMensagemErro("Jogada inv�lida");
							tela.limparEscolhaJogada();
						}
						
						if(valorJogada != -1)
							break;
					}

					while(true) {

						conexao.enviarMensagem(FabricMensagem.criaMensagemJogada(personagemEscolhido, valorJogada));

						Mensagem msg = conexao.lerMensagem(1000 /*espera m�xima em milisegundos*/);
						if(msg != null) {

							if(msg.isMensagemJogada() && (msg.getCampoJogada() == valorJogada)) {

								estado = EstadoJogo.CLIENTE_ESPERANDO_JOGADA_ADVERSARIO;

								break; //sai do while(true)

							} else if(msg.isMensagemErro() && (msg.getCampoTipoErro() == TipoErro.JOGADA_INVALIDA)){

								tela.mostrarMensagemErro("Jogada inv�lida");

								break; //sai do while(true). Vai escolher a jogada novamente.

							} else {
								conexao.limparEntrada();
								//								System.out.println("Aconteceu algum erro. msg="+msg+", ESTADO="+estado);
							}

						} else {
							Thread.sleep(100);
						}

					}

					break;
				}
				case CLIENTE_ESPERANDO_JOGADA_ADVERSARIO:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Cliente(ESTADO)="+estado);

					tela.setTextoEstado("Esperando jogada do advers�rio");
					tela.habilitaEscolhaPersonagem(false);
					tela.habilitaEscolhaJogada(false);

					Mensagem msg = conexao.lerMensagem(1000 /*espera m�xima em milisegundos*/);

					if(msg != null) {

						if(msg.isMensagemJogada() && (msg.getCampoPersonagem() != personagemEscolhido)) {

							tela.setJogadaAdversario(msg.getCampoJogada());
							System.out.println("Advers�rio ("+msg.getCampoPersonagem()+") jogou: " + msg.getCampoJogada());

						} else if(msg.isMensagemVencedor()) {

							if(msg.getCampoPersonagem() == personagemEscolhido) {
								tela.mostrarMensagemInfo("Voc� venceu");

							} else if(msg.getCampoPersonagem() == Personagem.EMPATE) {
								tela.mostrarMensagemInfo("Empate!");

							} else {
								tela.mostrarMensagemInfo("Voc� perdeu! O vencedor � o personagem " + msg.getCampoPersonagem());

							}

							tela.configurarCamposInicioJogo();
							estado = EstadoJogo.CLIENTE_ESCOLHENDO_PERSONAGENS;
						}

					} else {
						Thread.sleep(100);
					}

					break;
				}
				default:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Cliente(ESTADO)="+estado);

					estado = EstadoJogo.CLIENTE_ESCOLHENDO_PERSONAGENS;

					break;
				}

				}//fim do switch

			}//fim do while

		} catch(Exception e) {

			e.printStackTrace();
			tela.mostrarMensagemErro(e.getMessage());
			//			tela.fechar();
		}finally {


			//			if(sc != null)
			//				sc.close();
		}
	}

}
