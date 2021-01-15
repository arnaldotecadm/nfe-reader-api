package br.com.asoft.nfereader.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.xml.sax.SAXException;

import br.com.asoft.nfereader.message.ResponseMessage;
import br.com.asoft.nfereader.model.FileInfo;
import br.com.asoft.nfereader.model.NfeProc;
import br.com.asoft.nfereader.service.FilesStorageService;
import br.com.asoft.nfereader.service.NfeProcessor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
@RequestMapping("nfe")
@CrossOrigin("*")
public class NfeController {

	private List<NfeProc> nfeProcList = new ArrayList<>();

	@GetMapping("ping")
	public String ping() {
		return "ok-nfe";
	}

	@GetMapping("")
	public List<NfeProc> getAll() {
		return this.nfeProcList;
	}

	@GetMapping("clear")
	public String clear() {
		this.nfeProcList.clear();
		return "Lista limpa com Sucesso";
	}

	@PostMapping("processar")
	public Object processarNFe(@RequestParam("file") MultipartFile file) {
		try{
			NfeProc nfeProc = nfeProcessor.process(file);
			this.nfeProcList.add(nfeProc);
			return ResponseEntity.ok(nfeProc);
		}catch (Exception e){
			System.out.println("Não foi possível processar a NFe: " + e.getMessage());
			return ResponseEntity.status(400).body(e.getMessage());
		}

	}

	@Autowired
	FilesStorageService storageService;

	@Autowired
	private NfeProcessor nfeProcessor;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message;
		try {
			storageService.save(file);

			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(NfeController.class, "getFile", path.getFileName().toString()).build().toString();

			return new FileInfo(filename, url);
		}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@PathVariable String filename) {
		Resource file = storageService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/analise-quantitativa")
	public QuantityAnalisis analiseQuantitativa() {
		QuantityAnalisis qa = new QuantityAnalisis();
		
		List<TpAmbienteAnalisis> tpAmbienteList = processarTipoAmbiente();
		List<TpAmbienteAnalisis> ufList = processarUFDest();
		List<TpAmbienteAnalisis> natOperacaoList = processarNatOperacao();
		long nfeNaoProcessaveis = getTotalNaoProcessavel();

		qa.setQtdNfe(this.nfeProcList.size());
		qa.setNfeNaoProcessavel(nfeNaoProcessaveis);
		qa.setTpAmbiente(tpAmbienteList);
		qa.setUfDestList(ufList);
		qa.setNatOperacaoList(natOperacaoList);

		return qa;
	}

	public long getTotalNaoProcessavel() {
		return this.nfeProcList.stream().filter( item -> item.getProtNFe().getCStat() == null).count();
	}

	public List<TpAmbienteAnalisis> processarTipoAmbiente() {
		List<TpAmbienteAnalisis> tpAmbienteList = new ArrayList<>();

		for (NfeProc item : this.nfeProcList) {
			// neste caso não é uma nota de emissao possivelmente sera outro tipo de ocorrencia i.e inutilizacao
			if(null == item.getProtNFe().getCStat()){
				continue;
			}
			String tpAmb = item.getProtNFe().getTpAmb();

			populateArray(tpAmbienteList, tpAmb);

		}
		return tpAmbienteList;
	}
	
	public List<TpAmbienteAnalisis> processarUFDest() {
		List<TpAmbienteAnalisis> tpAmbienteList = new ArrayList<>();

		for (NfeProc item : this.nfeProcList) {
			// neste caso não é uma nota de emissao possivelmente sera outro tipo de ocorrencia i.e inutilizacao
			if(null == item.getProtNFe().getCStat()){
				continue;
			}
			String tpAmb = item.getNfe().getInfNFe().getDest().getEnderEmit().getUF();

			populateArray(tpAmbienteList, tpAmb);

		}
		return tpAmbienteList;
	}

	public List<TpAmbienteAnalisis> processarNatOperacao() {
		List<TpAmbienteAnalisis> tpAmbienteList = new ArrayList<>();

		for (NfeProc item : this.nfeProcList) {
			// neste caso não é uma nota de emissao possivelmente sera outro tipo de ocorrencia i.e inutilizacao
			if(null == item.getProtNFe().getCStat()){
				continue;
			}
			String tpAmb = item.getNfe().getInfNFe().getIde().getNatOp();

			populateArray(tpAmbienteList, tpAmb);

		}
		return tpAmbienteList;
	}

	private void populateArray(List<TpAmbienteAnalisis> tpAmbienteList, String tpAmb) {
		Optional<TpAmbienteAnalisis> findTpAmbiente = tpAmbienteList.stream()
				.filter(tp -> tp.getName().equalsIgnoreCase(tpAmb)).findAny();
		if (findTpAmbiente.isPresent()) {
			findTpAmbiente.get().addCount();
		} else {
			tpAmbienteList.add(new TpAmbienteAnalisis(tpAmb));
		}
	}
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class QuantityAnalisis {
	private int qtdNfe;
	private long nfeNaoProcessavel;
	private List<TpAmbienteAnalisis> tpAmbiente;
	private List<TpAmbienteAnalisis> ufDestList;
	private List<TpAmbienteAnalisis> natOperacaoList;
}

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class TpAmbienteAnalisis {
	@Include
	private String name;
	private int qtd;

	public TpAmbienteAnalisis(String name) {
		this.name = name;
		this.qtd = 1;
	}

	public void addCount() {
		this.qtd++;
	}
}
