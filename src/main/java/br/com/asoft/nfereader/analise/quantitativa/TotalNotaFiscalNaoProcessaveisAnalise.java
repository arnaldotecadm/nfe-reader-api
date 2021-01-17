package br.com.asoft.nfereader.analise.quantitativa;

import br.com.asoft.nfereader.analise.AnaliseQuantitativaInterface;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;

import java.util.ArrayList;
import java.util.List;

public class TotalNotaFiscalNaoProcessaveisAnalise implements AnaliseQuantitativaInterface {

    @Override
    public List<ItemAnalise> processar(List<NfeProc> lista) {
        List<ItemAnalise> tpAmbienteList = new ArrayList<>();

        Long total = lista.stream().filter(item -> item.getProtNFe().getCStat() == null).count();
        tpAmbienteList.add(new ItemAnalise("Total de Notas Fiscais Não Processaveis", total.intValue()));

        return tpAmbienteList;
    }

    @Override
    public String getNomeAnalise() {
        return "totalNotaFiscaslNaoProcessavel";
    }
}
