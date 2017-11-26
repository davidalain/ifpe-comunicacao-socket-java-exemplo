package jogo.cliente;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import jogo.Conexao;
import jogo.EstadoJogo;
import jogo.Personagem;
import jogo.mensagem.FabricMensagem;
import jogo.mensagem.Mensagem;
import jogo.mensagem.TipoErro;
import threaded.Config;

public class MainCliente {

	public static void main(String[] args) throws IOException {

		Scanner sc = null;

		try {

			sc = new Scanner(System.in);
			EstadoJogo estado = EstadoJogo.CLIENTE_CONECTANDO_AO_SERVIDOR;
			Conexao conexao = null;
			Personagem personagemEscolhido = null;
			int contadorPrint = -1;

			while(true) {

				/**
				 * M�quina de estados
				 */
				switch (estado) {
				case CLIENTE_CONECTANDO_AO_SERVIDOR:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Cliente(ESTADO)="+estado);

					conexao = new Conexao(new Socket(Config.HOST, Config.PORT));

					System.out.println("C#: Jogador conectado. IP:porta={" + conexao.getSocket().getInetAddress().getHostAddress() + ":" + conexao.getSocket().getPort()+ "}");

					estado = EstadoJogo.CLIENTE_ESCOLHENDO_PERSONAGENS;
					contadorPrint = -1;
					
					System.out.println("=============== START =====================");

					break;
				}
				case CLIENTE_ESCOLHENDO_PERSONAGENS:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Cliente(ESTADO)="+estado);

					/**
					 * Habilitar a tela para escolher os poss�veis personagens.
					 */
					//Neste caso como � um jogo de par ou impar, os poss�veis personagens s�o PAR e �MPAR
					int valor = -1;
					
					//L� do teclado a escolha do jogador
					do {
						System.out.println("Digite "+Personagem.PAR.ordinal()+" para PAR ou "+Personagem.IMPAR.ordinal()+" para �MPAR");
						try {
							valor = Integer.parseInt(sc.next());
						}catch (Exception e) {

						}
					}while(valor != 0 && valor != 1);

					personagemEscolhido = Personagem.values()[valor];
					System.out.println("Escolheu o personagem " + personagemEscolhido);

					while(true) {

						conexao.enviarMensagem(FabricMensagem.criaMensagemEscolhaPersonagem(personagemEscolhido));
						Thread.sleep(100);

						Mensagem msg = conexao.lerMensagem();

						if(msg != null) {

							if(msg.isMensagemEscolhaPersonagem() && (msg.getCampoPersonagem() == personagemEscolhido)) {

								estado = EstadoJogo.CLIENTE_ESCOLHENDO_JOGADA;
								contadorPrint = -1;
								
								break; //sai do while(true)

							} else if(msg.isMensagemErro() && (msg.getCampoTipoErro() == TipoErro.PERSONAGEM_JA_ESCOLHIDO)){
								System.out.println("Personagem j� foi escolhido por outro jogador!");

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
					//if(contadorPrint++ == 0)
					//System.out.println("Cliente(ESTADO)="+estado);

					/**
					 * Habilita a tela para que o jogador fa�a sua jogada
					 */ 
					//Neste caso como � um jogo de par ou �mpar, ent�o o jogador escolhe um n�mero inteiro n�o negativo.

					int valor = -1;

					//L� do teclado a escolha do jogador
					do {
						System.out.println("Digite sua jogada");
						try {
							valor = Integer.parseInt(sc.next());	
						}catch (Exception e) {

						}
					}while(valor < 0);
					System.out.println("Voc� ("+personagemEscolhido+") jogou o valor " + valor);

					while(true) {

						conexao.enviarMensagem(FabricMensagem.criaMensagemJogada(personagemEscolhido, valor));
						Thread.sleep(100);

						Mensagem msg = conexao.lerMensagem();
						if(msg != null) {

							if(msg.isMensagemJogada() && (msg.getCampoJogada() == valor)) {

								estado = EstadoJogo.CLIENTE_ESPERANDO_JOGADA_ADVERSARIO;
								contadorPrint = -1;
								
								break; //sai do while(true)

							} else if(msg.isMensagemErro() && (msg.getCampoTipoErro() == TipoErro.JOGADA_INVALIDA)){
								System.out.println("Jogada inv�lida");
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
					
					Mensagem msg = conexao.lerMensagem();

					if(msg != null) {
						
						if(msg.isMensagemJogada() && (msg.getCampoPersonagem() != personagemEscolhido)) {
							System.out.println("Advers�rio ("+msg.getCampoPersonagem()+") jogou: " + msg.getCampoJogada());
							
						} else if(msg.isMensagemVencedor()) {

							if(msg.getCampoPersonagem() == personagemEscolhido) {
								System.out.println("Voc� venceu!");
							} else if(msg.getCampoPersonagem() == Personagem.EMPATE) {
								System.out.println("Empate!");
							} else {
								System.out.println("Voc� perdeu!");
							}

							estado = EstadoJogo.CLIENTE_ESCOLHENDO_PERSONAGENS;
							contadorPrint = -1;
							System.out.println("=============== START =====================");
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
					contadorPrint = -1;

					break;
				}

				}//fim do switch

			}//fim do while

		} catch(Exception e) {
			e.printStackTrace();
		}finally {

			if(sc != null)
				sc.close();
		}
	}

}
