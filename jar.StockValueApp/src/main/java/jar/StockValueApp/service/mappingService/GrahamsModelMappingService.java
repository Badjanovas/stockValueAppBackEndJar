package jar.StockValueApp.service.mappingService;


import jar.StockValueApp.dto.GrahamsRequestDTO;
import jar.StockValueApp.dto.GrahamsResponseDTO;
import jar.StockValueApp.model.GrahamsModel;
import jar.StockValueApp.service.calculationService.GrahamsMethodCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GrahamsModelMappingService {

    private final GrahamsMethodCalculationService service;

    public GrahamsModel mapToEntity(final GrahamsRequestDTO requestDTO) {
        return GrahamsModel.builder()
                .name(requestDTO.getCompanyName())
                .ticker(requestDTO.getCompanyTicker())
                .eps(requestDTO.getEps())
                .growthRate(requestDTO.getGrowthRate())
                .currentYieldOfBonds(requestDTO.getCurrentYieldOfBonds())
                .intrinsicValue(service.calculateGrahamsValuation(requestDTO))
                .build();
    }

    public List<GrahamsResponseDTO> mapToResponse(final List<GrahamsModel> grahamModelValuations) {
        List<GrahamsResponseDTO> mappedGrahamValuations = new ArrayList<>();
        for (GrahamsModel grahamsValuation : grahamModelValuations) {
            GrahamsResponseDTO dto = GrahamsResponseDTO.builder()
                    .id(grahamsValuation.getId())
                    .companyName(grahamsValuation.getName())
                    .companyTicker(grahamsValuation.getTicker())
                    .eps(grahamsValuation.getEps())
                    .growthRate(grahamsValuation.getGrowthRate())
                    .currentYieldOfBonds(grahamsValuation.getCurrentYieldOfBonds())
                    .intrinsicValue(grahamsValuation.getIntrinsicValue())
                    .creationDate(grahamsValuation.getCreationDate())
                    .build();

            mappedGrahamValuations.add(dto);
        }
        return mappedGrahamValuations;
    }

    public GrahamsResponseDTO mapToResponse(final GrahamsModel grahamsValuation){
        return GrahamsResponseDTO.builder()
                .id(grahamsValuation.getId())
                .companyName(grahamsValuation.getName())
                .companyTicker(grahamsValuation.getTicker())
                .eps(grahamsValuation.getEps())
                .growthRate(grahamsValuation.getGrowthRate())
                .currentYieldOfBonds(grahamsValuation.getCurrentYieldOfBonds())
                .intrinsicValue(grahamsValuation.getIntrinsicValue())
                .creationDate(grahamsValuation.getCreationDate())
                .build();
    }



}
