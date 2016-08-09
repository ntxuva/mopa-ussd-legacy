package scode.ntxuva.sms;

public enum Questions {
	
	MAIN_MENU("Bem-vindo ao MOPA \r\n1. Enviar reclamacao \r\n" 
			+ "2. Saber estado de reclamacao \r\n3. Saber mais sobre o MOPA"), 
	COMPLAINT_ID("Submeter numero de reclamacao"),
	BAIRRO_MENU("Escolha o bairro"),
	COMPLAINT_MENU("Escolha a categoria do problema"),
	CONTAINER_ID("Qual o contentor?"),
	BLOCK_NUMBER("Qual o quarteirão?"),
	COMMENT_MENU("A reclamacao esta pronta. \r\n1. Enviar reclamacao \r\n"
			+ "2. Enviar mais informacao"),
	COMMENT("Explique o seu problema"),
	END_COMPLAINT_ID("Obrigado. Brevemente enviaremos o estado da sua reclamacao."), 
	END_MOPA_INFO("Obrigado. Brevemente enviaremos informacao do MOPA."),
	END_COMMENT("Obrigado. Brevemente enviaremos o numero da sua reclamacao."),
	INVALID("Submeteu dados invalidos. Por favor, tente novamente."), 
	SYSTEM_ERROR("Houve um erro no sistema. Por favor, tente novamente."), 
	MOPA_INFO("MOPA - Sistema de Monitoria Participativa de Servicos Urbanos de Maputo \r\n\r\n"
			+ "Categorias de problemas: \r\n"
			+ "- Camiao nao passou \r\n- Lixo fora do contentor \r\n"
			+ "- Contentor a arder \r\n- Tchova nao passou \r\n"
			+ "- Lixeira informal \r\n- Lixo na vala de drenagem \r\n\r\n"
			+ "Bairros disponiveis: \r\n"
			+ "- Magoanine C\r\n- Inhagoia B \r\n"
			+ "- Maxaquene A \r\n- Polana Canico B\r\n\r\n"
			+ "Mopar os problemas no lixo em Maputo utilizando *311#");
	
	private String text;
	private Questions(String text) { this.text = text; }
	public String toString() { return text; }
	

	
}