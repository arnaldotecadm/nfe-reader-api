package br.com.asoft.nfereader.analise;

import br.com.asoft.nfereader.model.ItemAnalise;

import java.util.List;
import java.util.Optional;

public class AnaliseUtil {

    public void populateArrayWithString(List<ItemAnalise> tpAmbienteList, String tpAmb) {
        Optional<ItemAnalise> findTpAmbiente = tpAmbienteList.stream()
                .filter(tp -> tp.getName().equalsIgnoreCase(tpAmb)).findAny();
        if (findTpAmbiente.isPresent()) {
            findTpAmbiente.get().addCount();
        } else {
            tpAmbienteList.add(new ItemAnalise(tpAmb));
        }
    }
}
