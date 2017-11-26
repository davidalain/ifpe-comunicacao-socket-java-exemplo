package jogo.mensagem;

public enum TipoMensagem {
	MSG_JOGADA, 				//Cliente informando para o servidor a sua jogada
	MSG_ESCOLHA_PERSONAGEM,		//Cliente informando para o servidor a sua escolha de personagem

	MSG_TABULEIRO,				//Servidor informando para os clientes o estado atual do tabuleiro do jogo
	MSG_VENCEDOR,				//Servidor informando para os clientes quem foi o vencedor da partida
	MSG_ERRO,					//Mensagem de erro
}
