package org.cbioportal.domain.clinical_data.usecase;

import org.cbioportal.domain.clinical_data.repository.ClinicalDataRepository;
import org.cbioportal.legacy.model.ClinicalData;
import org.cbioportal.domain.studyview.StudyViewFilterContext;
import org.cbioportal.legacy.web.parameter.ClinicalDataIdentifier;
import org.cbioportal.legacy.web.parameter.ClinicalDataMultiStudyFilter;
import org.cbioportal.shared.enums.ProjectionType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Use case for retrieving clinical data for a patient from the repository.
 * This class encapsulates the business logic for fetching clinical data based on
 * the provided study view filter context and filtered attributes.
 */
@Service
@Profile("clickhouse")
public class GetPatientClinicalDataUseCase {

    private final ClinicalDataRepository clinicalDataRepository;

    /**
     * Constructs a {@code GetPatientClinicalDataUseCase} with the provided repository.
     *
     * @param clinicalDataRepository the repository to be used for fetching patient clinical data
     */
    public GetPatientClinicalDataUseCase(ClinicalDataRepository clinicalDataRepository) {
        this.clinicalDataRepository = clinicalDataRepository;
    }

    /**
     * Executes the use case to retrieve clinical data for a patient.
     *
     * @param studyViewFilterContext the context of the study view filter to apply
     * @param filteredAttributes a list of attributes to filter the clinical data
     * @return a list of {@link ClinicalData} representing the patient's clinical data
     */
    public List<ClinicalData> execute(StudyViewFilterContext studyViewFilterContext, List<String> filteredAttributes) {
        return clinicalDataRepository.getPatientClinicalData(studyViewFilterContext, filteredAttributes);
    }
    
    public List<ClinicalData> execute(ClinicalDataMultiStudyFilter clinicalDataMultiStudyFilter, List<String> attributeIds, ProjectionType projectionType) {
        List<String> studyIds = new ArrayList<>();
        List<String> patientIds = new ArrayList<>();
        for (ClinicalDataIdentifier identifier : clinicalDataMultiStudyFilter.getIdentifiers()) {
            studyIds.add(identifier.getStudyId());
            patientIds.add(identifier.getEntityId());
        }

        // DETAILED level
        if (projectionType == ProjectionType.DETAILED) {
            return clinicalDataRepository.getPatientClinicalDataDetailed(studyIds, patientIds, attributeIds);
        }

        // ID or SUMMARY level
        List<ClinicalData> clinicalDataList = clinicalDataRepository.getPatientClinicalDataSummary(studyIds, patientIds, attributeIds);
        // ID level doesn't have attrValue
        if (projectionType == ProjectionType.ID) {
            for (ClinicalData clinicalData : clinicalDataList) {
                clinicalData.setAttrValue(null);
            }
        }
        return clinicalDataList;
    }
}

