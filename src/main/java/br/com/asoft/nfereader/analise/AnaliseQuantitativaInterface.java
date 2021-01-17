package br.com.asoft.nfereader.analise;

import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;

import java.util.List;

public interface AnaliseQuantitativaInterface {
    List<ItemAnalise> processar(List<NfeProc> lista);
    String getNomeAnalise();
}
