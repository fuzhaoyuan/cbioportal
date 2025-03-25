package org.cbioportal.domain.alteration.repository;

import org.cbioportal.legacy.model.AlterationCountByGene;
import org.cbioportal.legacy.model.CopyNumberCountByGene;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.domain.studyview.StudyViewFilterContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AlterationRepository {

    /**
     * Retrieves a list of mutated genes along with their alteration counts based on the given study view filter context.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @return A list of {@link AlterationCountByGene} representing mutated genes and their counts.
     */
    List<AlterationCountByGene> getMutatedGenes(StudyViewFilterContext studyViewFilterContext);

    /**
     * Retrieves a list of structural variant genes along with their alteration counts based on the given study view filter context.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @return A list of {@link AlterationCountByGene} representing structural variant genes and their counts.
     */
    List<AlterationCountByGene> getStructuralVariantGenes(StudyViewFilterContext studyViewFilterContext);

    /**
     * Retrieves a list of copy number alteration (CNA) genes along with their alteration counts based on the given study view filter context.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @return A list of {@link CopyNumberCountByGene} representing CNA genes and their counts.
     */
    List<CopyNumberCountByGene> getCnaGenes(StudyViewFilterContext studyViewFilterContext);

    /**
     * Retrieves the total number of profiled samples for a specific alteration type based on the given study view filter context,
     * grouped by study ID.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @param alterationType The type of alteration (e.g., MUTATION, CNA, SV).
     * @return A map where the key is the study ID and the value is the total number of profiled samples for that study.
     */
    Map<String, Integer> getTotalProfiledCountsByAlterationType(StudyViewFilterContext studyViewFilterContext, String alterationType);

    /**
     * Retrieves the total number of profiled samples categorized by gene, study ID, and alteration type.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @param alterationType         The type of alteration (e.g., MUTATION, CNA, SV).
     * @param molecularProfiles      A list of molecular profiles to consider.
     * @return A nested map where the outer key is the Hugo gene symbol, the inner key is the study ID,
     *         and the value is the count of profiled samples for that gene in that study.
     */
    Map<String, Map<String, Integer>> getTotalProfiledCounts(StudyViewFilterContext studyViewFilterContext,
                                                             String alterationType, List<MolecularProfile> molecularProfiles);

    /**
     * Retrieves a mapping of alteration types to the corresponding gene panel IDs that match the given study view filter context.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @param alterationType The type of alteration (e.g., MUTATION, CNA, SV).
     * @return A map where the key is the alteration type and the value is a set of matching gene panel IDs.
     */
    Map<String, Set<String>> getMatchingGenePanelIds(StudyViewFilterContext studyViewFilterContext,
                                                     String alterationType);

    /**
     * Retrieves the count of sample profiles that do not have associated gene panel data (WES data)
     * for a given alteration type, grouped by study ID.
     *
     * @param studyViewFilterContext The filter criteria for the study view.
     * @param alterationType The type of alteration (e.g., MUTATION, CNA, SV).
     * @return A map where the key is the study ID and the value is the number of sample profiles
     *         without gene panel data (WES) for that study.
     */
    Map<String, Integer> getSampleProfileCountWithoutPanelData(StudyViewFilterContext studyViewFilterContext, String alterationType);
}

