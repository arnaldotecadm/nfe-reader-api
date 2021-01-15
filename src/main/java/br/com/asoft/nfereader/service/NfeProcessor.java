package br.com.asoft.nfereader.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.internal.util.SimpleNamespaceResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.asoft.nfereader.builder.NFeBuilder;
import br.com.asoft.nfereader.model.NfeProc;

@Service
public class NfeProcessor {
	Log log = LogFactory.getLog(NfeProcessor.class);

	public Object process(String conteudo, String expressao, int tipo)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//		docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		InputStream inputStream = new ByteArrayInputStream(conteudo.getBytes(StandardCharsets.UTF_8));
		Document document = docBuilder.parse(inputStream);
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new SimpleNamespaceResolver("xmlns", "http://www.portalfiscal.inf.br/nfe"));

		System.out.println("Conteudo: " + conteudo);
		System.out.println("Expressao: " + expressao);

		if (tipo == 1) {
			return ((NodeList) xpath.compile(expressao).evaluate(document, XPathConstants.NODESET)).getLength();

		}
		if (tipo == 2) {
			return xpath.compile(expressao).evaluate(document, XPathConstants.NODE);
		}
		if (tipo == 3) {
			return xpath.compile(expressao).evaluate(document, XPathConstants.STRING);
		} else {
			return "Nenhum valor informado";
		}

	}

	public NfeProc process(MultipartFile fileToBeProcessed)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document document = docBuilder.parse(fileToBeProcessed.getInputStream(), "UTF-8");

		return new NFeBuilder(document)
				.comNFe()
				.comInfNfe()
				.comIde()
				.comEmit()
				.comDest()
				.comProtNFe()
				.build();
	}
}
