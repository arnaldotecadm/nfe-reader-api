package br.com.asoft.nfereader.service;

import br.com.asoft.nfereader.builder.NFeBuilder;
import br.com.asoft.nfereader.model.nfe.NfeProc;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
public class NfeProcessor {
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
