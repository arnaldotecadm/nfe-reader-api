package br.com.asoft.nfereader.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.asoft.nfereader.model.FileInfo;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.QuantityAnalisis;
import br.com.asoft.nfereader.service.NFeService;
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

import br.com.asoft.nfereader.message.ResponseMessage;
import br.com.asoft.nfereader.model.nfe.NfeProc;
import br.com.asoft.nfereader.service.FilesStorageService;
import br.com.asoft.nfereader.service.NfeProcessor;

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
        try {
            NfeProc nfeProc = nfeProcessor.process(file);
            this.nfeProcList.add(nfeProc);
            return ResponseEntity.ok(nfeProc);
        } catch (Exception e) {
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
    public Object analiseQuantitativa() {
        return new NFeService().getAnaliseQuantitativa(this.nfeProcList);
    }


}




