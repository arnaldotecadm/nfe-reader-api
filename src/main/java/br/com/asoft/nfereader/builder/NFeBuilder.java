package br.com.asoft.nfereader.builder;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.glassfish.jersey.internal.util.SimpleNamespaceResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.asoft.nfereader.model.Dest;
import br.com.asoft.nfereader.model.Ide;
import br.com.asoft.nfereader.model.InfNFe;
import br.com.asoft.nfereader.model.NFe;
import br.com.asoft.nfereader.model.NFeEmit;
import br.com.asoft.nfereader.model.NfeProc;
import br.com.asoft.nfereader.model.ProtNFe;

public class NFeBuilder {

	private Document document;
	private XPath xpath;

	private NfeProc nfeProc;
	private Ide ide;
	private NFe nfe;
	private InfNFe infNFe;
	private ProtNFe protNFe;
	private NFeEmit nfeEmit;
	private Dest dest;

	public NFeBuilder(Document document) {
		this.document = document;

		XPathFactory factory = XPathFactory.newInstance();
		this.xpath = factory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceResolver("xmlns", "http://www.portalfiscal.inf.br/nfe"));

		this.nfeProc = new NfeProc();
	}

	public NFeBuilder comNFe() {
		this.nfe = new NFe();
		return this;
	}

	public NFeBuilder comInfNfe() {
		this.infNFe = new InfNFe();
		return this;
	}

	public NFeBuilder comIde() {
		Element xmlIDE = (Element) getObject("//infNFe/ide", XPathConstants.NODE);
		this.ide = new Ide(extractTextValue(xmlIDE.getElementsByTagName("cUF")),
				extractTextValue(xmlIDE.getElementsByTagName("cNF")),
				extractTextValue(xmlIDE.getElementsByTagName("natOp")),
				extractTextValue(xmlIDE.getElementsByTagName("nNF")),
				extractTextValue(xmlIDE.getElementsByTagName("dhEmi")),
				extractTextValue(xmlIDE.getElementsByTagName("tpNF")),
				extractTextValue(xmlIDE.getElementsByTagName("idDest")),
				extractTextValue(xmlIDE.getElementsByTagName("cMunFG")),
				extractTextValue(xmlIDE.getElementsByTagName("indFinal")),
				extractTextValue(xmlIDE.getElementsByTagName("indPres")));
		return this;
	}

	public NFeBuilder comEmit() {
		Element xmlEmit = (Element) getObject("//infNFe/emit", XPathConstants.NODE);
		this.nfeEmit = new NFeEmit(extractTextValue(xmlEmit.getElementsByTagName("CNPJ")),
				extractTextValue(xmlEmit.getElementsByTagName("xNome")),
				extractTextValue(xmlEmit.getElementsByTagName("xFant")),
				extractTextValue(xmlEmit.getElementsByTagName("IE")),
				extractTextValue(xmlEmit.getElementsByTagName("CRT")));

		return this;
	}

	public NFeBuilder comDest() {
		Element xmlEmit = (Element) getObject("//infNFe/emit", XPathConstants.NODE);
		this.dest = new Dest(extractTextValue(xmlEmit.getElementsByTagName("CNPJ")),
				extractTextValue(xmlEmit.getElementsByTagName("xNome")),
				extractTextValue(xmlEmit.getElementsByTagName("indIEDest")));

		return this;
	}

	public NfeProc build() {
		this.nfeProc.setNfe(nfe);
		this.nfeProc.setProtNFe(protNFe);

		if (this.nfe != null) {
			if (infNFe != null) {
				this.nfeProc.getNfe().setInfNFe(infNFe);
				this.nfeProc.getNfe().getInfNFe().setIde(ide);
			}

			if (nfeEmit != null) {
				this.nfeProc.getNfe().setInfNFe(infNFe);
				this.nfeProc.getNfe().getInfNFe().setEmit(nfeEmit);
			}

			if (dest != null) {
				this.nfeProc.getNfe().setInfNFe(infNFe);
				this.nfeProc.getNfe().getInfNFe().setDest(dest);
			}
		}

		return this.nfeProc;
	}

	private Object getObject(String expression, QName tipo) {
		try {
			return xpath.compile(expression).evaluate(document, tipo);
		} catch (XPathExpressionException e) {
			throw new RuntimeException("Erro ao tentar processar XML");
		}
	}

	public static String extractTextValue(NodeList aList) {
		return extractTextValue(aList, null);
	}

	public static String extractTextValue(NodeList aList, String aDefaultValue) {
		if (aList == null || aList.getLength() == 0) {
			return aDefaultValue;
		}
		return aList.item(0).getTextContent();
	}

}
