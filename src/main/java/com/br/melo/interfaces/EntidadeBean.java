package com.br.melo.interfaces;
 

import org.hibernate.Hibernate;

 
public abstract class EntidadeBean {

	public static final String strId = "id";

	/**
	 * Retorna a ID, primary key, do bean.
	 * 
	 * @return ID do bean.
	 */
	public abstract Long getId();
	
	public String getSingleNameOfClass() {
		return getClass().getSimpleName();
	}
	
	/**
	 * Seta a ID, primary key, do bean.
	 * 
	 * @return ID do bean.
	 */
	public abstract void setId(Long id);

	/**
	 * Método que deve validar se o bean pode ser usado. Como se ele está
	 * 'indExcluido' ou 'Inativo'.
	 * 
	 * @return True se o bean estiver válido para o uso.
	 * @throws Exception
	 *             Alguma exception lançada na validação.
	 */
	public abstract boolean validar() throws Exception;

	/**
	 * Deverá setar os valores padrões das propriedades do bean.
	 * 
	 * @throws Exception
	 *             Alguma exception lançada no processo.
	 */
	public abstract void setValoresPadroes() throws Exception;

	
	@Override
	public int hashCode() {
		
		if (getId() == null) {
			return super.hashCode();
		}

		return getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		
//		if (this == obj) {
//			return true;
//		}
		
		if (Hibernate.getClass(this) != Hibernate.getClass(obj)) {
			return false;
		}
		
		EntidadeBean other = (EntidadeBean) obj;
		
		if (getId() == null || other.getId() == null) {
			return super.equals(other);
		}
		
		if (!getId().equals(other.getId())) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return "{\"entidadeBean\":[{\"id\":" + getId() + ", \"classe\":\"" + getSingleNameOfClass()+"\"}]}";
	}
	
	
	
}
