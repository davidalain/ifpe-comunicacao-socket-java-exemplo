package jogo.servidor;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;

import jogo.Conexao;
import jogo.EstadoJogo;
import jogo.Personagem;
import jogo.mensagem.FabricMensagem;
import jogo.mensagem.Mensagem;
import jogo.mensagem.TipoErro;
import threaded.Config;

public class MainServidor {

	public static void main(String[] args) throws IOException {

		DadosJogo dadosJogo = new DadosJogo(EstadoJogo.SERVIDOR_ESPERANDO_CONEXOES_JOGADORES);

		int contadorPrint = -2; 

		try {

			//Instancia o ServerSocket ouvindo a porta
			ServerSocket servidor = new ServerSocket(Config.PORT);
			System.out.println("S#: Servidor TCP ouvindo a porta " + Config.PORT);
			int idConexao = 0;

			while(true) {

				/**
				 * Máquina de estados
				 */

				switch (dadosJogo.getEstado()) {
				case SERVIDOR_ESPERANDO_CONEXOES_JOGADORES:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Servidor(ESTADO)="+dadosJogo.getEstado());

					Conexao novoCliente = new Conexao(servidor.accept(), idConexao++);

					dadosJogo.getClientes().add(novoCliente);

					System.out.println("S#: Jogador "+(dadosJogo.getClientes().size())+" conectado. IP:porta={" + novoCliente.getSocket().getInetAddress().getHostAddress() + ":" + novoCliente.getSocket().getPort()+ "}");


					//Ainda tem jogadores para se conectar
					if(!dadosJogo.todosClientesConectados()) {
						//novoCliente.enviarMensagem(FabricMensagem.criaMensagemEstadoJogo(dadosJogo.getEstado()));

					} else {
						//Todos os jogadores já se conectaram
						dadosJogo.setEstado(EstadoJogo.SERVIDOR_ESPERANDO_ESCOLHAS_PERSONAGENS);
						contadorPrint = -1;

					}

					break;
				}
				case SERVIDOR_ESPERANDO_ESCOLHAS_PERSONAGENS:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Servidor(ESTADO)="+dadosJogo.getEstado());

					//Lê as escolhas dos personagens
					for(Conexao cliente : dadosJogo.getClientes()) {

						Mensagem msg = cliente.lerMensagem();

						//Tem alguma mensagem nova
						if(msg != null) {

							if(msg.isMensagemEscolhaPersonagem()) {

								Personagem personagemEscolhido = msg.getCampoPersonagem();
								System.out.println("S#: Cliente " +cliente+ " escolheu seu personagem: " + personagemEscolhido);

								//Verifica se o personagem escolhido pelo jogador já não foi escolhido por outro jogador
								if((!dadosJogo.getMapaPersonagens().containsValue(personagemEscolhido)) || (dadosJogo.getMapaPersonagens().get(cliente) == personagemEscolhido)) {

									//Coloca o personagem escolhido pelo cliente
									dadosJogo.getMapaPersonagens().put(cliente, personagemEscolhido);

									//Envia para o cliente a confirmação de que o personagem foi escolhido
									cliente.enviarMensagem(FabricMensagem.criaMensagemEscolhaPersonagem(personagemEscolhido));
									System.out.println("S#: Criou mensagem OK escolha personagem " + personagemEscolhido + " para o cliente " + cliente);

								}else {

									//Envia para o cliente uma mensagem de erro informando que o personagem já foi escolhido por outro jogador
									cliente.enviarMensagem(FabricMensagem.criaMensagemErro(TipoErro.PERSONAGEM_JA_ESCOLHIDO));
									System.out.println("S#: Criou mensagem ERRO escolha personagem " + personagemEscolhido + " para o cliente " + cliente+". Personagem já escolhido!!!");

								}

								System.out.println("S#: mapaPesonagens="+dadosJogo.getMapaPersonagens().entrySet());
							}

						} 

					}

					//Todos os jogadores já escolheram seus personagens?
					if(dadosJogo.todosPersonsagensEscolhidos()) {

						dadosJogo.setEstado(EstadoJogo.SERVIDOR_ESPERANDO_JOGADAS);
						contadorPrint = -1;

					} else {
						Thread.sleep(100);
					}

					break;
				}
				case SERVIDOR_ESPERANDO_JOGADAS:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Servidor(ESTADO)="+dadosJogo.getEstado());

					//Lê as jogadas
					for(Conexao cliente : dadosJogo.getClientes()) {

						Mensagem msg = cliente.lerMensagem();

						//Tem alguma mensagem nova
						if(msg != null) {

							if(msg.isMensagemJogada()) {

								int valorJogada = msg.getCampoJogada();

								//Verifica se a jogada é válida
								if(valorJogada >= 0) {

									//Associa a jogada com o jogador
									dadosJogo.getMapaJogadas().put(cliente, valorJogada);

									Personagem personagem = dadosJogo.getMapaPersonagens().get(cliente);

									//Envia para o cliente a confirmação de que a jogada foi recebida
									cliente.enviarMensagem(FabricMensagem.criaMensagemJogada(personagem, valorJogada));

								}else {

									//Envia para o cliente uma mensagem de erro informando que o personagem já foi escolhido por outro jogador
									cliente.enviarMensagem(FabricMensagem.criaMensagemErro(TipoErro.JOGADA_INVALIDA));
								}

							}

						} else {
							Thread.sleep(100);		
						}

					}

					//Todos os jogadores já fizeram suas jogadas?
					if(dadosJogo.todasJogadasRealizadas()) {

						//Informa pra todos os jogadores as jogadas uns dos outros
						for(Conexao cliente : dadosJogo.getClientes()) {

							for(Conexao clienteJogada : dadosJogo.getClientes()) {

								//Não a jogada do mesmo jogador, só dos outros jogadores
								if(!cliente.equals(clienteJogada)) {

									Personagem personagem = dadosJogo.getMapaPersonagens().get(clienteJogada);
									int jogada = dadosJogo.getMapaJogadas().get(clienteJogada);

									//Envia para o cliente a jogada dos outros clientes
									cliente.enviarMensagem(FabricMensagem.criaMensagemJogada(personagem, jogada));

								}

							}

						}

						dadosJogo.setEstado(EstadoJogo.SERVIDOR_ANALISA_JOGADAS);
						contadorPrint = -1;
					}

					break;
				}
				case SERVIDOR_ANALISA_JOGADAS:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Servidor(ESTADO)="+dadosJogo.getEstado());
					/**
					 * Analisa as jogadas de cada um e decide quem é o vencedor
					 */

					int soma = 0;
					for(Integer jogada : dadosJogo.getMapaJogadas().values()) {
						soma += jogada;
					}

					Conexao clienteVencedor = null;
					for(Conexao cliente : dadosJogo.getClientes()) {

						//Soma deu par
						if(soma % 2 == 0) {

							if(dadosJogo.getMapaPersonagens().get(cliente).equals(Personagem.PAR)) {
								//Salva o jogador vencedor
								clienteVencedor = cliente;
								break;
							}

						}else { //Soma deu ímpar

							if(dadosJogo.getMapaPersonagens().get(cliente).equals(Personagem.IMPAR)) {
								//Salva o jogador vencedor
								clienteVencedor = cliente;
								break;
							}
						}
					}

					//Envia para todos os jogadores a mensagem do vencedor
					for(Conexao cliente : dadosJogo.getClientes()) {

						Personagem personagemVencedor = dadosJogo.getMapaPersonagens().get(clienteVencedor);

						cliente.enviarMensagem(FabricMensagem.criaMensagemVencedor(personagemVencedor));
					}

					dadosJogo.setEstado(EstadoJogo.SERVIDOR_FIM_DE_JOGO);
					contadorPrint = -1;

					break;
				}
				case SERVIDOR_FIM_DE_JOGO:
				default:
				{
					//if(contadorPrint++ == 0)
					//System.out.println("Servidor(ESTADO)="+dadosJogo.getEstado());

					dadosJogo.setEstado(EstadoJogo.SERVIDOR_ESPERANDO_ESCOLHAS_PERSONAGENS);

					/**
					 * Limpa todas as variáveis dos jogadores e volta pra o estado de escolha dos personagens
					 */
					dadosJogo.getMapaJogadas().clear();
					dadosJogo.getMapaPersonagens().clear();

					contadorPrint = -1;


					//					Thread.sleep(100);

					break;
				}

				}//fim do switch

			}//fim do while

		} catch(BindException e) {
			System.out.println("S#: Erro: já existe um processo utilizando esta porta!");
		} catch(Exception e) {
			System.out.println("S#: Erro: " + e.getMessage());
		} finally {

		} 
	}


}
