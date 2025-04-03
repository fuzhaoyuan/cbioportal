package org.cbioportal.infrastructure.repository.clickhouse.clinical_data;

import org.cbioportal.domain.clinical_data.repository.ClinicalDataRepository;
import org.cbioportal.legacy.model.ClinicalData;
import org.cbioportal.legacy.model.ClinicalDataCountItem;
import org.cbioportal.domain.studyview.StudyViewFilterContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
@Profile("clickhouse")
public class ClickhouseClinicalDataRepository implements ClinicalDataRepository {

    private static final List<String> FILTERED_CLINICAL_ATTR_VALUES = Collections.emptyList();

    private final ClickhouseClinicalDataMapper mapper;

    public ClickhouseClinicalDataRepository(ClickhouseClinicalDataMapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<ClinicalData> getPatientClinicalData(StudyViewFilterContext studyViewFilterContext, List<String> filteredAttributes) {
        return mapper.getPatientClinicalDataFromStudyViewFilter(studyViewFilterContext, filteredAttributes);
    }
    
    @Override
    public List<ClinicalData> getPatientClinicalDataSummary(List<String> studyIds, List<String> patientIds, List<String> attributeIds) {
        return mapper.getPatientClinicalDataSummary(studyIds, patientIds, attributeIds);
    }

    @Override
    public List<ClinicalData> getPatientClinicalDataDetailed(List<String> studyIds, List<String> patientIds, List<String> attributeIds) {
        return mapper.getPatientClinicalDataDetailed(studyIds, patientIds, attributeIds);
    }

    @Override
    public List<ClinicalData> getSampleClinicalData(StudyViewFilterContext studyViewFilterContext, List<String> filteredAttributes) {
        return mapper.getSampleClinicalDataFromStudyViewFilter(studyViewFilterContext, filteredAttributes);
    }

    @Override
    public List<ClinicalData> getSampleClinicalDataSummary(List<String> studyIds, List<String> sampleIds, List<String> attributeIds) {
        return mapper.getSampleClinicalDataSummary(studyIds, sampleIds, attributeIds);
    }

    @Override
    public List<ClinicalData> getSampleClinicalDataDetailed(List<String> studyIds, List<String> sampleIds, List<String> attributeIds) {
        return mapper.getSampleClinicalDataDetailed(studyIds, sampleIds, attributeIds);
    }

    @Override
    public List<ClinicalDataCountItem> getClinicalDataCounts(StudyViewFilterContext studyViewFilterContext, List<String> filteredAttributes) {
        return mapper.getClinicalDataCounts(studyViewFilterContext, filteredAttributes, FILTERED_CLINICAL_ATTR_VALUES);
    }
}
