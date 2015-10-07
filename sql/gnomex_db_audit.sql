
USE gnomex;

delimiter $$


DROP TRIGGER IF EXISTS TrAI_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAI_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentProfile_FER
$$
DROP TRIGGER IF EXISTS TrAI_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAU_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAD_AlignmentProfileGenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAI_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAU_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAD_Analysis_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisExperimentItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGroup_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisGroupItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAI_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAU_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAD_AnnotationReportField_FER
$$
DROP TRIGGER IF EXISTS TrAI_Application_FER
$$
DROP TRIGGER IF EXISTS TrAU_Application_FER
$$
DROP TRIGGER IF EXISTS TrAD_Application_FER
$$
DROP TRIGGER IF EXISTS TrAI_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAU_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAD_ApplicationTheme_FER
$$
DROP TRIGGER IF EXISTS TrAI_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ApplicationType_FER
$$
DROP TRIGGER IF EXISTS TrAI_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_AppUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAU_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAD_ArrayCoordinate_FER
$$
DROP TRIGGER IF EXISTS TrAI_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_ArrayDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAU_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAD_Assay_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingAccount_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingAccountUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingChargeKind_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingPeriod_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingSlideProductClass_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingSlideServiceClass_FER
$$
DROP TRIGGER IF EXISTS TrAI_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_BillingStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAU_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAD_BioanalyzerChipType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAU_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAD_Chromatogram_FER
$$
DROP TRIGGER IF EXISTS TrAI_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAU_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAD_ConcentrationUnit_FER
$$
DROP TRIGGER IF EXISTS TrAI_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAU_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAD_ContextSensitiveHelp_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacility_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilityLab_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilityManager_FER
$$
DROP TRIGGER IF EXISTS TrAI_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAU_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAD_CoreFacilitySubmitter_FER
$$
DROP TRIGGER IF EXISTS TrAI_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAU_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAD_CreditCardCompany_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackFolder_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackToFolder_FER
$$
DROP TRIGGER IF EXISTS TrAI_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_DataTrackToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAU_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAD_DiskUsageByMonth_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentDesignEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFactor_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFactorEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_ExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAU_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAD_FAQ_FER
$$
DROP TRIGGER IF EXISTS TrAI_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_FeatureExtractionProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAU_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAD_FlowCell_FER
$$
DROP TRIGGER IF EXISTS TrAI_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAU_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAD_FlowCellChannel_FER
$$
DROP TRIGGER IF EXISTS TrAI_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAU_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAD_FundingAgency_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeBuild_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeBuildAlias_FER
$$
DROP TRIGGER IF EXISTS TrAI_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAU_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAD_GenomeIndex_FER
$$
DROP TRIGGER IF EXISTS TrAI_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_HybProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAU_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAD_Hybridization_FER
$$
DROP TRIGGER IF EXISTS TrAI_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAU_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAD_Institution_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstitutionLab_FER
$$
DROP TRIGGER IF EXISTS TrAI_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAU_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAD_Instrument_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstrumentRun_FER
$$
DROP TRIGGER IF EXISTS TrAI_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_InstrumentRunStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAU_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAD_InternalAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAI_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAU_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAD_Invoice_FER
$$
DROP TRIGGER IF EXISTS TrAI_IScanChip_FER
$$
DROP TRIGGER IF EXISTS TrAU_IScanChip_FER
$$
DROP TRIGGER IF EXISTS TrAD_IScanChip_FER
$$
DROP TRIGGER IF EXISTS TrAI_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAU_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAD_IsolationPrepType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAU_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAD_Lab_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_Label_FER
$$
DROP TRIGGER IF EXISTS TrAU_Label_FER
$$
DROP TRIGGER IF EXISTS TrAD_Label_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabeledSample_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabelingProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabelingReactionSize_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabManager_FER
$$
DROP TRIGGER IF EXISTS TrAI_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_LabUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAU_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAD_MetrixObject_FER
$$
DROP TRIGGER IF EXISTS TrAI_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_NewsItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAU_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAD_Notification_FER
$$
DROP TRIGGER IF EXISTS TrAI_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAU_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAD_NucleotideType_FER
$$
DROP TRIGGER IF EXISTS TrAI_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAU_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAD_NumberSequencingCycles_FER
$$
DROP TRIGGER IF EXISTS TrAI_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAU_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAD_NumberSequencingCyclesAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcode_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcodeScheme_FER
$$
DROP TRIGGER IF EXISTS TrAI_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAU_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAD_OligoBarcodeSchemeAllowed_FER
$$
DROP TRIGGER IF EXISTS TrAI_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAU_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAD_Organism_FER
$$
DROP TRIGGER IF EXISTS TrAI_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAU_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAD_OtherAccountFieldsConfiguration_FER
$$
DROP TRIGGER IF EXISTS TrAI_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAU_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAD_Plate_FER
$$
DROP TRIGGER IF EXISTS TrAI_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PlateType_FER
$$
DROP TRIGGER IF EXISTS TrAI_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAU_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAD_PlateWell_FER
$$
DROP TRIGGER IF EXISTS TrAI_Price_FER
$$
DROP TRIGGER IF EXISTS TrAU_Price_FER
$$
DROP TRIGGER IF EXISTS TrAD_Price_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCategoryStep_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceCriteria_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheet_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheetPriceCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_PriceSheetRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAU_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAD_Primer_FER
$$
DROP TRIGGER IF EXISTS TrAI_Product_FER
$$
DROP TRIGGER IF EXISTS TrAU_Product_FER
$$
DROP TRIGGER IF EXISTS TrAD_Product_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductLedger_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductLineItem_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrder_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrderFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductOrderStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProductType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Project_FER
$$
DROP TRIGGER IF EXISTS TrAU_Project_FER
$$
DROP TRIGGER IF EXISTS TrAD_Project_FER
$$
DROP TRIGGER IF EXISTS TrAI_Property_FER
$$
DROP TRIGGER IF EXISTS TrAU_Property_FER
$$
DROP TRIGGER IF EXISTS TrAD_Property_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyAnalysisType_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyAppUser_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyDictionary_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntryOption_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyEntryValue_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyOption_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyOrganism_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyPlatformApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAU_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAD_PropertyType_FER
$$
DROP TRIGGER IF EXISTS TrAI_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ProtocolType_FER
$$
DROP TRIGGER IF EXISTS TrAI_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAU_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAD_QualityControlStep_FER
$$
DROP TRIGGER IF EXISTS TrAI_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_QualityControlStepEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAU_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAD_ReactionType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Request_FER
$$
DROP TRIGGER IF EXISTS TrAU_Request_FER
$$
DROP TRIGGER IF EXISTS TrAD_Request_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategoryApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCategoryType_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestCollaborator_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestHybridization_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestSeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestStatus_FER
$$
DROP TRIGGER IF EXISTS TrAI_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAU_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAD_RequestToTopic_FER
$$
DROP TRIGGER IF EXISTS TrAI_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAU_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAD_Sample_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleDropOffLocation_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleExperimentFile_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleFileType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAU_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAD_SamplePrepMethod_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleSource_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAU_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAD_SampleTypeRequestCategory_FER
$$
DROP TRIGGER IF EXISTS TrAI_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_ScanProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SealType_FER
$$
DROP TRIGGER IF EXISTS TrAI_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAU_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAD_Segment_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibProtocol_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibProtocolApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqLibTreatment_FER
$$
DROP TRIGGER IF EXISTS TrAI_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAU_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAD_SeqRunType_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequenceLane_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequencingControl_FER
$$
DROP TRIGGER IF EXISTS TrAI_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAU_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAD_SequencingPlatform_FER
$$
DROP TRIGGER IF EXISTS TrAI_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAU_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAD_Slide_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideDesign_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideProduct_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideProductApplication_FER
$$
DROP TRIGGER IF EXISTS TrAI_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAU_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAD_SlideSource_FER
$$
DROP TRIGGER IF EXISTS TrAI_State_FER
$$
DROP TRIGGER IF EXISTS TrAU_State_FER
$$
DROP TRIGGER IF EXISTS TrAD_State_FER
$$
DROP TRIGGER IF EXISTS TrAI_Step_FER
$$
DROP TRIGGER IF EXISTS TrAU_Step_FER
$$
DROP TRIGGER IF EXISTS TrAD_Step_FER
$$
DROP TRIGGER IF EXISTS TrAI_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAU_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAD_SubmissionInstruction_FER
$$
DROP TRIGGER IF EXISTS TrAI_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAU_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAD_Topic_FER
$$
DROP TRIGGER IF EXISTS TrAI_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAU_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAD_TreatmentEntry_FER
$$
DROP TRIGGER IF EXISTS TrAI_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAU_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAD_UnloadDataTrack_FER
$$
DROP TRIGGER IF EXISTS TrAI_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAU_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAD_UserPermissionKind_FER
$$
DROP TRIGGER IF EXISTS TrAI_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAU_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAD_Vendor_FER
$$
DROP TRIGGER IF EXISTS TrAI_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAU_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAD_Visibility_FER
$$
DROP TRIGGER IF EXISTS TrAI_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAU_WorkItem_FER
$$
DROP TRIGGER IF EXISTS TrAD_WorkItem_FER
$$



--
-- Audit Table For AlignmentPlatform 
--

CREATE TABLE IF NOT EXISTS `AlignmentPlatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`alignmentPlatformName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AlignmentProfile 
--

CREATE TABLE IF NOT EXISTS `AlignmentProfile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`alignmentProfileName`  varchar(120)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`parameters`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAlignmentPlatform`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AlignmentProfileGenomeIndex 
--

CREATE TABLE IF NOT EXISTS `AlignmentProfileGenomeIndex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAlignmentProfile`  int(10)  NULL DEFAULT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Analysis 
--

CREATE TABLE IF NOT EXISTS `Analysis_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisCollaborator 
--

CREATE TABLE IF NOT EXISTS `AnalysisCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisExperimentItem 
--

CREATE TABLE IF NOT EXISTS `AnalysisExperimentItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisExperimentItem`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisFile 
--

CREATE TABLE IF NOT EXISTS `AnalysisFile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`comments`  varchar(2000)  NULL DEFAULT NULL
 ,`uploadDate`  datetime  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`baseFilePath`  varchar(300)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisGenomeBuild 
--

CREATE TABLE IF NOT EXISTS `AnalysisGenomeBuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisGroup 
--

CREATE TABLE IF NOT EXISTS `AnalysisGroup_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisGroupItem 
--

CREATE TABLE IF NOT EXISTS `AnalysisGroupItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisGroup`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisProtocol 
--

CREATE TABLE IF NOT EXISTS `AnalysisProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisProtocol`  int(10)  NULL DEFAULT NULL
 ,`analysisProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisToTopic 
--

CREATE TABLE IF NOT EXISTS `AnalysisToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnalysisType 
--

CREATE TABLE IF NOT EXISTS `AnalysisType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
 ,`analysisType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AnnotationReportField 
--

CREATE TABLE IF NOT EXISTS `AnnotationReportField_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAnnotationReportField`  int(10)  NULL DEFAULT NULL
 ,`source`  varchar(50)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`display`  varchar(50)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`dictionaryLookUpTable`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Application 
--

CREATE TABLE IF NOT EXISTS `Application_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`application`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`hasCaptureLibDesign`  char(1)  NULL DEFAULT NULL
 ,`coreSteps`  varchar(5000)  NULL DEFAULT NULL
 ,`coreStepsNoLibPrep`  varchar(5000)  NULL DEFAULT NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`onlyForLabPrepped`  char(1)  NULL DEFAULT NULL
 ,`samplesPerBatch`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`hasChipTypes`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ApplicationTheme 
--

CREATE TABLE IF NOT EXISTS `ApplicationTheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idApplicationTheme`  int(10)  NULL DEFAULT NULL
 ,`applicationTheme`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ApplicationType 
--

CREATE TABLE IF NOT EXISTS `ApplicationType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeApplicationType`  varchar(10)  NULL DEFAULT NULL
 ,`applicationType`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For AppUser 
--

CREATE TABLE IF NOT EXISTS `AppUser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`uNID`  varchar(50)  NULL DEFAULT NULL
 ,`email`  varchar(200)  NULL DEFAULT NULL
 ,`phone`  varchar(50)  NULL DEFAULT NULL
 ,`department`  varchar(100)  NULL DEFAULT NULL
 ,`institute`  varchar(100)  NULL DEFAULT NULL
 ,`jobTitle`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userNameExternal`  varchar(100)  NULL DEFAULT NULL
 ,`passwordExternal`  varchar(100)  NULL DEFAULT NULL
 ,`ucscUrl`  varchar(250)  NULL DEFAULT NULL
 ,`salt`  varchar(300)  NULL DEFAULT NULL
 ,`guid`  varchar(100)  NULL DEFAULT NULL
 ,`guidExpiration`  datetime  NULL DEFAULT NULL
 ,`passwordExpired`  char(1)  NULL DEFAULT NULL
 ,`confirmEmailGuid`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ArrayCoordinate 
--

CREATE TABLE IF NOT EXISTS `ArrayCoordinate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`x`  int(10)  NULL DEFAULT NULL
 ,`y`  int(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ArrayDesign 
--

CREATE TABLE IF NOT EXISTS `ArrayDesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idArrayDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`accessionNumberUArrayExpress`  varchar(100)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Assay 
--

CREATE TABLE IF NOT EXISTS `Assay_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingAccount 
--

CREATE TABLE IF NOT EXISTS `BillingAccount_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`accountName`  varchar(200)  NULL DEFAULT NULL
 ,`accountNumber`  varchar(100)  NULL DEFAULT NULL
 ,`expirationDate`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`accountNumberBus`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberOrg`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberFund`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberActivity`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberProject`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAccount`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberAu`  varchar(10)  NULL DEFAULT NULL
 ,`accountNumberYear`  varchar(10)  NULL DEFAULT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`isPO`  char(1)  NULL DEFAULT NULL
 ,`isCreditCard`  char(1)  NULL DEFAULT NULL
 ,`zipCode`  varchar(20)  NULL DEFAULT NULL
 ,`isApproved`  char(1)  NULL DEFAULT NULL
 ,`approvedDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`submitterEmail`  varchar(200)  NULL DEFAULT NULL
 ,`submitterUID`  varchar(50)  NULL DEFAULT NULL
 ,`totalDollarAmount`  decimal(12,2)  NULL DEFAULT NULL
 ,`purchaseOrderForm`  longblob  NULL DEFAULT NULL
 ,`orderFormFileType`  varchar(10)  NULL DEFAULT NULL
 ,`orderFormFileSize`  bigint(20)  NULL DEFAULT NULL
 ,`shortAcct`  varchar(10)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`custom1`  varchar(50)  NULL DEFAULT NULL
 ,`custom2`  varchar(50)  NULL DEFAULT NULL
 ,`custom3`  varchar(50)  NULL DEFAULT NULL
 ,`approverEmail`  varchar(200)  NULL DEFAULT NULL
 ,`idApprover`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingAccountUser 
--

CREATE TABLE IF NOT EXISTS `BillingAccountUser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingChargeKind 
--

CREATE TABLE IF NOT EXISTS `BillingChargeKind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`billingChargeKind`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingItem 
--

CREATE TABLE IF NOT EXISTS `BillingItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingItem`  int(10)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`category`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`invoicePrice`  decimal(9,2)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`percentagePrice`  decimal(4,3)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`completeDate`  datetime  NULL DEFAULT NULL
 ,`splitType`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
 ,`tag`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingPeriod 
--

CREATE TABLE IF NOT EXISTS `BillingPeriod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`billingPeriod`  varchar(50)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`endDate`  datetime  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingSlideProductClass 
--

CREATE TABLE IF NOT EXISTS `BillingSlideProductClass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideProductClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingSlideServiceClass 
--

CREATE TABLE IF NOT EXISTS `BillingSlideServiceClass_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
 ,`billingSlideServiceClass`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BillingStatus 
--

CREATE TABLE IF NOT EXISTS `BillingStatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBillingStatus`  varchar(10)  NULL DEFAULT NULL
 ,`billingStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For BioanalyzerChipType 
--

CREATE TABLE IF NOT EXISTS `BioanalyzerChipType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`bioanalyzerChipType`  varchar(100)  NULL DEFAULT NULL
 ,`concentrationRange`  varchar(50)  NULL DEFAULT NULL
 ,`maxSampleBufferStrength`  varchar(50)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`sampleWellsPerChip`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`protocolDescription`  longtext  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Chromatogram 
--

CREATE TABLE IF NOT EXISTS `Chromatogram_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idChromatogram`  int(10)  NULL DEFAULT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(200)  NULL DEFAULT NULL
 ,`readLength`  int(10)  NULL DEFAULT NULL
 ,`trimmedLength`  int(11)  NULL DEFAULT NULL
 ,`q20`  int(10)  NULL DEFAULT NULL
 ,`q40`  int(10)  NULL DEFAULT NULL
 ,`aSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`cSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`gSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`tSignalStrength`  int(10)  NULL DEFAULT NULL
 ,`releaseDate`  datetime  NULL DEFAULT NULL
 ,`qualifiedFilePath`  varchar(500)  NULL DEFAULT NULL
 ,`idReleaser`  int(10)  NULL DEFAULT NULL
 ,`lane`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ConcentrationUnit 
--

CREATE TABLE IF NOT EXISTS `ConcentrationUnit_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`concentrationUnit`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ContextSensitiveHelp 
--

CREATE TABLE IF NOT EXISTS `ContextSensitiveHelp_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idContextSensitiveHelp`  int(10)  NULL DEFAULT NULL
 ,`context1`  varchar(100)  NULL DEFAULT NULL
 ,`context2`  varchar(100)  NULL DEFAULT NULL
 ,`context3`  varchar(100)  NULL DEFAULT NULL
 ,`helpText`  varchar(10000)  NULL DEFAULT NULL
 ,`toolTipText`  varchar(10000)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For CoreFacility 
--

CREATE TABLE IF NOT EXISTS `CoreFacility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`facilityName`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`showProjectAnnotations`  char(1)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`acceptOnlineWorkAuth`  char(1)  NULL DEFAULT NULL
 ,`shortDescription`  varchar(1000)  NULL DEFAULT NULL
 ,`contactName`  varchar(200)  NULL DEFAULT NULL
 ,`contactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`contactPhone`  varchar(200)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`contactImage`  varchar(200)  NULL DEFAULT NULL
 ,`labPhone`  varchar(200)  NULL DEFAULT NULL
 ,`contactRoom`  varchar(200)  NULL DEFAULT NULL
 ,`labRoom`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For CoreFacilityLab 
--

CREATE TABLE IF NOT EXISTS `CoreFacilityLab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For CoreFacilityManager 
--

CREATE TABLE IF NOT EXISTS `CoreFacilityManager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For CoreFacilitySubmitter 
--

CREATE TABLE IF NOT EXISTS `CoreFacilitySubmitter_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For CreditCardCompany 
--

CREATE TABLE IF NOT EXISTS `CreditCardCompany_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idCreditCardCompany`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrack 
--

CREATE TABLE IF NOT EXISTS `DataTrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`summary`  varchar(5000)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`isLoaded`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrackCollaborator 
--

CREATE TABLE IF NOT EXISTS `DataTrackCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrackFile 
--

CREATE TABLE IF NOT EXISTS `DataTrackFile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFile`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisFile`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrackFolder 
--

CREATE TABLE IF NOT EXISTS `DataTrackFolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentDataTrackFolder`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrackToFolder 
--

CREATE TABLE IF NOT EXISTS `DataTrackToFolder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idDataTrackFolder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DataTrackToTopic 
--

CREATE TABLE IF NOT EXISTS `DataTrackToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For DiskUsageByMonth 
--

CREATE TABLE IF NOT EXISTS `DiskUsageByMonth_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idDiskUsageByMonth`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`asOfDate`  datetime  NULL DEFAULT NULL
 ,`lastCalcDate`  datetime  NULL DEFAULT NULL
 ,`totalAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedAnalysisDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`totalExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`assessedExperimentDiskSpace`  decimal(16,0)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ExperimentDesign 
--

CREATE TABLE IF NOT EXISTS `ExperimentDesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`experimentDesign`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ExperimentDesignEntry 
--

CREATE TABLE IF NOT EXISTS `ExperimentDesignEntry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentDesignEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentDesign`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ExperimentFactor 
--

CREATE TABLE IF NOT EXISTS `ExperimentFactor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`experimentFactor`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ExperimentFactorEntry 
--

CREATE TABLE IF NOT EXISTS `ExperimentFactorEntry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFactorEntry`  int(10)  NULL DEFAULT NULL
 ,`codeExperimentFactor`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ExperimentFile 
--

CREATE TABLE IF NOT EXISTS `ExperimentFile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For FAQ 
--

CREATE TABLE IF NOT EXISTS `FAQ_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFAQ`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(300)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For FeatureExtractionProtocol 
--

CREATE TABLE IF NOT EXISTS `FeatureExtractionProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`featureExtractionProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For FlowCell 
--

CREATE TABLE IF NOT EXISTS `FlowCell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`notes`  varchar(200)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`runNumber`  int(10)  NULL DEFAULT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`side`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For FlowCellChannel 
--

CREATE TABLE IF NOT EXISTS `FlowCellChannel_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idFlowCell`  int(10)  NULL DEFAULT NULL
 ,`number`  int(10)  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCyclesActual`  int(10)  NULL DEFAULT NULL
 ,`clustersPerTile`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(500)  NULL DEFAULT NULL
 ,`startDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleDate`  datetime  NULL DEFAULT NULL
 ,`firstCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`firstCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`lastCycleDate`  datetime  NULL DEFAULT NULL
 ,`lastCycleCompleted`  char(1)  NULL DEFAULT NULL
 ,`lastCycleFailed`  char(1)  NULL DEFAULT NULL
 ,`sampleConcentrationpM`  decimal(8,2)  NULL DEFAULT NULL
 ,`pipelineDate`  datetime  NULL DEFAULT NULL
 ,`pipelineFailed`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`phiXErrorRate`  decimal(4,4)  NULL DEFAULT NULL
 ,`read1ClustersPassedFilterM`  decimal(6,2)  NULL DEFAULT NULL
 ,`read2ClustersPassedFilterM`  int(10)  NULL DEFAULT NULL
 ,`q30Gb`  decimal(4,1)  NULL DEFAULT NULL
 ,`q30Percent`  decimal(4,3)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For FundingAgency 
--

CREATE TABLE IF NOT EXISTS `FundingAgency_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idFundingAgency`  int(10)  NULL DEFAULT NULL
 ,`fundingAgency`  varchar(200)  NULL DEFAULT NULL
 ,`isPeerReviewedFunding`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For GenomeBuild 
--

CREATE TABLE IF NOT EXISTS `GenomeBuild_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`genomeBuildName`  varchar(500)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isLatestBuild`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`buildDate`  datetime  NULL DEFAULT NULL
 ,`coordURI`  varchar(2000)  NULL DEFAULT NULL
 ,`coordVersion`  varchar(50)  NULL DEFAULT NULL
 ,`coordSource`  varchar(50)  NULL DEFAULT NULL
 ,`coordTestRange`  varchar(100)  NULL DEFAULT NULL
 ,`coordAuthority`  varchar(50)  NULL DEFAULT NULL
 ,`ucscName`  varchar(100)  NULL DEFAULT NULL
 ,`igvName`  varchar(50)  NULL DEFAULT NULL
 ,`dataPath`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For GenomeBuildAlias 
--

CREATE TABLE IF NOT EXISTS `GenomeBuildAlias_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeBuildAlias`  int(10)  NULL DEFAULT NULL
 ,`alias`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For GenomeIndex 
--

CREATE TABLE IF NOT EXISTS `GenomeIndex_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idGenomeIndex`  int(10)  NULL DEFAULT NULL
 ,`genomeIndexName`  varchar(120)  NULL DEFAULT NULL
 ,`webServiceName`  varchar(120)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For HybProtocol 
--

CREATE TABLE IF NOT EXISTS `HybProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Hybridization 
--

CREATE TABLE IF NOT EXISTS `Hybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocol`  int(10)  NULL DEFAULT NULL
 ,`idHybBuffer`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel1`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSampleChannel2`  int(10)  NULL DEFAULT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`idArrayCoordinate`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocol`  int(10)  NULL DEFAULT NULL
 ,`hybDate`  datetime  NULL DEFAULT NULL
 ,`hybFailed`  char(1)  NULL DEFAULT NULL
 ,`hybBypassed`  char(1)  NULL DEFAULT NULL
 ,`extractionDate`  datetime  NULL DEFAULT NULL
 ,`extractionFailed`  char(1)  NULL DEFAULT NULL
 ,`extractionBypassed`  char(1)  NULL DEFAULT NULL
 ,`hasResults`  char(1)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Institution 
--

CREATE TABLE IF NOT EXISTS `Institution_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`institution`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`isDefault`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For InstitutionLab 
--

CREATE TABLE IF NOT EXISTS `InstitutionLab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Instrument 
--

CREATE TABLE IF NOT EXISTS `Instrument_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrument`  int(10)  NULL DEFAULT NULL
 ,`instrument`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For InstrumentRun 
--

CREATE TABLE IF NOT EXISTS `InstrumentRun_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`runDate`  datetime  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(100)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For InstrumentRunStatus 
--

CREATE TABLE IF NOT EXISTS `InstrumentRunStatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeInstrumentRunStatus`  varchar(10)  NULL DEFAULT NULL
 ,`instrumentRunStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For InternalAccountFieldsConfiguration 
--

CREATE TABLE IF NOT EXISTS `InternalAccountFieldsConfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInternalAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`displayName`  varchar(50)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`isNumber`  char(1)  NULL DEFAULT NULL
 ,`minLength`  int(10)  NULL DEFAULT NULL
 ,`maxLength`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Invoice 
--

CREATE TABLE IF NOT EXISTS `Invoice_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idInvoice`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idBillingPeriod`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`invoiceNumber`  varchar(50)  NULL DEFAULT NULL
 ,`lastEmailDate`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For IScanChip 
--

CREATE TABLE IF NOT EXISTS `IScanChip_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`costPerSample`  decimal(5,2)  NULL DEFAULT NULL
 ,`samplesPerChip`  int(10)  NULL DEFAULT NULL
 ,`chipsPerKit`  int(10)  NULL DEFAULT NULL
 ,`markersPerSample`  varchar(100)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For IsolationPrepType 
--

CREATE TABLE IF NOT EXISTS `IsolationPrepType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`isolationPrepType`  varchar(100)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Lab 
--

CREATE TABLE IF NOT EXISTS `Lab_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`department`  varchar(200)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`contactName`  varchar(200)  NULL DEFAULT NULL
 ,`contactAddress`  varchar(200)  NULL DEFAULT NULL
 ,`contactCodeState`  varchar(10)  NULL DEFAULT NULL
 ,`contactZip`  varchar(50)  NULL DEFAULT NULL
 ,`contactCity`  varchar(200)  NULL DEFAULT NULL
 ,`contactPhone`  varchar(50)  NULL DEFAULT NULL
 ,`contactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`isCCSGMember`  char(1)  NULL DEFAULT NULL
 ,`firstName`  varchar(200)  NULL DEFAULT NULL
 ,`lastName`  varchar(200)  NULL DEFAULT NULL
 ,`isExternalPricing`  varchar(1)  NULL DEFAULT NULL
 ,`isExternalPricingCommercial`  char(1)  NULL DEFAULT NULL
 ,`isActive`  varchar(1)  NULL DEFAULT NULL
 ,`excludeUsage`  char(1)  NULL DEFAULT NULL
 ,`billingContactEmail`  varchar(200)  NULL DEFAULT NULL
 ,`version`  bigint(20)  NULL DEFAULT NULL
 ,`contactAddress2`  varchar(200)  NULL DEFAULT NULL
 ,`contactCountry`  varchar(200)  NULL DEFAULT NULL
 ,`billingContactPhone`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabCollaborator 
--

CREATE TABLE IF NOT EXISTS `LabCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Label 
--

CREATE TABLE IF NOT EXISTS `Label_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabeledSample 
--

CREATE TABLE IF NOT EXISTS `LabeledSample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`labelingYield`  decimal(8,2)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabel`  int(10)  NULL DEFAULT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`numberOfReactions`  int(10)  NULL DEFAULT NULL
 ,`labelingDate`  datetime  NULL DEFAULT NULL
 ,`labelingFailed`  char(1)  NULL DEFAULT NULL
 ,`labelingBypassed`  char(1)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabelingProtocol 
--

CREATE TABLE IF NOT EXISTS `LabelingProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLabelingProtocol`  int(10)  NULL DEFAULT NULL
 ,`labelingProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabelingReactionSize 
--

CREATE TABLE IF NOT EXISTS `LabelingReactionSize_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeLabelingReactionSize`  varchar(20)  NULL DEFAULT NULL
 ,`labelingReactionSize`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabManager 
--

CREATE TABLE IF NOT EXISTS `LabManager_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For LabUser 
--

CREATE TABLE IF NOT EXISTS `LabUser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sendUploadAlert`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For MetrixObject 
--

CREATE TABLE IF NOT EXISTS `MetrixObject_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`id`  int(10)  NULL DEFAULT NULL
 ,`run_id`  varchar(512)  NULL DEFAULT NULL
 ,`object_value`  varbinary(8000)  NULL DEFAULT NULL
 ,`state`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For NewsItem 
--

CREATE TABLE IF NOT EXISTS `NewsItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNewsItem`  int(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`title`  varchar(200)  NULL DEFAULT NULL
 ,`message`  varchar(4000)  NULL DEFAULT NULL
 ,`date`  datetime  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Notification 
--

CREATE TABLE IF NOT EXISTS `Notification_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNotification`  int(10)  NULL DEFAULT NULL
 ,`idUserTarget`  int(10)  NULL DEFAULT NULL
 ,`idLabTarget`  int(10)  NULL DEFAULT NULL
 ,`sourceType`  varchar(20)  NULL DEFAULT NULL
 ,`message`  varchar(250)  NULL DEFAULT NULL
 ,`date`  datetime  NULL DEFAULT NULL
 ,`expID`  varchar(25)  NULL DEFAULT NULL
 ,`type`  varchar(25)  NULL DEFAULT NULL
 ,`fullNameUser`  varchar(100)  NULL DEFAULT NULL
 ,`imageSource`  varchar(50)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For NucleotideType 
--

CREATE TABLE IF NOT EXISTS `NucleotideType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For NumberSequencingCycles 
--

CREATE TABLE IF NOT EXISTS `NumberSequencingCycles_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`numberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For NumberSequencingCyclesAllowed 
--

CREATE TABLE IF NOT EXISTS `NumberSequencingCyclesAllowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`protocolDescription`  longtext  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For OligoBarcode 
--

CREATE TABLE IF NOT EXISTS `OligoBarcode_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For OligoBarcodeScheme 
--

CREATE TABLE IF NOT EXISTS `OligoBarcodeScheme_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`oligoBarcodeScheme`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For OligoBarcodeSchemeAllowed 
--

CREATE TABLE IF NOT EXISTS `OligoBarcodeSchemeAllowed_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOligoBarcodeSchemeAllowed`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeScheme`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`isIndexGroupB`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Organism 
--

CREATE TABLE IF NOT EXISTS `Organism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`organism`  varchar(50)  NULL DEFAULT NULL
 ,`abbreviation`  varchar(10)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`das2Name`  varchar(200)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
 ,`binomialName`  varchar(200)  NULL DEFAULT NULL
 ,`NCBITaxID`  varchar(45)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For OtherAccountFieldsConfiguration 
--

CREATE TABLE IF NOT EXISTS `OtherAccountFieldsConfiguration_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idOtherAccountFieldsConfiguration`  int(10)  NULL DEFAULT NULL
 ,`fieldName`  varchar(50)  NULL DEFAULT NULL
 ,`include`  char(1)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Plate 
--

CREATE TABLE IF NOT EXISTS `Plate_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idInstrumentRun`  int(10)  NULL DEFAULT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`quadrant`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`comments`  varchar(200)  NULL DEFAULT NULL
 ,`label`  varchar(50)  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`creator`  varchar(50)  NULL DEFAULT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PlateType 
--

CREATE TABLE IF NOT EXISTS `PlateType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePlateType`  varchar(10)  NULL DEFAULT NULL
 ,`plateTypeDescription`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PlateWell 
--

CREATE TABLE IF NOT EXISTS `PlateWell_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlateWell`  int(10)  NULL DEFAULT NULL
 ,`row`  varchar(50)  NULL DEFAULT NULL
 ,`col`  int(10)  NULL DEFAULT NULL
 ,`ind`  int(10)  NULL DEFAULT NULL
 ,`idPlate`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`redoFlag`  char(1)  NULL DEFAULT NULL
 ,`isControl`  char(1)  NULL DEFAULT NULL
 ,`idAssay`  int(10)  NULL DEFAULT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Price 
--

CREATE TABLE IF NOT EXISTS `Price_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalAcademic`  decimal(7,2)  NULL DEFAULT NULL
 ,`unitPriceExternalCommercial`  decimal(7,2)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceCategory 
--

CREATE TABLE IF NOT EXISTS `PriceCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`codeBillingChargeKind`  varchar(10)  NULL DEFAULT NULL
 ,`pluginClassName`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter1`  varchar(500)  NULL DEFAULT NULL
 ,`dictionaryClassNameFilter2`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceCategoryStep 
--

CREATE TABLE IF NOT EXISTS `PriceCategoryStep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceCriteria 
--

CREATE TABLE IF NOT EXISTS `PriceCriteria_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceCriteria`  int(10)  NULL DEFAULT NULL
 ,`filter1`  varchar(10)  NULL DEFAULT NULL
 ,`filter2`  varchar(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceSheet 
--

CREATE TABLE IF NOT EXISTS `PriceSheet_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceSheetPriceCategory 
--

CREATE TABLE IF NOT EXISTS `PriceSheetPriceCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PriceSheetRequestCategory 
--

CREATE TABLE IF NOT EXISTS `PriceSheetRequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPriceSheet`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Primer 
--

CREATE TABLE IF NOT EXISTS `Primer_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPrimer`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`sequence`  varchar(2000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Product 
--

CREATE TABLE IF NOT EXISTS `Product_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`idPrice`  int(10)  NULL DEFAULT NULL
 ,`orderQty`  int(10)  NULL DEFAULT NULL
 ,`useQty`  int(10)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`batchSamplesByUseQuantity`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductLedger 
--

CREATE TABLE IF NOT EXISTS `ProductLedger_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductLedger`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`comment`  varchar(5000)  NULL DEFAULT NULL
 ,`timeStame`  datetime  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductLineItem 
--

CREATE TABLE IF NOT EXISTS `ProductLineItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductLineItem`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
 ,`qty`  int(10)  NULL DEFAULT NULL
 ,`unitPrice`  decimal(7,2)  NULL DEFAULT NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductOrder 
--

CREATE TABLE IF NOT EXISTS `ProductOrder_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`submitDate`  datetime  NULL DEFAULT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`quoteNumber`  varchar(50)  NULL DEFAULT NULL
 ,`quoteReceivedDate`  datetime  NULL DEFAULT NULL
 ,`uuid`  varchar(36)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`productOrderNumber`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductOrderFile 
--

CREATE TABLE IF NOT EXISTS `ProductOrderFile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProductOrderFile`  int(10)  NULL DEFAULT NULL
 ,`idProductOrder`  int(10)  NULL DEFAULT NULL
 ,`fileName`  varchar(2000)  NULL DEFAULT NULL
 ,`fileSize`  decimal(14,0)  NULL DEFAULT NULL
 ,`createDate`  date  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductOrderStatus 
--

CREATE TABLE IF NOT EXISTS `ProductOrderStatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProductOrderStatus`  varchar(10)  NULL DEFAULT NULL
 ,`productOrderStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProductType 
--

CREATE TABLE IF NOT EXISTS `ProductType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idPriceCategory`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Project 
--

CREATE TABLE IF NOT EXISTS `Project_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(4000)  NULL DEFAULT NULL
 ,`publicDateForAppUsers`  datetime  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Property 
--

CREATE TABLE IF NOT EXISTS `Property_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(50)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`isRequired`  char(1)  NULL DEFAULT NULL
 ,`forSample`  char(1)  NULL DEFAULT NULL
 ,`forAnalysis`  char(1)  NULL DEFAULT NULL
 ,`forDataTrack`  char(1)  NULL DEFAULT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`forRequest`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyAnalysisType 
--

CREATE TABLE IF NOT EXISTS `PropertyAnalysisType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAnalysisType`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyAppUser 
--

CREATE TABLE IF NOT EXISTS `PropertyAppUser_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyDictionary 
--

CREATE TABLE IF NOT EXISTS `PropertyDictionary_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyDictionary`  int(10)  NULL DEFAULT NULL
 ,`propertyName`  varchar(200)  NULL DEFAULT NULL
 ,`propertyValue`  varchar(2000)  NULL DEFAULT NULL
 ,`propertyDescription`  varchar(2000)  NULL DEFAULT NULL
 ,`forServerOnly`  char(1)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyEntry 
--

CREATE TABLE IF NOT EXISTS `PropertyEntry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(200)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
 ,`idDataTrack`  int(10)  NULL DEFAULT NULL
 ,`idAnalysis`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyEntryOption 
--

CREATE TABLE IF NOT EXISTS `PropertyEntryOption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyEntryValue 
--

CREATE TABLE IF NOT EXISTS `PropertyEntryValue_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyEntryValue`  int(10) unsigned  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idPropertyEntry`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyOption 
--

CREATE TABLE IF NOT EXISTS `PropertyOption_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPropertyOption`  int(10)  NULL DEFAULT NULL
 ,`value`  varchar(200)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyOrganism 
--

CREATE TABLE IF NOT EXISTS `PropertyOrganism_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyPlatformApplication 
--

CREATE TABLE IF NOT EXISTS `PropertyPlatformApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idPlatformApplication`  int(10)  NULL DEFAULT NULL
 ,`idProperty`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For PropertyType 
--

CREATE TABLE IF NOT EXISTS `PropertyType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codePropertyType`  varchar(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ProtocolType 
--

CREATE TABLE IF NOT EXISTS `ProtocolType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For QualityControlStep 
--

CREATE TABLE IF NOT EXISTS `QualityControlStep_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`qualityControlStep`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyCode`  varchar(50)  NULL DEFAULT NULL
 ,`mageOntologyDefinition`  varchar(5000)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For QualityControlStepEntry 
--

CREATE TABLE IF NOT EXISTS `QualityControlStepEntry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idQualityControlStepEntry`  int(10)  NULL DEFAULT NULL
 ,`codeQualityControlStep`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`valueString`  varchar(100)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ReactionType 
--

CREATE TABLE IF NOT EXISTS `ReactionType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeReactionType`  varchar(10)  NULL DEFAULT NULL
 ,`reactionType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Request 
--

CREATE TABLE IF NOT EXISTS `Request_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(50)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`codeProtocolType`  varchar(10)  NULL DEFAULT NULL
 ,`protocolNumber`  varchar(100)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idBillingAccount`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idProject`  int(10)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`idSampleTypeDefault`  int(10)  NULL DEFAULT NULL
 ,`idOrganismSampleDefault`  int(10)  NULL DEFAULT NULL
 ,`idSampleSourceDefault`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethodDefault`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`notes`  varchar(2000)  NULL DEFAULT NULL
 ,`completedDate`  datetime  NULL DEFAULT NULL
 ,`isArrayINFORequest`  char(1)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`lastModifyDate`  datetime  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`privacyExpirationDate`  datetime  NULL DEFAULT NULL
 ,`description`  varchar(5000)  NULL DEFAULT NULL
 ,`corePrepInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(5000)  NULL DEFAULT NULL
 ,`captureLibDesignId`  varchar(200)  NULL DEFAULT NULL
 ,`avgInsertSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`avgInsertSizeTo`  int(10)  NULL DEFAULT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`idSubmitter`  int(10)  NULL DEFAULT NULL
 ,`numberIScanChips`  int(10)  NULL DEFAULT NULL
 ,`idIScanChip`  int(10)  NULL DEFAULT NULL
 ,`coreToExtractDNA`  char(1)  NULL DEFAULT NULL
 ,`applicationNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`processingDate`  datetime  NULL DEFAULT NULL
 ,`codeIsolationPrepType`  varchar(15)  NULL DEFAULT NULL
 ,`bioinformaticsAssist`  char(1)  NULL DEFAULT NULL
 ,`hasPrePooledLibraries`  char(1)  NULL DEFAULT NULL
 ,`numPrePooledTubes`  int(10)  NULL DEFAULT NULL
 ,`includeBisulfideConversion`  char(1)  NULL DEFAULT NULL
 ,`includeQubitConcentration`  char(1)  NULL DEFAULT NULL
 ,`alignToGenomeBuild`  char(1)  NULL DEFAULT NULL
 ,`adminNotes`  varchar(5000)  NULL DEFAULT NULL
 ,`idProduct`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestCategory 
--

CREATE TABLE IF NOT EXISTS `RequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`requestCategory`  varchar(50)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`numberOfChannels`  int(10)  NULL DEFAULT NULL
 ,`notes`  varchar(500)  NULL DEFAULT NULL
 ,`icon`  varchar(200)  NULL DEFAULT NULL
 ,`type`  varchar(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`isInternal`  char(1)  NULL DEFAULT NULL
 ,`isExternal`  char(1)  NULL DEFAULT NULL
 ,`refrainFromAutoDelete`  char(1)  NULL DEFAULT NULL
 ,`isClinicalResearch`  char(1)  NULL DEFAULT NULL
 ,`isOwnerOnly`  char(1)  NULL DEFAULT NULL
 ,`sampleBatchSize`  int(10)  NULL DEFAULT NULL
 ,`codeProductType`  varchar(10)  NULL DEFAULT NULL
 ,`associatedWithAnalysis`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestCategoryApplication 
--

CREATE TABLE IF NOT EXISTS `RequestCategoryApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idLabelingProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idHybProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idScanProtocolDefault`  int(10)  NULL DEFAULT NULL
 ,`idFeatureExtractionProtocolDefault`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestCategoryType 
--

CREATE TABLE IF NOT EXISTS `RequestCategoryType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestCategoryType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(50)  NULL DEFAULT NULL
 ,`defaultIcon`  varchar(100)  NULL DEFAULT NULL
 ,`isIllumina`  char(1)  NULL DEFAULT NULL
 ,`hasChannels`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestCollaborator 
--

CREATE TABLE IF NOT EXISTS `RequestCollaborator_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`canUploadData`  char(1)  NULL DEFAULT NULL
 ,`canUpdate`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestHybridization 
--

CREATE TABLE IF NOT EXISTS `RequestHybridization_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestSeqLibTreatment 
--

CREATE TABLE IF NOT EXISTS `RequestSeqLibTreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestStatus 
--

CREATE TABLE IF NOT EXISTS `RequestStatus_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeRequestStatus`  varchar(10)  NULL DEFAULT NULL
 ,`requestStatus`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For RequestToTopic 
--

CREATE TABLE IF NOT EXISTS `RequestToTopic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Sample 
--

CREATE TABLE IF NOT EXISTS `Sample_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`name`  varchar(200)  NULL DEFAULT NULL
 ,`description`  varchar(2000)  NULL DEFAULT NULL
 ,`concentration`  decimal(8,3)  NULL DEFAULT NULL
 ,`codeConcentrationUnit`  varchar(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`otherOrganism`  varchar(100)  NULL DEFAULT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`otherSamplePrepMethod`  varchar(300)  NULL DEFAULT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idOligoBarcode`  int(10)  NULL DEFAULT NULL
 ,`qualDate`  datetime  NULL DEFAULT NULL
 ,`qualFailed`  char(1)  NULL DEFAULT NULL
 ,`qualBypassed`  char(1)  NULL DEFAULT NULL
 ,`qual260nmTo280nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qual260nmTo230nmRatio`  decimal(3,2)  NULL DEFAULT NULL
 ,`qualCalcConcentration`  decimal(8,2)  NULL DEFAULT NULL
 ,`qual28sTo18sRibosomalRatio`  decimal(2,1)  NULL DEFAULT NULL
 ,`qualRINNumber`  varchar(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`fragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepByCore`  char(1)  NULL DEFAULT NULL
 ,`seqPrepDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepBypassed`  char(1)  NULL DEFAULT NULL
 ,`qualFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`qualFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepLibConcentration`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepQualCodeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeFrom`  int(10)  NULL DEFAULT NULL
 ,`seqPrepGelFragmentSizeTo`  int(10)  NULL DEFAULT NULL
 ,`seqPrepStockLibVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockEBVol`  decimal(8,1)  NULL DEFAULT NULL
 ,`seqPrepStockDate`  datetime  NULL DEFAULT NULL
 ,`seqPrepStockFailed`  char(1)  NULL DEFAULT NULL
 ,`seqPrepStockBypassed`  char(1)  NULL DEFAULT NULL
 ,`prepInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`ccNumber`  varchar(20)  NULL DEFAULT NULL
 ,`multiplexGroupNumber`  int(10)  NULL DEFAULT NULL
 ,`barcodeSequence`  varchar(20)  NULL DEFAULT NULL
 ,`meanLibSizeActual`  int(10)  NULL DEFAULT NULL
 ,`idOligoBarcodeB`  int(10)  NULL DEFAULT NULL
 ,`barcodeSequenceB`  varchar(20)  NULL DEFAULT NULL
 ,`qubitConcentration`  decimal(8,3)  NULL DEFAULT NULL
 ,`groupName`  varchar(200)  NULL DEFAULT NULL
 ,`qcCodeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleDropOffLocation 
--

CREATE TABLE IF NOT EXISTS `SampleDropOffLocation_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleDropOffLocation`  int(10)  NULL DEFAULT NULL
 ,`sampleDropOffLocation`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleExperimentFile 
--

CREATE TABLE IF NOT EXISTS `SampleExperimentFile_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleExperimentFile`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead1`  int(10)  NULL DEFAULT NULL
 ,`idExpFileRead2`  int(10)  NULL DEFAULT NULL
 ,`seqRunNumber`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleFileType 
--

CREATE TABLE IF NOT EXISTS `SampleFileType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSampleFileType`  varchar(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SamplePrepMethod 
--

CREATE TABLE IF NOT EXISTS `SamplePrepMethod_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSamplePrepMethod`  int(10)  NULL DEFAULT NULL
 ,`samplePrepMethod`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleSource 
--

CREATE TABLE IF NOT EXISTS `SampleSource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleSource`  int(10)  NULL DEFAULT NULL
 ,`sampleSource`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleType 
--

CREATE TABLE IF NOT EXISTS `SampleType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`sampleType`  varchar(50)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`codeNucleotideType`  varchar(50)  NULL DEFAULT NULL
 ,`notes`  varchar(5000)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SampleTypeRequestCategory 
--

CREATE TABLE IF NOT EXISTS `SampleTypeRequestCategory_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSampleTypeRequestCategory`  int(10)  NULL DEFAULT NULL
 ,`idSampleType`  int(10)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For ScanProtocol 
--

CREATE TABLE IF NOT EXISTS `ScanProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idScanProtocol`  int(10)  NULL DEFAULT NULL
 ,`scanProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SealType 
--

CREATE TABLE IF NOT EXISTS `SealType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSealType`  varchar(10)  NULL DEFAULT NULL
 ,`sealType`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Segment 
--

CREATE TABLE IF NOT EXISTS `Segment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSegment`  int(10)  NULL DEFAULT NULL
 ,`length`  int(10) unsigned  NULL DEFAULT NULL
 ,`name`  varchar(100)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
 ,`sortOrder`  int(10) unsigned  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SeqLibProtocol 
--

CREATE TABLE IF NOT EXISTS `SeqLibProtocol_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`seqLibProtocol`  varchar(200)  NULL DEFAULT NULL
 ,`description`  longtext  NULL DEFAULT NULL
 ,`url`  varchar(500)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`adapterSequenceRead1`  varchar(500)  NULL DEFAULT NULL
 ,`adapterSequenceRead2`  varchar(500)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SeqLibProtocolApplication 
--

CREATE TABLE IF NOT EXISTS `SeqLibProtocolApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibProtocol`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SeqLibTreatment 
--

CREATE TABLE IF NOT EXISTS `SeqLibTreatment_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqLibTreatment`  int(10)  NULL DEFAULT NULL
 ,`seqLibTreatment`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SeqRunType 
--

CREATE TABLE IF NOT EXISTS `SeqRunType_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`seqRunType`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SequenceLane 
--

CREATE TABLE IF NOT EXISTS `SequenceLane_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`number`  varchar(100)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idSeqRunType`  int(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCycles`  int(10)  NULL DEFAULT NULL
 ,`analysisInstructions`  varchar(2000)  NULL DEFAULT NULL
 ,`idGenomeBuildAlignTo`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`readCount`  int(10)  NULL DEFAULT NULL
 ,`pipelineVersion`  varchar(10)  NULL DEFAULT NULL
 ,`idNumberSequencingCyclesAllowed`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SequencingControl 
--

CREATE TABLE IF NOT EXISTS `SequencingControl_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSequencingControl`  int(10)  NULL DEFAULT NULL
 ,`sequencingControl`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SequencingPlatform 
--

CREATE TABLE IF NOT EXISTS `SequencingPlatform_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSequencingPlatform`  varchar(10)  NULL DEFAULT NULL
 ,`sequencingPlatform`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Slide 
--

CREATE TABLE IF NOT EXISTS `Slide_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlide`  int(10)  NULL DEFAULT NULL
 ,`barcode`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`slideName`  varchar(200)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SlideDesign 
--

CREATE TABLE IF NOT EXISTS `SlideDesign_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideDesign`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`slideDesignProtocolName`  varchar(100)  NULL DEFAULT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`accessionNumberArrayExpress`  varchar(200)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SlideProduct 
--

CREATE TABLE IF NOT EXISTS `SlideProduct_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(500)  NULL DEFAULT NULL
 ,`catalogNumber`  varchar(100)  NULL DEFAULT NULL
 ,`isCustom`  char(1)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`idOrganism`  int(10)  NULL DEFAULT NULL
 ,`arraysPerSlide`  int(10)  NULL DEFAULT NULL
 ,`slidesInSet`  int(10)  NULL DEFAULT NULL
 ,`isSlideSet`  char(1)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`idBillingSlideProductClass`  int(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SlideProductApplication 
--

CREATE TABLE IF NOT EXISTS `SlideProductApplication_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSlideProduct`  int(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SlideSource 
--

CREATE TABLE IF NOT EXISTS `SlideSource_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeSlideSource`  varchar(10)  NULL DEFAULT NULL
 ,`slideSource`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For State 
--

CREATE TABLE IF NOT EXISTS `State_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeState`  varchar(10)  NULL DEFAULT NULL
 ,`state`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Step 
--

CREATE TABLE IF NOT EXISTS `Step_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeStep`  varchar(10)  NULL DEFAULT NULL
 ,`step`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
 ,`sortOrder`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For SubmissionInstruction 
--

CREATE TABLE IF NOT EXISTS `SubmissionInstruction_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idSubmissionInstruction`  int(10)  NULL DEFAULT NULL
 ,`description`  varchar(200)  NULL DEFAULT NULL
 ,`url`  varchar(2000)  NULL DEFAULT NULL
 ,`codeRequestCategory`  varchar(10)  NULL DEFAULT NULL
 ,`codeApplication`  varchar(10)  NULL DEFAULT NULL
 ,`codeBioanalyzerChipType`  varchar(10)  NULL DEFAULT NULL
 ,`idBillingSlideServiceClass`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Topic 
--

CREATE TABLE IF NOT EXISTS `Topic_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTopic`  int(10)  NULL DEFAULT NULL
 ,`name`  varchar(2000)  NULL DEFAULT NULL
 ,`description`  varchar(10000)  NULL DEFAULT NULL
 ,`idParentTopic`  int(10)  NULL DEFAULT NULL
 ,`idLab`  int(10)  NULL DEFAULT NULL
 ,`createdBy`  varchar(200)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`idInstitution`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For TreatmentEntry 
--

CREATE TABLE IF NOT EXISTS `TreatmentEntry_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idTreatmentEntry`  int(10)  NULL DEFAULT NULL
 ,`treatment`  varchar(2000)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`otherLabel`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For UnloadDataTrack 
--

CREATE TABLE IF NOT EXISTS `UnloadDataTrack_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idUnloadDataTrack`  int(10)  NULL DEFAULT NULL
 ,`typeName`  varchar(2000)  NULL DEFAULT NULL
 ,`idAppUser`  int(10)  NULL DEFAULT NULL
 ,`idGenomeBuild`  int(10)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For UserPermissionKind 
--

CREATE TABLE IF NOT EXISTS `UserPermissionKind_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeUserPermissionKind`  varchar(10)  NULL DEFAULT NULL
 ,`userPermissionKind`  varchar(50)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Vendor 
--

CREATE TABLE IF NOT EXISTS `Vendor_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idVendor`  int(10)  NULL DEFAULT NULL
 ,`vendorName`  varchar(100)  NULL DEFAULT NULL
 ,`description`  varchar(100)  NULL DEFAULT NULL
 ,`isActive`  char(1)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For Visibility 
--

CREATE TABLE IF NOT EXISTS `Visibility_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`codeVisibility`  varchar(10)  NULL DEFAULT NULL
 ,`visibility`  varchar(100)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$


--
-- Audit Table For WorkItem 
--

CREATE TABLE IF NOT EXISTS `WorkItem_Audit` (
  `AuditAppuser`       varchar(128) NOT NULL
 ,`AuditOperation`     char(1)      NOT NULL
 ,`AuditSystemUser`    varchar(30)  NOT NULL
 ,`AuditOperationDate` datetime     NOT NULL
 ,`idWorkItem`  int(10)  NULL DEFAULT NULL
 ,`codeStepNext`  varchar(10)  NULL DEFAULT NULL
 ,`idSample`  int(10)  NULL DEFAULT NULL
 ,`idLabeledSample`  int(10)  NULL DEFAULT NULL
 ,`idHybridization`  int(10)  NULL DEFAULT NULL
 ,`idRequest`  int(10)  NULL DEFAULT NULL
 ,`createDate`  datetime  NULL DEFAULT NULL
 ,`idSequenceLane`  int(10)  NULL DEFAULT NULL
 ,`idFlowCellChannel`  int(10)  NULL DEFAULT NULL
 ,`idCoreFacility`  int(10)  NULL DEFAULT NULL
 ,`status`  varchar(50)  NULL DEFAULT NULL
) ENGINE=InnoDB
$$
