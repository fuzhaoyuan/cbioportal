package org.cbioportal.infrastructure.repository.clickhouse.alteration;

import org.cbioportal.domain.alteration.repository.AlterationRepository;
import org.cbioportal.domain.alteration.ProfiledCountByStudy;
import org.cbioportal.legacy.model.AlterationCountByGene;
import org.cbioportal.legacy.model.CopyNumberCountByGene;
import org.cbioportal.legacy.model.GenePanelToGene;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.legacy.persistence.helper.AlterationFilterHelper;
import org.cbioportal.domain.studyview.StudyViewFilterContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@Profile("clickhouse")
public class ClickhouseAlterationRepository implements AlterationRepository {

    private final ClickhouseAlterationMapper mapper;

    public ClickhouseAlterationRepository(ClickhouseAlterationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<AlterationCountByGene> getMutatedGenes(StudyViewFilterContext studyViewFilterContext) {
        return mapper.getMutatedGenes(studyViewFilterContext,
                AlterationFilterHelper.build(studyViewFilterContext.alterationFilter()));
    }

    @Override
    public List<AlterationCountByGene> getStructuralVariantGenes(StudyViewFilterContext studyViewFilterContext) {
        return mapper.getStructuralVariantGenes(studyViewFilterContext, AlterationFilterHelper.build(studyViewFilterContext.alterationFilter()));
    }

    @Override
    public List<CopyNumberCountByGene> getCnaGenes(StudyViewFilterContext studyViewFilterContext) {
        return mapper.getCnaGenes(studyViewFilterContext, AlterationFilterHelper.build(studyViewFilterContext.alterationFilter()));
    }

    @Override
    public Map<String, Integer> getTotalProfiledCountsByAlterationType(StudyViewFilterContext studyViewFilterContext, String alterationType) {
        return mapper.getTotalProfiledCountByAlterationType(studyViewFilterContext, alterationType).stream().collect(Collectors.toMap(
            ProfiledCountByStudy::getStudyId,
            ProfiledCountByStudy::getProfiledCount
        ));
    }

    @Override
    public Map<String, Map<String, Integer>> getTotalProfiledCounts(StudyViewFilterContext studyViewFilterContext, String alterationType, List<MolecularProfile> molecularProfiles) {
        List<AlterationCountByGene> counts = mapper.getTotalProfiledCounts(studyViewFilterContext, alterationType, molecularProfiles);
        Map<String, Map<String, Integer>> geneToStudyProfiledCountMap = new HashMap<>();
        for (AlterationCountByGene count : counts) {
            String hugoGeneSymbol = count.getHugoGeneSymbol();
            String studyId = count.getStudyId();
            int profiledCount = count.getNumberOfProfiledCases();

            geneToStudyProfiledCountMap
                .computeIfAbsent(hugoGeneSymbol, k -> new HashMap<>())
                .put(studyId, profiledCount);
        }
        return geneToStudyProfiledCountMap;
    }

    @Override
    public Map<String, Set<String>> getMatchingGenePanelIds(StudyViewFilterContext studyViewFilterContext, String alterationType) {
        return mapper.getMatchingGenePanelIds(studyViewFilterContext, alterationType)
                .stream()
                .collect(Collectors.groupingBy(GenePanelToGene::getHugoGeneSymbol,
                        Collectors.mapping(GenePanelToGene::getGenePanelId, Collectors.toSet())));
    }

    @Override
    public Map<String, Integer> getSampleProfileCountWithoutPanelData(StudyViewFilterContext studyViewFilterContext, String alterationType) {
        return mapper.getSampleProfileCountWithoutPanelData(studyViewFilterContext, alterationType).stream().collect(Collectors.toMap(
            ProfiledCountByStudy::getStudyId,
            ProfiledCountByStudy::getProfiledCount
        ));
    }
}
