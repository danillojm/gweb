package com.br.melo.classAbstrata;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.omnifaces.util.Faces;

import com.br.melo.dao.AlunoDAO;
import com.br.melo.util.HibernateUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public abstract class ParametroRelatorio {

	
	
	
	
	
	
	public void imprimir(List<Object> list, String nomeRelatorio) {

		AlunoDAO dao = new AlunoDAO();

		try {
			String nomeDoRelatorio = "rel_aluno";
			Connection conexao = HibernateUtil.getConexao();
			String caminho = Faces.getRealPath("/relatorios/" + nomeDoRelatorio + ".jasper");

			Map<String, Object> params = new HashedMap();
			JasperPrint relatorio = JasperFillManager.fillReport(caminho, null, new JRBeanCollectionDataSource(dao.listar()));
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
