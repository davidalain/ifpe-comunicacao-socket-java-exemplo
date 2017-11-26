package jogo.mensagem;

import jogo.Personagem;

public class FabricMensagem {

	public static final Mensagem criaMensagemJogada(Personagem personagem, int valorJogada) {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);
		
		msg.getCampos()[0] = TipoMensagem.MSG_JOGADA.ordinal();
		msg.getCampos()[1] = personagem.ordinal();
		msg.getCampos()[2] = valorJogada;

		return msg;
	}

	//public static final Mensagem criaMensagemTabuleiro(int[] jogadas) {
	//	Mensagem msg = new Mensagem();
	//
	//	msg.getCampos()[0] = TipoMensagem.MSG_JOGADA.ordinal();
	//	msg.getCampos()[1] = valorJogada;
	//	
	//	return msg;
	//}

	public static final Mensagem criaMensagemEscolhaPersonagem(Personagem personagem) {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);

		msg.getCampos()[0] = TipoMensagem.MSG_ESCOLHA_PERSONAGEM.ordinal();
		msg.getCampos()[1] = personagem.ordinal();

		return msg;
	}

	public static final Mensagem criaMensagemErro(TipoErro erro) {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);

		msg.getCampos()[0] = TipoMensagem.MSG_ERRO.ordinal();
		msg.getCampos()[1] = erro.ordinal();

		return msg;
	}

	public static final Mensagem criaMensagemVencedor(Personagem personsagem) {
		Mensagem msg = new Mensagem(new int[Mensagem.MSG_SIZE]);

		msg.getCampos()[0] = TipoMensagem.MSG_VENCEDOR.ordinal();
		msg.getCampos()[1] = personsagem.ordinal();

		return msg;
	}

}
