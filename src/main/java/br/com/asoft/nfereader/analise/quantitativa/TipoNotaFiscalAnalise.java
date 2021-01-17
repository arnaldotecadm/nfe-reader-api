package br.com.asoft.nfereader.analise.quantitativa;

import br.com.asoft.nfereader.analise.AnaliseQuantitativaInterface;
import br.com.asoft.nfereader.analise.AnaliseUtil;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;

import java.util.ArrayList;
import java.util.List;

public class TipoNotaFiscalAnalise implements AnaliseQuantitativaInterface {

    AnaliseUtil analiseUtil = new AnaliseUtil();

    @Override
    public List<ItemAnalise> processar(List<NfeProc> lista) {

        List<ItemAnalise> tpAmbienteList = new ArrayList<>();

        lista.stream()
                .filter(item -> null != item.getProtNFe().getCStat())
                .map(item -> item.getNfe().getInfNFe().getIde().getTpNF())
                .forEach(data -> analiseUtil.populateArrayWithString(tpAmbienteList, data));

        return tpAmbienteList;

    }

    @Override
    public String getNomeAnalise() {
        return "tipoNotaFiscal";
    }
}
