package br.com.felmanc.apitransacao.services;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.com.felmanc.apitransacao.dtos.EstatisticaDTO;
import br.com.felmanc.apitransacao.dtos.TransacaoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstatisticaService {

	private final TransacoesService transacoesService;

	public EstatisticaDTO calcularEstatistica(Long intervaloSegundos) {
		log.info("Iniciando cálculo de estatísticas para intervalo de {} segundos", intervaloSegundos);

		List<TransacaoDTO> transacoesFiltradas = transacoesService.filtrarTransacoes(intervaloSegundos);

		if(transacoesFiltradas.isEmpty()) {
			log.info("Não foram encontradas transações dentro do intervalo");
			
			return new EstatisticaDTO(0l, 0.0, 0.0, 0.0, 0.0);
		}

		log.info("Foram encontradas {} transações dentro do intervalo", transacoesFiltradas.size());
		
		
		DoubleSummaryStatistics estatisticas = transacoesFiltradas.stream()
				.collect(Collectors.summarizingDouble(TransacaoDTO::valor));

		EstatisticaDTO resultado = new EstatisticaDTO(estatisticas.getCount(), estatisticas.getSum(),
				estatisticas.getAverage(), estatisticas.getMin(), estatisticas.getMax());

		log.info("Estatísticas calculadas: Quantidade={}, Soma={}, Média={}, Mínimo={}, Máximo={}", resultado.count(),
				resultado.sum(), resultado.avg(), resultado.min(), resultado.max());

		return resultado;
	}
}
