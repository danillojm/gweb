package com.br.melo.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.br.melo.entidade.Bairro;
import com.br.melo.entidade.Cidade;
import com.br.melo.entidade.Logradouro;
import com.br.melo.entidade.Uf;
import com.br.melo.interfaces.EntidadeBean;

public abstract class Utils {

	public static final String NAO = "N"; // Indicador de negação
	public static final String SIM = "S"; // Indicador de afirmação

	public static final String SEPARADOR_DADO = "-/-";
	public static final String SEPARADOR_REGISTRO = "$$";

	public static final String SEPARADOR_SUB_DADO = "*/*";
	public static final String SEPARADOR_SUB_REGISTRO = "%%";

	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String tratarMensagemValidacaoBean(String msgValidacao) {
		Scanner scanner = new Scanner(msgValidacao);
		StringBuilder retorno = new StringBuilder();

		List<String> listaOrdenada = new ArrayList();

		do {

			try {
				String linha = scanner.nextLine();

				if (linha.startsWith("--")) {
					listaOrdenada.add(linha.replaceAll("-->", "\n"));
				}
			} catch (Exception e2) {
				// Não precisa tratar nada aqui, sempre cai no final da Str
				// e.printStackTrace();
			}

		} while (scanner.hasNextLine());

		Collections.sort(listaOrdenada, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		for (String linha : listaOrdenada) {
			retorno.append(linha);
		}

		return retorno.toString();
	}

	public static String mudaCapitalizacaoPrimeiraLetra(String texto, boolean isMaiuscula) {
		if (isMaiuscula) {
			return texto.substring(0, 1).toUpperCase() + texto.substring(1, texto.length());
		} else {
			return texto.substring(0, 1).toLowerCase() + texto.substring(1, texto.length());
		}
	}

	public static String formataTextoCamelCaseJava(String texto) {
		String txtFormatado = "";
		String[] palavras = texto.trim().split(" ");

		if (palavras.length <= 1) {
			if (texto.length() > 1) {
				return texto.substring(0, 1).toUpperCase() + texto.substring(1, texto.length()).toLowerCase();
			} else {
				return texto.toUpperCase();
			}
		}
		for (int i = 0; i < palavras.length; i++) {
			String palavra = palavras[i].toLowerCase();
			// Remove espaços duplos
			if (palavra.trim().isEmpty()) {
				continue;
			}
			txtFormatado += palavra.substring(0, 1).toUpperCase() + palavra.substring(1, palavra.length());
		}

		return txtFormatado.replaceAll(" ", "").trim();
	}

	public static String formataTextoCamelCasePT(String texto) {
		String txtFormatado = "";
		String[] palavras = texto.trim().split(" ");

		if (palavras.length <= 1) {
			if (texto.length() > 1) {
				return texto.substring(0, 1).toUpperCase() + texto.substring(1, texto.length()).toLowerCase();
			} else {
				return texto.toUpperCase();
			}
		}
		for (int i = 0; i < palavras.length; i++) {
			String palavra = palavras[i].toLowerCase();
			// Remove espaços duplos
			if (palavra.trim().isEmpty()) {
				continue;
			}
			if (palavra.length() == 1) {
				txtFormatado += palavra;
			} else if (palavra.length() == 2 && (palavra.equals("da") || palavra.equals("do") || palavra.equals("de") || palavra.equals("di") || palavra.equals("du"))) {
				txtFormatado += palavra;
			} else if (palavra.length() == 3 && (palavra.equals("das") || palavra.equals("dos") || palavra.equals("des") || palavra.equals("dis") || palavra.equals("dus"))) {
				txtFormatado += palavra;
			} else {
				txtFormatado += palavra.substring(0, 1).toUpperCase() + palavra.substring(1, palavra.length());
			}

			txtFormatado += " ";
		}

		return txtFormatado.trim();
	}

	public static Long numberToLong(Number n) {
		if (n instanceof Double)
			return Math.round((Double) n);
		else if (n instanceof Float)
			return (long) Math.round((Float) n);
		else if (n instanceof BigDecimal)
			return Math.round(((BigDecimal) n).doubleValue());
		else
			return (Long) n;
	}

	/**
	 * Este método é utilizado para recuperar e realizar o Unproxy da Entidade quando esta está mapeada como Lazy.<br/>
	 * Se uma Entidade é mapeada como Lazy, quando o Hibernate a recupera, ela não é instanciada como o Tipo da entidade e o <strong> instanceof </strong> só funciona após realizar o unproxy.
	 * 
	 * @param <T>
	 * @param var
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T var) {
		if (var == null) {
			throw new IllegalArgumentException("Passed argument is null");
		}

		Hibernate.initialize(var);
		if (var instanceof HibernateProxy) {
			var = (T) ((HibernateProxy) var).getHibernateLazyInitializer().getImplementation();
		}
		return var;
	}

	/**
	 * Retira caracteres que quando convertidos para UTF-8, correspondem a mais de um Byte!
	 * 
	 * @param string
	 * @return
	 */
	public static String retiraByteDuplo(String string) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < string.length(); i++) {
			Character c = string.charAt(i);

			if (c.toString().getBytes(Charset.forName("UTF-8")).length > 1) {
				sb.append(" ");
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * Remove todos os espaços da string
	 * 
	 * @param str
	 * @return
	 */
	public static String trimFull(String str) {

		if (str == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();

		char[] toChar = str.toCharArray();

		for (char ch : toChar) {
			if (ch != ' ') {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static String converter(long valor) {

		if (valor == 0L) {
			return "0 B";
		}

		String[] array = { "B", "KB", "MB", "GB", "TB" };

		int potencia = 0;
		int proxima = 0;
		boolean cond1;
		boolean cond2;
		proxima = potencia + 1;
		cond1 = Math.pow(2, potencia * 10) <= valor;
		cond2 = valor < Math.pow(2, proxima * 10);
		potencia++;

		while (!(cond1 && cond2)) {
			proxima = potencia + 1;
			cond1 = Math.pow(2, potencia * 10) <= valor;
			cond2 = valor < Math.pow(2, proxima * 10);
			potencia++;
		}
		potencia--;

		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("en", "US"));

		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(0);

		return nf.format(valor / Math.pow(2, potencia * 10)) + " " + array[potencia];
	}

	public static void geraBitmapDisplay(ByteArrayOutputStream baos, BufferedImage image, boolean incluiDimensoes) throws IOException {
		byte valor = 0;

		int posBit = 8;

		if (incluiDimensoes) {
			baos.write(image.getWidth());
			baos.write(image.getHeight());
		}

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {

				Color c = new Color(image.getRGB(x, y));

				posBit--;

				if (!(c.getRed() == 255 && c.getBlue() == 255 && c.getGreen() == 255)) {
					valor += Math.pow(2, posBit);
				}

				if (posBit == 0) {
					posBit = 8;

					baos.write(new byte[] { valor });

					valor = 0;
				}

			}
		}

		if (posBit != 8) {
			baos.write(new byte[] { valor });
		}
	}

	/**
	 * Retorna a data e hora pasada no formato: dd/MM/yyyy as HH:mm:sshs
	 * 
	 * @return
	 */
	public static String getStringDataHoraImpressaoTerminal(Date data) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

		String dataVenda = sdf.format(data).replaceAll("-", "as");
		dataVenda = dataVenda + "hs";

		return dataVenda;
	}

	public static boolean contemKey(Map<?, ?> mapa, Object key) {

		boolean encontrou = false;

		for (Object obj : mapa.keySet()) {
			if (obj.equals(key)) {
				encontrou = true;
				break;
			}
		}

		return encontrou;
	}

	public static String arredondaCampoFinal(String campo, int tam, String caracter) {
		StringBuffer sb = new StringBuffer();

		if (campo.length() > tam) {
			campo = campo.substring(campo.length() - tam);
		}

		sb.append(campo);

		for (int i = 0; i < tam - campo.length(); i++) {
			sb.append(caracter);
		}

		return sb.toString();
	}

	public static String arredondaString(String string, int tam, String caracter) {
		StringBuffer sb = new StringBuffer();

		if (string.length() > tam) {
			string = string.substring(0, tam);
		}

		sb.append(string);

		for (int i = 0; i < tam - string.length(); i++) {
			sb.append(caracter);
		}

		return sb.toString();
	}

	public static String arredondaCampo(String campo, int tam, String caracter) {

		if (tam < 0) {
			return campo;
		}

		StringBuffer sb = new StringBuffer();

		if (campo.length() > tam) {
			campo = campo.substring(campo.length() - tam);
		}

		for (int i = 0; i < tam - campo.length(); i++) {
			sb.append(caracter);
		}

		sb.append(campo);

		return sb.toString();
	}

	/**
	 * 
	 * Cria um senha com uma quantidade informada de caracteres que pode ser numï¿½rica ou alfaNumerica
	 * 
	 * @param qtdCaracteres
	 *            Quantidade de caracteres na senha
	 * @param somenteNumeros
	 *            Se a senha eh somente numï¿½rica (true) ou alfaNumerica (false)
	 * @return Senha criada a partir dos parï¿½metros
	 */
	public static String gerarSenha(int qtdCaracteres, Boolean somenteNumeros) {

		String[] caracteresPossiveis = new String[] {};

		if (somenteNumeros) {
			caracteresPossiveis = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		} else {
			caracteresPossiveis = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		}

		String senha = "";

		for (int x = 0; x < qtdCaracteres; x++) {
			int j = (int) (Math.random() * caracteresPossiveis.length);
			senha += caracteresPossiveis[j];
		}

		return senha;
	}

	/**
	 * Método que checa se a classe 'objetoClass' implementa diretamente ou indiretamente a interface 'interfaceImplementada'
	 * 
	 * @param objetoClass
	 *            - Classe que será checada
	 * @param interfaceImplementada
	 *            - Interface que deve ser extendida
	 * @return
	 */
	public static boolean checkIfImplements(Class<?> objetoClass, Class<?> interfaceImplementada) {

		if (!interfaceImplementada.isInterface()) {
			return false;
		}

		for (Class<?> interf : objetoClass.getInterfaces()) {
			if (interf == interfaceImplementada) {
				return true;
			}
		}

		if (objetoClass == Object.class) {
			return false;
		}

		while (objetoClass.getSuperclass() != Object.class) {

			objetoClass = objetoClass.getSuperclass();

			for (Class<?> interf : objetoClass.getInterfaces()) {
				if (interf == interfaceImplementada) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Método que checa se a classe 'objetoClass' extende diretamente ou indiretamente a classe 'classExtendida'
	 * 
	 * @param objetoClass
	 *            - Classe que será checada
	 * @param classExtendida
	 *            - Classe que deve ser extendida
	 * @return
	 */
	public static boolean checkIfExtend(Class<?> objetoClass, Class<?> classExtendida) {

		if (objetoClass == Object.class) {
			return true;
		}

		boolean encontrou = objetoClass.getSuperclass() == classExtendida;

		while (objetoClass.getSuperclass() != classExtendida && objetoClass != Object.class) {

			objetoClass = objetoClass.getSuperclass();

			if (objetoClass.getSuperclass() == classExtendida) {
				encontrou = true;
				break;
			}
		}

		return encontrou;
	}

	/**
	 * 
	 * Cria uma string com uma quantidade informada de letras.
	 * 
	 * @param qtdCaracteres
	 *            Quantidade de caracteres
	 * @return String aleatï¿½ria
	 */
	public static String gerarString(int qtdCaracteres) {

		String[] caracteresPossiveis = new String[] {};

		caracteresPossiveis = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

		String string = "";

		for (int x = 0; x < qtdCaracteres; x++) {
			int j = (int) (Math.random() * caracteresPossiveis.length);
			string += caracteresPossiveis[j];
		}

		return string;
	}

	public static boolean isListaVazia(List<Object> lista) {
		if (lista == null || lista.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Retorna a porcentagem do valor.
	 * 
	 * @param valor
	 * @param percent
	 * @return
	 */
	public static long getPorcentagem(long valor, double percent) {
		return Math.round(getPorcentagemSemArrendodamento(valor, percent));
	}

	/**
	 * Retorna a porcentagem do valor sem arrendondamento.
	 * 
	 * @param valor
	 * @param percent
	 * @return
	 */
	public static double getPorcentagemSemArrendodamento(long valor, double percent) {
		double prc = percent / 100D;

		double valorD = (double) valor * prc;

		return valorD;
	}

	public static long getValorAcrescentado(long valor, double prAcrescimo) {

		double valorPrc = getPorcentagemSemArrendodamento(valor, prAcrescimo);

		double valorD = (double) valor + valorPrc;

		return Math.round(valorD);
	}

	public static long getValorDescontado(long valor, double prcDesconto) {

		double valorPrc = getPorcentagemSemArrendodamento(valor, prcDesconto);

		double valorD = (double) valor - valorPrc;

		return Math.round(valorD);
	}

	public static Calendar setPrimeiraHoraDia(Date data) {

		return zerarHoras(data);

	}

	public static Calendar setUltimaHoraDia(Date data) {

		Calendar c = new GregorianCalendar();
		c.setTime(data);

		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);

		return c;

	}

	public static Long retornarQtdNotNull(Object... args) {
		Long retorno = 0l;
		for (Object object : args) {
			if (object != null) {
				if (object instanceof String && !((String) object).trim().isEmpty()) {
					retorno++;
				}

				if (!(object instanceof String)) {
					retorno++;
				}
			}
		}
		return retorno;
	}

	public static Calendar zerarHoras(Date data) {

		Calendar c = new GregorianCalendar();
		c.setTime(data);

		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		return c;

	}

	/**
	 * Mï¿½todos que retornam o id das empresas separando da forma como ï¿½ gravado no Terminal de Venda
	 * 
	 * @param idsEmpresas
	 * @return
	 */
	public static String getIdConfigEmpresa(String idsEmpresas) {
		return getIdsEmpresas(idsEmpresas)[0];
	}

	public static String[] getIdsEmpresas(String idsEmpresas) {
		return idsEmpresas.split("/");
	}

	/*
	 * public static Calendar getDiasUteis(Calendar calendar, Cidade cidade, Session sessao, int qtdDiasUteis) {
	 * 
	 * // Retira um dia em aberto. calendar.add(Calendar.DAY_OF_MONTH, -1); int i = 0; while (i < qtdDiasUteis) { if (Utils.isDiaUtil(calendar, cidade, sessao)) { i++; } calendar.add(Calendar.DAY_OF_MONTH, -1); } return calendar;
	 * 
	 * }
	 */

	public static Calendar getDthAtualLocal(Integer diferenca) {
		Calendar calendar = new GregorianCalendar();

		calendar.add(Calendar.HOUR_OF_DAY, diferenca);

		return calendar;
	}

	public static String preventNull(String str) {
		if (str == null) {
			return "";
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public static void fundirListas(List lista1, List lista2) {
		for (int i = 0; i < lista2.size(); i++) {
			lista1.add(lista2.get(i));
		}
	}

	public static String formataValor(String valStr) {
		int valor = 0;

		valor = Integer.parseInt(valStr);
		valStr = "" + valor;

		StringBuffer retorno = new StringBuffer();

		if (valor == 0) {
			retorno.append("0,00");
			return retorno.toString();
		}

		if (valor < 10) {
			retorno.append("0,0");
			retorno.append(valor);
			return retorno.toString();
		}
		if (valor < 100) {
			retorno.append("0,");
			retorno.append(valor);
			return retorno.toString();
		}

		int ii = 0;

		for (int i = valStr.length() - 3; i >= 0; i--) {

			String strAux = "";

			ii++;

			strAux = retorno.toString();

			retorno.delete(0, retorno.length());

			retorno.append(valStr.charAt(i));
			retorno.append(strAux);

			if (ii == 3 && i != 0) {
				strAux = retorno.toString();

				retorno.delete(0, retorno.length());

				retorno.append('.');
				retorno.append(strAux);

				ii = 0;
			}
		}

		retorno.append(",");
		retorno.append(valStr.substring(valStr.length() - 2));

		return retorno.toString();
	}

	public static ArrayList<ArrayList<String>> separarRegistros(String registro) {

		if (Utils.isStringVazia(registro)) {
			return null;
		}

		String[] registrosSplit = registro.split("\\$\\$");

		ArrayList<ArrayList<String>> retorno = new ArrayList<ArrayList<String>>();

		for (String reg : registrosSplit) {
			ArrayList<String> listaCol = new ArrayList<String>();
			String[] regSplit = reg.split(SEPARADOR_DADO);

			for (String dado : regSplit) {
				String dadoTratado = Utils.isStringVazia(dado) || dado.equalsIgnoreCase("NULL") ? null : dado;
				listaCol.add(dadoTratado);
			}
			retorno.add(listaCol);
		}

		return retorno;
	}

	public static String criaRegistro(List<List<String>> lista) {

		StringBuffer retorno = new StringBuffer();

		for (int i = 0; i < lista.size(); i++) {

			List<String> arrayRegistro = lista.get(i);

			StringBuffer registro = new StringBuffer();

			for (int ii = 0; ii < arrayRegistro.size(); ii++) {
				String item = arrayRegistro.get(ii);

				if (item == null || item.equals("")) {
					item = "null";
				}

				if (item.indexOf(Utils.SEPARADOR_DADO) >= 0) {
					item = item.replaceAll(Utils.SEPARADOR_DADO, "_");
				}
				if (item.indexOf(Utils.SEPARADOR_REGISTRO) >= 0) {
					// TODO - Separador de registro precisa ser dado escape
					item = item.replaceAll("\\$\\$", "_");
				}

				registro.append(item);

				if (ii != arrayRegistro.size() - 1) {
					registro.append(Utils.SEPARADOR_DADO);
				}
			}

			retorno.append(registro);

			if (i != lista.size() - 1) {
				retorno.append(Utils.SEPARADOR_REGISTRO);
			}
		}
		return retorno.toString();
	}

	// /**
	// * Método que calcula o número de dias entre duas datas
	// * @param d1
	// * @param d2
	// * @return Retorna o número de dias
	// */
	// public static long calculaIntervaloDuasDatas(Date d1, Date d2){
	//
	// long dt = (d2.getTime() - d1.getTime());
	//
	// return dt / 86400000L;
	// }

	public static String criaSubRegistro(List<List<String>> lista) {

		StringBuffer retorno = new StringBuffer();

		for (int i = 0; i < lista.size(); i++) {

			List<String> arrayRegistro = lista.get(i);

			StringBuffer registro = new StringBuffer();

			for (int ii = 0; ii < arrayRegistro.size(); ii++) {
				String item = arrayRegistro.get(ii);

				if (item == null || item.equals("")) {
					item = "null";
				}

				if (item.indexOf(Utils.SEPARADOR_SUB_DADO) >= 0) {
					item = item.replaceAll(Utils.SEPARADOR_SUB_DADO, "_");
				}
				if (item.indexOf(Utils.SEPARADOR_SUB_REGISTRO) >= 0) {
					// TODO - Separador de registro precisa ser dado escape
					item = item.replaceAll("\\%\\%", "_");
				}

				registro.append(item);

				if (ii != arrayRegistro.size() - 1) {
					registro.append(Utils.SEPARADOR_SUB_DADO);
				}
			}

			retorno.append(registro);

			if (i != lista.size() - 1) {
				retorno.append(Utils.SEPARADOR_SUB_REGISTRO);
			}
		}
		return retorno.toString();
	}

	/**
	 * Mï¿½todo que cria o codigo da empresa usavel pelos terminais, no formato: admin.tb_config_empresa.id/empresa.tb_empresa.id
	 * 
	 * @param idConfigEmpresa
	 *            - id de admin.tb_config_empresa
	 * @param idEmpresa
	 *            - idEmpresa de empresa.tb_empresa
	 * @return retorna a string pronta
	 */
	public static String criaCodigoEmpresa(String idConfigEmpresa, String idEmpresa) {
		return idConfigEmpresa + "/" + idEmpresa;
	}

	/**
	 * Formata um objeto Date para um formato em String no formato padrao do brasil
	 * 
	 * @param data
	 *            - objeto date
	 * @return - retorna a string no formato correto
	 */
	public static String formatDataParaString(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

	public static Date parseDate(String data) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return sdf.parse(data);
	}

	/**
	 * Pega um nome de um atributo e converte a primeira letra para maiuscula. Ex: primeiroNome ficaria: PrimeiroNome
	 * 
	 * @param prefixo
	 *            - Um prefixo que serï¿½ anexado no comeï¿½o do nome criado
	 * @param atributo
	 *            - nome do atributo que serï¿½ convertido
	 * @return - Retorna a string pronta
	 */
	public static String criaNomeMetodoComPrefixo(String prefixo, String atributo) {
		return prefixo + atributo.substring(0, 1).toUpperCase() + atributo.substring(1);
	}

	/**
	 * Cria o nome de um mï¿½todo get passando o nome do atributo
	 * 
	 * @param atributo
	 *            - nome do atributo
	 * @return - nome do mï¿½todo get referente ao atributo
	 */
	public static String getMetodoGet(String atributo) {
		return criaNomeMetodoComPrefixo("get", atributo);
	}

	public static String getMetodoIs(String atributo) {
		return criaNomeMetodoComPrefixo("is", atributo);
	}

	/**
	 * Cria o nome de um mï¿½todo set passando o nome do atributo
	 * 
	 * @param atributo
	 *            - nome do atributo
	 * @return - nome do mï¿½todo set referente ao atributo
	 */
	public static String getMetodoSet(String atributo) {
		return criaNomeMetodoComPrefixo("set", atributo);
	}

	/**
	 * Verifica se a string ï¿½ nula ou vazia.
	 * 
	 * @param str
	 *            - String a ser verificada
	 * @return - true se a string for vazia, false se nao.
	 */
	public static boolean isStringVazia(String str) {
		if (str != null && !str.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public static void printSacadoBolPrincipal1(Graphics2D g2, String texto) {
		printDados(g2, texto, 42, 1, 425, 117, -1, -1);
	}

	public static void printSacadoBolPrincipal(Graphics2D g2, String texto) {
		printDados(g2, texto, 50, 2, 390, 130, 390, 142);
	}

	public static void printInstrucoesBolPrincipal(Graphics2D g2, String instrucoes) {
		printDados(g2, instrucoes.replaceAll("\r", " ").replaceAll("\n", ""), 52, 2, 10, 130, 10, 142);
	}

	private static void printDados(Graphics2D g2, String instrucoes, int maxLinha, int numLinhas, int x1, int y1, int x2, int y2) {

		final int maxTotal = maxLinha * numLinhas;

		String st1 = null;
		String st2 = null;

		try {
			st1 = instrucoes.substring(0, maxLinha);
		} catch (StringIndexOutOfBoundsException s) {
			try {
				st1 = instrucoes.substring(0, instrucoes.length());
			} catch (StringIndexOutOfBoundsException ss) {
				st1 = null;
			}
		}

		try {
			st2 = instrucoes.substring(maxLinha, maxTotal);
		} catch (StringIndexOutOfBoundsException s) {
			try {
				st2 = instrucoes.substring(maxLinha, instrucoes.length());
			} catch (StringIndexOutOfBoundsException ss) {
				st2 = null;
			}
		}

		g2.drawString(preventNull(st1), x1, y1); // Linha 1

		if (x2 >= 0) {
			g2.drawString(preventNull(st2), x2, y2); // Linha 2
		}

	}

	/**
	 * Este metodo arredonda o pacote para ele conter um tamanho multiplo de 8, necessario para encriptar o pacote. ï¿½ adicionado ao final do pacote o caracter de codigo 0(NULL) ate que seu tamanho se torne multiplo de 8
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static byte[] arredondaPacotePOS(byte[] bytes, byte caracter) throws IOException {
		double tamanhoPacote = bytes.length;

		if (tamanhoPacote % 8 != 0) {
			double diferenca = tamanhoPacote / 8;
			diferenca = diferenca - (int) diferenca;

			int i = 0;

			diferenca = 1 - diferenca;

			while (diferenca > 0) {
				i++;
				diferenca -= 0.125;
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			baos.write(bytes);

			for (int ii = 0; ii < i; ii++) {
				baos.write(new byte[] { caracter });
			}
			bytes = baos.toByteArray();
		}

		return bytes;
	}

	/**
	 * Retorna o texto normalizado, com caracteres printáveis
	 * 
	 * @param texto
	 * @return
	 */
	public static String normalizarString(String texto) {
		String retorno = texto;

		retorno = Normalizer.normalize(retorno, Normalizer.Form.NFD).replaceAll("[^\\p{Print}]", "").replaceAll("\"", "");

		return retorno;
	}

	/**
	 * Formata o texto baseado de forma similar ao que é impresso na bubina do POS
	 * 
	 * @param qtdCharsLinha
	 *            - Quantidade de caracteres por linha
	 * @param texto
	 *            - Texto a ser formatado
	 * @param isHtml
	 *            - Indica se é pra exibir o texto em HTML ou ASCII
	 * @return - Retorna o texto formatado
	 */
	public static String processaTextoVisualizacaoPOS(int qtdCharsLinha, String texto, boolean isHtml) {

		if (texto.length() > qtdCharsLinha) {

			char[] arTexto = texto.toCharArray();

			int i = 0;

			StringBuilder sb = new StringBuilder();
			StringBuilder sbAux = new StringBuilder();

			for (char carac : arTexto) {

				i++;

				sbAux.append(carac);

				if (i == qtdCharsLinha || carac == '\n') {
					sb.append(sbAux);

					if (carac != '\n') {
						sb.append('\n');
					} else if (i == 1) {
						sb.delete(sb.length() - 1, sb.length());
					}

					sbAux.delete(0, sbAux.length());
					i = 0;
					continue;
				}
			}

			if (sbAux.length() > 0) {
				sb.append(sbAux);
			}

			texto = sb.toString();
		}

		if (isHtml) {
			texto = texto.replaceAll("\n", "<br />");
		}

		return texto;
	}

	/**
	 * Calcula a idade
	 * 
	 * @param dataNasc
	 * @return
	 */
	public static int getIdade(Date dataNasc) {

		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(dataNasc);

		// Cria um objeto calendar com a data atual
		Calendar today = Calendar.getInstance();

		// Obtém a idade baseado no ano
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

		dateOfBirth.add(Calendar.YEAR, age);

		// se a data de hoje é antes da data de Nascimento, então diminui 1(um)
		if (today.before(dateOfBirth)) {
			age--;
		}

		return age;
	}

	public static boolean isStringSomenteNumerica(String s) {

		return s.matches("[0-9]+");

	}

	public static String desacentuarString(String str) {
		StringBuilder sb = new StringBuilder();

		if (str != null && !str.isEmpty()) {
			for (char c : str.toCharArray()) {
				boolean lowerCase = Character.isLowerCase(c);

				c = Character.toLowerCase(c);

				char res;
				switch (c) {
				case 'á':
				case 'à':
				case 'ã':
				case 'â':
					res = 'a';
					break;
				case 'é':
				case 'ê':
					res = 'e';
					break;
				case 'í':
					res = 'i';
					break;
				case 'ó':
				case 'õ':
				case 'ô':
					res = 'o';
					break;
				case 'ú':
				case 'ü':
					res = 'u';
					break;
				default:
					res = c;
				}

				sb.append(lowerCase ? res : Character.toUpperCase(res));
			}
		}
		return sb.toString();

	}

	public static String getEnderecoCompletoSemCEP(Logradouro logradouro, String numEndereco) {
		StringBuilder sb = new StringBuilder();

		if (logradouro != null) {
			sb.append(logradouro.getNomLogradouro());
			if (numEndereco != null && !numEndereco.isEmpty()) {
				sb.append(", ");
				sb.append(numEndereco);
			}
			sb.append(" ");

			Bairro bairro = logradouro.getBairro();
			if (bairro != null && bairro.getNomBairro() != null) {
				sb.append(bairro.getNomBairro());
				sb.append(" ");

				Cidade cidade = bairro.getCidade();

				if (cidade != null && cidade.getNomeCidade() != null) {
					sb.append(cidade.getNomeCidade());
					sb.append(" ");

					Uf uf = cidade.getUf();
					if (uf != null && uf.getSigla() != null) {
						sb.append(uf.getSigla());
					}
				}
			}
		}

		return sb.toString();
	}

	/**
	 * Remove os acentos da string
	 * 
	 * @param campo
	 * @return
	 */
	public static String substituiAcento(String campo) {

		campo = campo.replaceAll("[ãâáà]", "a");
		campo = campo.replaceAll("[ÁÀÂÃÄ]", "A");
		campo = campo.replaceAll("[éêèë]", "e");
		campo = campo.replaceAll("[ÉÊÈË]", "E");
		campo = campo.replaceAll("[îíìï]", "i");
		campo = campo.replaceAll("[ÎÍÌÏ]", "I");
		campo = campo.replaceAll("[õôóòö]", "o");
		campo = campo.replaceAll("[ÕÔÓÒÖ]", "O");
		campo = campo.replaceAll("[ûúùü]", "u");
		campo = campo.replaceAll("[ÛÚÙÜ]", "U");
		campo = campo.replaceAll("[ç]", "c");
		campo = campo.replaceAll("[Ç]", "C");
		campo = campo.replaceAll("[Ñ]", "N");
		campo = campo.replaceAll("[ñ]", "n");
		campo = campo.replaceAll("[" + ((char) 186) + "]", "o");
		campo = campo.replaceAll("[" + ((char) 170) + "]", "a");
		campo = campo.replaceAll("[" + ((char) 180) + ((char) 96) + "]", "" + ((char) 39));

		return campo;
	}

	public static List<Long> getListaIdFromListCollection(List<? extends EntidadeBean> listaEntidadeBean) {
		List<Long> retorno = new ArrayList<Long>();

		for (@SuppressWarnings("unchecked")
		Iterator<EntidadeBean> iterator = (Iterator<EntidadeBean>) listaEntidadeBean.iterator(); iterator.hasNext();) {
			EntidadeBean entidadeBean = iterator.next();
			retorno.add(entidadeBean.getId());
		}

		return retorno;
	}

	public static Collection<Long> getListaIdFromListCollection(Collection<? extends EntidadeBean> listaEntidadeBean) {
		List<Long> retorno = new ArrayList<Long>();

		for (@SuppressWarnings("unchecked")
		Iterator<EntidadeBean> iterator = (Iterator<EntidadeBean>) listaEntidadeBean.iterator(); iterator.hasNext();) {
			EntidadeBean entidadeBean = iterator.next();
			retorno.add(entidadeBean.getId());
		}

		return retorno;
	}

	public static boolean isNull(EntidadeBean bean) {
		return bean == null || bean.getId() == null;
	}

	public static boolean isNotNull(EntidadeBean bean) {
		return bean != null && bean.getId() != null;
	}

	public static boolean isEmpty(List<?> list) {
		return list == null || list.size() == 0;
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> intersecaoConjuntos(Collection<T> conj1, Collection<T> conj2) throws Exception {
		Collection<T> resultado = conj1.getClass().newInstance();

		for (T obj : conj1) {
			if (conj2.contains(obj)) {
				resultado.add(obj);
			}
		}

		return resultado;
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> diferencaConjuntos(Collection<T> conj1, Collection<T> conj2) throws Exception {
		Collection<T> resultado = conj1.getClass().newInstance();

		for (T obj : conj1) {
			if (!conj2.contains(obj)) {
				resultado.add(obj);
			}
		}

		return resultado;
	}

	public static void imprimeCampos(Object ob) throws Exception {

		for (Field field : ob.getClass().getDeclaredFields()) {

			String metodoGet = Utils.getMetodoGet(field.getName());

			try {
				System.out.println(field.getName() + ": " + ob.getClass().getMethod(metodoGet, new Class<?>[] {}).invoke(ob, new Object[] {}));
			} catch (Exception e) {
				continue;
			}
		}

	}

	public static String removeCamelCase(String nomeParam) {
		String primeraLetra = nomeParam.substring(0, 1).toUpperCase();
		nomeParam = nomeParam.substring(1, nomeParam.length());
		String regEx = "([A-Z])";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(nomeParam);
		int i = 0;
		while (matcher.find()) {
			nomeParam = nomeParam.substring(0, (matcher.start(matcher.groupCount())) + i) + " " + nomeParam.substring(matcher.start(matcher.groupCount()) + i, nomeParam.length());
			i++;
		}
		nomeParam = primeraLetra + nomeParam;
		return nomeParam;
	}

	public static String imprimeCamposString(Object ob) {
		StringBuilder sb = new StringBuilder();

		String[][] ArrayParametros;

		try {
			// sb.append(ob.getClass().getSimpleName() + "\n");
			// sb.append("========================\n");

			for (Field field : ob.getClass().getDeclaredFields()) {

				String metodoGet = Utils.getMetodoGet(field.getName());

				try {
					Object retornoMetodo = ob.getClass().getMethod(metodoGet, new Class<?>[] {}).invoke(ob, new Object[] {});

					if (retornoMetodo != null) {

						String nomeParam = field.getName();

						if (nomeParam.startsWith("list")) {
							nomeParam = nomeParam.replaceFirst("list", "");
						}
						if (nomeParam.startsWith("a")) {
							nomeParam = nomeParam.replaceFirst("a", "");
						}
						if (nomeParam.startsWith("a")) {
							nomeParam = nomeParam.replaceFirst("a", "");
						}
						nomeParam = removeCamelCase(nomeParam);

						nomeParam = nomeParam.replaceFirst("Id ", "Código ");

						StringBuilder n = sb.append("<b>" + nomeParam + ":</b>");

						if (retornoMetodo instanceof List) {
							if (((List) retornoMetodo).isEmpty()) {
								sb.append(" Nada selecionado.");
							} else {
								for (Object obj : (List) retornoMetodo) {
									sb.append("  " + obj.toString() + ", ");
								}

								sb.deleteCharAt(sb.length() - 2);
								sb.append(".");
							}
						} else if (retornoMetodo instanceof Date) {
							sb.append("   " + DataUtil.strDataHora((Date) retornoMetodo));
						} else {
							sb.append("   " + retornoMetodo);
						}
						sb.append("\r\n");
					}
				} catch (Exception e) {
					continue;
				}
			}

			// sb.append("========================\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();

	}

	/**
	 * Método que retorna o valor monetário por Extenso.
	 * 
	 * @param vlr
	 * @return
	 */
	public static String valorMonetarioPorExtenso(Long valorMonetario) {
		if (valorMonetario == 0)
			return ("zero");

		String vlrString = Utils.formataValor(valorMonetario.toString());

		vlrString = vlrString.replaceFirst(",", ".");

		Double vlr = new Double(vlrString);

		long inteiro = (long) Math.abs(vlr); // parte inteira do valor
		double resto = vlr - inteiro; // parte fracionária do valor

		String vlrS = String.valueOf(inteiro);
		if (vlrS.length() > 15)
			return ("Erro: valor superior a 999 trilhões.");

		String s = "", saux, vlrP;
		String centavos = String.valueOf((int) Math.round(resto * 100));

		String[] unidade = { "", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez", "onze", "doze", "treze", "quatorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove" };
		String[] centena = { "", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos" };
		String[] dezena = { "", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa" };
		String[] qualificaS = { "", "mil", "milhão", "bilhão", "trilhão" };
		String[] qualificaP = { "", "mil", "milhões", "bilhões", "trilhões" };

		// definindo o extenso da parte inteira do valor
		int n, unid, dez, cent, tam, i = 0;
		boolean umReal = false, tem = false;
		while (!vlrS.equals("0")) {
			tam = vlrS.length();
			// retira do valor a 1a. parte, 2a. parte, por exemplo, para
			// 123456789:
			// 1a. parte = 789 (centena)
			// 2a. parte = 456 (mil)
			// 3a. parte = 123 (milhões)
			if (tam > 3) {
				vlrP = vlrS.substring(tam - 3, tam);
				vlrS = vlrS.substring(0, tam - 3);
			} else { // última parte do valor
				vlrP = vlrS;
				vlrS = "0";
			}
			if (!vlrP.equals("000")) {
				saux = "";
				if (vlrP.equals("100"))
					saux = "cem";
				else {
					n = Integer.parseInt(vlrP, 10); // para n = 371, tem-se:
					cent = n / 100; // cent = 3 (centena trezentos)
					dez = (n % 100) / 10; // dez = 7 (dezena setenta)
					unid = (n % 100) % 10; // unid = 1 (unidade um)
					if (cent != 0)
						saux = centena[cent];
					if ((n % 100) <= 19) {
						if (saux.length() != 0)
							saux = saux + " e " + unidade[n % 100];
						else
							saux = unidade[n % 100];
					} else {
						if (saux.length() != 0)
							saux = saux + " e " + dezena[dez];
						else
							saux = dezena[dez];
						if (unid != 0) {
							if (saux.length() != 0)
								saux = saux + " e " + unidade[unid];
							else
								saux = unidade[unid];
						}
					}
				}
				if (vlrP.equals("1") || vlrP.equals("001")) {
					if (i == 0) // 1a. parte do valor (um real)
						umReal = true;
					else
						saux = saux + " " + qualificaS[i];
				} else if (i != 0)
					saux = saux + " " + qualificaP[i];
				if (s.length() != 0)
					s = saux + ", " + s;
				else
					s = saux;
			}
			if (((i == 0) || (i == 1)) && s.length() != 0)
				tem = true; // tem centena ou mil no valor
			i = i + 1; // próximo qualificador: 1- mil, 2- milhão, 3- bilhão,
						// ...
		}

		if (s.length() != 0) {
			if (umReal)
				s = s + " real";
			else if (tem)
				s = s + " reais";
			else
				s = s + " de reais";
		}

		// definindo o extenso dos centavos do valor
		if (!centavos.equals("0")) { // valor com centavos
			if (s.length() != 0) // se não é valor somente com centavos
				s = s + " e ";
			if (centavos.equals("1"))
				s = s + "um centavo";
			else {
				n = Integer.parseInt(centavos, 10);
				if (n <= 19)
					s = s + unidade[n];
				else { // para n = 37, tem-se:
					unid = n % 10; // unid = 37 % 10 = 7 (unidade sete)
					dez = n / 10; // dez = 37 / 10 = 3 (dezena trinta)
					s = s + dezena[dez];
					if (unid != 0)
						s = s + " e " + unidade[unid];
				}
				s = s + " centavos";
			}
		}
		return (s);
	}

	public static String getDiferencaPercentual(Long valorInicial, Long valorFinal) {

		Long diff = valorFinal - valorInicial;

		double percent = ((double) diff / (double) valorInicial);

		System.out.println(percent);

		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(3);
		return nf.format(percent);
	}

	public static String concatenaComZerosEsquerda(String vlr, int tamanho) {
		String resultado = null;

		if (vlr != null) {
			int diferenca = tamanho - vlr.toString().length();

			String zeros = "";

			while (diferenca > 0) {
				zeros += "0";
				diferenca--;
			}

			resultado = zeros + vlr;
		}

		return resultado.substring(0, tamanho);
	}

	public static String concatenaComZerosEsquerda(Long vlr, int tamanho) {
		return concatenaComZerosEsquerda(vlr.toString(), tamanho);
	}

	public static String concatenaComEspacosBrancosDireita(String vlr, int tamanho) {
		String resultado = null;

		if (vlr == null) {
			vlr = "";
		}

		if (vlr != null) {
			int diferenca = tamanho - vlr.toString().length();

			String espaco = "";

			while (diferenca > 0) {
				espaco += " ";
				diferenca--;
			}

			resultado = vlr + espaco;
		}

		return resultado.substring(0, tamanho);
	}

	// public static boolean isApliccellRV(String identificadorAppl , Empresa
	// empresa){
	//
	// boolean isAplicCell
	// =identificadorAppl.equals(TerminalAppl.IDENTIFICADOR_APLICCELLN40);
	//
	// ArrayList<String> listaEmpresas = new ArrayList<String>();
	// listaEmpresas.add("vailog");
	//
	// if(ParametrosGlobais.isDesenvolvimento){
	// return isAplicCell;
	// }else{
	// ConfigGerencialWeb config =
	// Utils.initializeAndUnproxy(empresa.getConfigGerencialWeb());
	// if(isAplicCell && listaEmpresas.contains(config.getNomSchema()) ){
	// return true;
	// }
	// }
	//
	// return false;
	// }
	//
	// public static TerminalVenda getTerminalRV(String numSerialStr,Session
	// sessao) throws ConfigEmpresaNaoLocalizadaException {
	//
	// Criteria ct = sessao.createCriteria(TerminalVenda.class);
	// ct.createAlias(TerminalVenda.strProdutoTerminal,
	// TerminalVenda.strProdutoTerminal);
	// ct.createAlias(TerminalVenda.strProdutoTerminal+"."+ProdutoTerminal.strTerminalAppl,
	// ProdutoTerminal.strTerminalAppl);
	//
	// ct.add(Restrictions.eq(ProdutoTerminal.strTerminalAppl+"."+TerminalAppl.strIdentificadorTerminal,
	// TerminalAppl.IDENTIFICADOR_APLICCELLN40));
	// String numSerial = numSerialStr.replaceAll("\\D", "");
	// ct.add(Restrictions.eq(TerminalVenda.strNumSerial,
	// Long.parseLong(numSerial)));
	// ct.add(Restrictions.isNotNull(TerminalVenda.strIdTerminalRV));
	//
	// return (TerminalVenda) ct.uniqueResult();
	// }

	public static Integer getInteiro(String str) {
		String numeros = "0123456789";
		StringBuilder ret = new StringBuilder();
		int qnt = str.length();

		for (int i = 0; i < qnt; i++) {
			if (numeros.indexOf(str.charAt(i)) != -1) {
				ret.append(str.charAt(i));
			}
		}

		return new Integer(ret.length() > 0 ? ret.toString() : "0");

	}

	public static String getDataFormatoRv(Date data) {
		return "";
	}

	public static String getDataFormatoRV(String data) {
		return "";
	}

	public static void uploadFtp(String host, String user, String pass, StringBuilder sbLinhas) throws Exception {

		String nomArquivo = "ENVIO_" + DataUtil.strDataDDMMYYYY_hhmmss(new Timestamp(new Date().getTime())) + ".csv";
		String nomeDiretorioTemp = System.getProperty("java.io.tmpdir");
		String filePath = nomeDiretorioTemp + nomArquivo;

		FileOutputStream fos = new FileOutputStream(nomeDiretorioTemp + nomArquivo);

		fos.write(sbLinhas.toString().getBytes());
		fos.flush();
		fos.close();

		String ftpUrl = "ftp://%s:%s@%s/%s;type=i";
		String uploadPath = "/SFA//parceiro//" + nomArquivo;

		ftpUrl = String.format(ftpUrl, user, pass, host, uploadPath);
		System.out.println("Upload URL: " + ftpUrl);

		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			OutputStream outputStream = conn.getOutputStream();
			FileInputStream inputStream = new FileInputStream(filePath);

			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			inputStream.close();
			outputStream.close();

			System.out.println("File uploaded");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static String gerarMd5(String senha) {
		String sen = "";
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
		sen = hash.toString(16);
		return sen;
	}

	public static String gerarHashString(String frase) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(frase.getBytes());
		byte[] hashMd5 = md.digest();

		return stringHexa(hashMd5);
	}

	public static String gerarHashMD5(File arquivo) throws Exception {
		FileInputStream fileInputStream = new FileInputStream(arquivo);
		byte[] buffer = new byte[1024];

		MessageDigest md = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fileInputStream.read(buffer);
			if (numRead > 0) {
				md.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fileInputStream.close();

		byte[] bytes = md.digest();

		// Hexa para string
		// StringBuilder stringBuilder = new StringBuilder();
		// for (int i = 0; i < bytes.length; i++) {
		// int alto = ((bytes[i] >> 4) & 0xf) << 4;
		// int baixo = bytes[i] & 0xf;
		// if (alto == 0){
		// stringBuilder.append('0');
		// }
		// stringBuilder.append(Integer.toHexString(alto | baixo));
		// }
		return stringHexa(bytes);
	}

	private synchronized static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}

	public static String getStringSoComNumero(String string) {

		if (string == null || string.isEmpty()) {
			return "";
		}
		return string.replaceAll("\\D", "");

	}
}
