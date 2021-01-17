package br.com.asoft.nfereader.service;

import br.com.asoft.nfereader.analise.AnaliseQuantitativaInterface;
import br.com.asoft.nfereader.model.ItemAnalise;
import br.com.asoft.nfereader.model.nfe.NfeProc;
import org.reflections.Reflections;

import java.util.*;

public class NFeService {

    public Object getAnaliseQuantitativa(List<NfeProc> nfeProcList) {

        List<Map<String, List<ItemAnalise>>> retornoList = new ArrayList<>();

        Reflections reflections = new Reflections("br.com.asoft.nfereader.analise");
        Set<Class<? extends AnaliseQuantitativaInterface>> annotatedWithList = reflections.getSubTypesOf(AnaliseQuantitativaInterface.class);

        annotatedWithList.forEach(item -> {
            try {
                AnaliseQuantitativaInterface newInstance = (AnaliseQuantitativaInterface) Class.forName(item.getName()).newInstance();
                Map<String, List<ItemAnalise>> resultado = new HashMap<>();
                resultado.put(newInstance.getNomeAnalise(), newInstance.processar(nfeProcList));
                retornoList.add(resultado);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return retornoList;
    }
}
