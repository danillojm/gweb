package com.br.melo.interfaces;

import java.sql.Timestamp;

/**
 * Interface que indica se um dado bean deve ser excluído de maneira lógica somente, ou seja, que este bean tenha uma propriedade dthExclusao.
 * 
 * 
 *
 */
public interface ExclusaoLogica {

	public static final String strDthExclusao = "dthExclusao";

	public abstract void setDthExclusao(Timestamp dthExclusao);

	/**
	 * Deve retornar a data de exclusão do Bean. Null caso ele não esteja excluído.
	 * 
	 * @return Data de exclusão do Bean.
	 */
	public abstract Timestamp getDthExclusao();
}
